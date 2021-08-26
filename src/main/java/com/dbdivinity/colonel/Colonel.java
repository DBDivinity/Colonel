package com.dbdivinity.colonel;

import com.dbdivinity.colonel.command.ForgeCommand;
import com.dbdivinity.colonel.util.ModInfo;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

/**
 * Colonel is a backport of Brigadier for Minecraft 1.7.10
 * built using Forge.
 */
@Mod(modid = ModInfo.id, version = ModInfo.version, acceptableRemoteVersions = "*")
public class Colonel {
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ForgeCommand());
    }
}
