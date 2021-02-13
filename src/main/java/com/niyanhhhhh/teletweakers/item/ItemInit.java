package com.niyanhhhhh.teletweakers.item;

import com.niyanhhhhh.teletweakers.TTMain;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TTMain.MODID)
public class ItemInit {

    private static String modid = TTMain.MODID;

    public static final Item TEST_EXPLOSION = new Item() {

        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                EnumFacing facing, float hitX, float hitY, float hitZ) {
            pos = pos.offset(facing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (!player.canPlayerEdit(pos, facing, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                double px = MathHelper.floor(pos.getX()) + 0.5;
                double py = MathHelper.floor(pos.getY()) + 0.5;
                double pz = MathHelper.floor(pos.getZ()) + 0.5;
                worldIn.newExplosion(null, px, py, pz, 8F, true, true);
                return EnumActionResult.SUCCESS;
            }
        }

    }.setCreativeTab(CreativeTabs.MISC).setUnlocalizedName(modid + ".test_explosion")
            .setRegistryName(modid + ":test_explosion");

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {

        event.getRegistry().register(TEST_EXPLOSION);

    }

}
