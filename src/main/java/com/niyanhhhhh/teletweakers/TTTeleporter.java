package com.niyanhhhhh.teletweakers;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TTTeleporter extends Teleporter {

    public static TTTeleporter getTTTeleporter(MinecraftServer server, int dim) {
        WorldServer ws = server.getWorld(dim);
        for (Teleporter t : ws.customTeleporters) {
            if (t instanceof TTTeleporter) {
                return (TTTeleporter) t;
            }
        }

        TTTeleporter tp = new TTTeleporter(ws);
        ws.customTeleporters.add(tp);
        return tp;
    }

    public TTTeleporter(WorldServer worldIn) {
        super(worldIn);
    }

    @Override
    public void placeInPortal(@Nonnull Entity entityIn, float rotationYaw) {
        BlockPos entityPos = entityIn.getPosition();
        double desX = entityPos.getX();
        double desY = entityPos.getY();
        if (entityIn.dimension == 0) {
            desY = 15;
        }
        double desZ = entityPos.getZ();
        if (entityIn instanceof EntityPlayerMP) {
            ((EntityPlayerMP) entityIn).connection.setPlayerLocation(desX, desY, desZ, entityIn.rotationYaw,
                    entityIn.rotationPitch);
        } else {
            entityIn.setLocationAndAngles(desX, desY, desZ, entityIn.rotationYaw, entityIn.rotationPitch);
        }

        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;

        IBlockState air = Blocks.AIR.getDefaultState();
        IBlockState fire = Blocks.FIRE.getDefaultState();
        world.setBlockState(entityPos, air);
        world.setBlockState(entityPos.west(), fire);
        world.setBlockState(entityPos.east(), fire);
        world.setBlockState(entityPos.north(), fire);
        world.setBlockState(entityPos.south(), fire);
        world.setBlockState(entityPos.up(), air);
    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        return false;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return false;
    }

}
