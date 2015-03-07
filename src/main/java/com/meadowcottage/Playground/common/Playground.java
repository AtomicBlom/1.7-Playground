package com.meadowcottage.Playground.common;

import com.meadowcottage.Playground.common.proxy.CommonProxy;
import com.meadowcottage.Playground.common.reference.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Playground
{
    @SidedProxy(clientSide = Reference.ClientProxy, serverSide = Reference.ServerProxy)
    public static CommonProxy proxy;

    @Mod.Instance(Reference.MODID)
    public static Playground instance;
}
