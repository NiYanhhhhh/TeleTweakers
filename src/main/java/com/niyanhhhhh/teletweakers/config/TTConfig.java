package com.niyanhhhhh.teletweakers.config;

import com.niyanhhhhh.teletweakers.TTMain;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;

@Config(modid = TTMain.MODID)
@LangKey("config.teletweakers.general")
public class TTConfig {
    
    @Comment("when it's set to be true, the nether portal won't spawn through igniting.(default: true)")
    @LangKey("config.teletweakers.general.cancelportal")
    public static boolean portalTweaks = true;

}
