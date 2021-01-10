package com.niyanhhhhh.teletweakers;

import java.lang.reflect.Field;
import java.util.List;

import com.niyanhhhhh.teletweakers.config.TTConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TTMain.MODID)
public class TTCommonEventHandler {

    @SubscribeEvent
    public static void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        if (TTConfig.cancelPortalSpawn) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void teleportToNether(ExplosionEvent.Detonate event) {
        Explosion explosion = event.getExplosion();
        Field sizeField;
        Vec3d explosionPos = explosion.getPosition();
        List<Entity> affectedEntity = event.getAffectedEntities();
        System.out.println(affectedEntity);

        try {
            sizeField = Explosion.class.getDeclaredField("field_77280_f");
            sizeField.setAccessible(true);
            float size = (float) sizeField.get(explosion);

            if (explosionPos.y <= 7 && size > 7 && checkStructure(event, explosionPos) && !affectedEntity.isEmpty()) {
                affectedEntity.forEach(entity -> {
                    if (entity instanceof EntityPlayerMP) {
                        if (isInExplosionCenter(entity.getPositionVector(), explosionPos)) {
                            System.out.println("teleport " + entity);
                            teleportPlayer(entity, true);
                        }
                    }
                });
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

    public static boolean isInExplosionCenter(Vec3d playerPos, Vec3d explosionPos) {
        int px = MathHelper.floor(playerPos.x);
        int py = MathHelper.floor(playerPos.x);
        int pz = MathHelper.floor(playerPos.x);

        int ex = MathHelper.floor(explosionPos.x);
        int ey = MathHelper.floor(explosionPos.x);
        int ez = MathHelper.floor(explosionPos.x);

        if (px == ex && py == ey && pz == ez) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkStructure(ExplosionEvent.Detonate event, Vec3d blockPos) {
        World world = event.getWorld();
        double x0 = blockPos.x;
        double y0 = blockPos.y;
        double z0 = blockPos.z;
        int check = 1;

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) == 2 && Math.abs(j) == 2) {
                    continue;
                }
                IBlockState state = world.getBlockState(new BlockPos(x0 + i, y0, z0 + j));
                IBlockState checkState;
                if (Math.abs(i) > 1 || Math.abs(j) > 1) {
                    checkState = Blocks.OBSIDIAN.getDefaultState();
                } else {
                    checkState = Blocks.AIR.getDefaultState();
                }
                if (state != checkState) {
                    check = 0;
                } else {
                }
            }
        }

        if (check == 1)
            return true;
        else
            return false;
    }

    private static void teleportPlayer(Entity entity, boolean forcedEntry) {

        if (entity.isDead || entity.world.isRemote) {
            return;
        }

        if (!forcedEntry) {
            return;
        }

        entity.dismountRidingEntity();
        entity.removePassengers();
        int destination = getDestination(entity);
        WorldServer ws = entity.getServer().getWorld(destination);

        entity.changeDimension(destination, TTTeleporter.getTTTeleporter(entity.getServer(), destination));

    }

    private static int getDestination(Entity entity) {
        return entity.dimension != -1 ? -1 : 0;
    }

}
