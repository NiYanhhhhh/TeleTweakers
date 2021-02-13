package com.niyanhhhhh.teletweakers;

import java.lang.reflect.Field;

import com.niyanhhhhh.teletweakers.config.TTConfig;
import com.niyanhhhhh.teletweakers.entity.EntityPortalBreaker;
import com.niyanhhhhh.teletweakers.storage.SpecialPositionWorldData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import twilightforest.TFConfig;
import twilightforest.TFTeleporter;

@Mod.EventBusSubscriber(modid = TTMain.MODID)
public class TTCommonEventHandler {

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        BlockPos pos = event.getPos();
        long eventTime = event.getWorld().getTotalWorldTime();
        World world = event.getWorld();

        if (TTConfig.portalTweaks) {
            boolean canSpawn = false;
            SpecialPositionWorldData data = SpecialPositionWorldData.get(event.getWorld());
            for (int i = 0; i < data.getSize(); ++i) {
                if ((eventTime - data.getTime(i)) <= 100 && !world.isRemote && ((world.provider.getDimension() == -1)
                        || (world.provider.getDimension() == 0 && pos.getY() <= 10))) {
                    BlockPos explosionPos = new BlockPos(data.getPosition(i));
                    if (isValidPos(world, explosionPos)) {
                        canSpawn = true;
                        data.remove(i);
                        EntityPortalBreaker pb = new EntityPortalBreaker(world);
                        pb.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        world.spawnEntity(pb);
                        break;
                    }
                }
            }
            event.setCanceled(!canSpawn);
        }
    }

    @SubscribeEvent
    public static void recordExplosionPos(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        Field sizeField;
        Vec3d explosionPos = explosion.getPosition();
        World world = event.getWorld();

        try {
            sizeField = Explosion.class.getDeclaredField("field_77280_f");
            sizeField.setAccessible(true);
            float size = (float) sizeField.get(explosion);
            System.out.println("explosion size: " + size + "\nexplosion pos: " + explosionPos);

            if (size > 6.5) {
                long time = world.getTotalWorldTime();
                SpecialPositionWorldData.get(world).add(explosionPos, time);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void teleportToTF(PlayerTickEvent event) {
        if (Loader.isModLoaded("twilightforest")) {
            EntityPlayer player = event.player;
            World world = player.world;
            double px = player.getPosition().getX();
            long time = world.getWorldTime();

            time = time % 24000;
            if (time > 12000 && time < 14000 && px < -20000 && player instanceof EntityPlayerMP
                    && player.isPlayerFullyAsleep()) {
                teleportPlayer(player, TFConfig.dimension.dimensionID);
            }
        }
    }

    @SubscribeEvent
    public static void preventEndPortalSpawn(RightClickBlock event) {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        if (!world.isRemote) {
            ItemStack itemStack = player.getHeldItem(event.getHand());
            IBlockState iBlockState = world.getBlockState(pos);

            if (player.canPlayerEdit(pos.offset(event.getFace()), event.getFace(), itemStack)
                    && iBlockState.getBlock() == Blocks.END_PORTAL_FRAME
                    && !((Boolean) iBlockState.getValue(BlockEndPortalFrame.EYE)).booleanValue()
                    && itemStack.getItem() == Items.ENDER_EYE) {

                world.setBlockState(pos, iBlockState.withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(true)), 2);
                world.updateComparatorOutputLevel(pos, Blocks.END_PORTAL_FRAME);
                itemStack.shrink(1);

                BlockPattern.PatternHelper blockpattern$patternhelper = BlockEndPortalFrame.getOrCreatePortalShape()
                        .match(world, pos);
                if (blockpattern$patternhelper != null) {
                    System.out.println("end portal will form!");
                    world.playEvent(2001, pos, Block.getStateId(Blocks.END_PORTAL_FRAME.getDefaultState()));
                    world.setBlockToAir(pos);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDimensionTravel(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        World worldBefore = entity.world;
        BlockPos pos = entity.getPosition();
        int dimensionIn = event.getDimension();
        int dimensionBefore = entity.dimension;
        if (dimensionIn == 0 && dimensionBefore == -1) {
            if (entity instanceof EntityPlayerMP) {
                if (((EntityPlayerMP) entity).getHeldItemMainhand().getItem() != Items.NETHER_STAR) {
                    EntityPortalBreaker pb = new EntityPortalBreaker(worldBefore);
                    pb.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    worldBefore.spawnEntity(pb);
                    event.setCanceled(true);
                } else {
                }
            }
        }
    }

//    public static boolean checkStructure(ExplosionEvent.Detonate event, Vec3d blockPos) {
//        World world = event.getWorld();
//        double x0 = blockPos.x;
//        double y0 = blockPos.y;
//        double z0 = blockPos.z;
//        int check = 1;
//
//        for (int i = -2; i <= 2; i++) {
//            for (int j = -2; j <= 2; j++) {
//                if (Math.abs(i) == 2 && Math.abs(j) == 2) {
//                    continue;
//                }
//                IBlockState state = world.getBlockState(new BlockPos(x0 + i, y0, z0 + j));
//                IBlockState checkState;
//                if (Math.abs(i) > 1 || Math.abs(j) > 1) {
//                    checkState = Blocks.OBSIDIAN.getDefaultState();
//                } else {
//                    checkState = Blocks.AIR.getDefaultState();
//                }
//                if (state != checkState) {
//                    check = 0;
//                } else {
//                }
//            }
//        }
//
//        if (check == 1)
//            return true;
//        else
//            return false;
//    }

    private static boolean isValidPos(World world, BlockPos pos) {
        BlockPortal.Size size = new BlockPortal.Size(world, pos, EnumFacing.Axis.X);
        if (size.isValid()) {
            return true;
        } else {
            BlockPortal.Size size1 = new BlockPortal.Size(world, pos, EnumFacing.Axis.Z);
            if (size1.isValid()) {
                return true;
            } else {
                System.out.println("can not make portal");
                return false;
            }
        }

    }

    private static void teleportPlayer(Entity entity, int dim) {

        if (entity.isDead || entity.world.isRemote) {
            return;
        }

        entity.dismountRidingEntity();
        entity.removePassengers();
        int destination = getDestination(entity, dim);

        if (dim == TFConfig.dimension.dimensionID) {
            entity.changeDimension(destination, TFTeleporter.getTeleporterForDim(entity.getServer(), destination));
        } else if (dim == 1) {
            entity.changeDimension(destination);
        }

    }

    private static int getDestination(Entity entity, int dim) {
        if (entity.dimension == 0) {
            return dim;
        } else {
            if (entity.dimension != dim) {
                return entity.dimension;
            } else {
                return 0;
            }
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(TTMain.MODID)) {
            ConfigManager.sync(TTMain.MODID, Config.Type.INSTANCE);
        }
    }

}
