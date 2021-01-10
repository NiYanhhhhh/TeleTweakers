package com.niyanhhhhh.teletweakers.item;

import com.niyanhhhhh.teletweakers.TTMain;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TTMain.MODID)
public class ItemInit {

    private static String modid = TTMain.MODID;

    public static Item testExplosion = new Item() {

        @Override
        public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
            double px = MathHelper.floor(player.posX) + 0.5;
            double py = MathHelper.floor(player.posY) + 0.5;
            double pz = MathHelper.floor(player.posZ) + 0.5;
            world.newExplosion(null, px, py, pz, 8F, true, true);
            return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
        }

    }.setCreativeTab(CreativeTabs.MISC)
            .setUnlocalizedName(modid + ".test_explosion")
            .setRegistryName(modid + ":test_explosion");

    @SubscribeEvent
    public static void registerItem(RegistryEvent.Register<Item> event) {

        event.getRegistry().register(testExplosion);

    }

}
