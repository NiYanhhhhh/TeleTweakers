package com.niyanhhhhh.teletweakers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.niyanhhhhh.teletweakers.common.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = TTMain.MODID, name = TTMain.MODNAME, version = TTMain.MODVERSION, acceptedMinecraftVersions = TTMain.MC_VERSION)
public class TTMain {

    public static final String MODID = "teletweakers";
    public static final String MODNAME = "Teleport Tweakers";
    public static final String MODVERSION = "1.0.0";
    public static final String MC_VERSION = "[1.12.2]";

    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    @SidedProxy(clientSide = "com.niyanhhhhh.teletweakers.client.ClientProxy", serverSide = "package com.niyanhhhhh.teletweakers.common.CommonProxy")
    public static CommonProxy ttProxy;

//    @EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//
//    }
//
//    @EventHandler
//    public void init(FMLInitializationEvent event) {
//
//    }
//
//    @EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//
//    }

}
