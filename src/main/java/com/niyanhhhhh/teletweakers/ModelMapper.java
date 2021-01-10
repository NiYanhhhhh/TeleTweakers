package com.niyanhhhhh.teletweakers;

import com.niyanhhhhh.teletweakers.item.ItemInit;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = "teletweakers")
public class ModelMapper {
    
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ItemInit.testExplosion, 0, new ModelResourceLocation(ItemInit.testExplosion.getRegistryName(), "inventory"));
    }

}
