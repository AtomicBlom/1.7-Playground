package com.meadowcottage.Playground.common;

import com.meadowcottage.Playground.common.init.ModBlocks;
import com.meadowcottage.Playground.common.init.ModItems;
import com.meadowcottage.Playground.common.init.ModTools;
import com.meadowcottage.Playground.common.proxy.CommonProxy;
import com.meadowcottage.Playground.common.reference.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Playground
{
    @SidedProxy(clientSide = Reference.ClientProxy, serverSide = Reference.CommonProxy)
    public static CommonProxy proxy;

    @Mod.Instance(Reference.MODID)
    public static Playground instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preinit();

        ModBlocks.init();
        ModItems.init();
        ModTools.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
}
