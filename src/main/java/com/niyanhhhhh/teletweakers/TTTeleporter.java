package com.niyanhhhhh.teletweakers;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TTTeleporter extends Teleporter {

    public TTTeleporter(WorldServer worldIn, World worldOld) {
        super(worldIn);
        this.worldIn = worldIn;
        this.worldOld = worldOld;
    }
    
    public WorldServer worldIn;
    public World worldOld;

    public TTTeleporter getTTTeleporter() {
        for (Teleporter t : worldIn.customTeleporters) {
            if (t instanceof TTTeleporter) {
                return (TTTeleporter) t;
            }
        }

        worldIn.customTeleporters.add(this);
        return this;
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
        BlockPos desPos = new BlockPos(desX, desY, desZ);
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
        world.setBlockState(desPos, air);
        world.setBlockState(desPos.west(), fire);
        world.setBlockState(desPos.east(), fire);
        world.setBlockState(desPos.north(), fire);
        world.setBlockState(desPos.south(), fire);
        world.setBlockState(desPos.up(), air);
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
