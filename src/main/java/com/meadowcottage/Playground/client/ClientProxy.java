package com.meadowcottage.Playground.client;

import com.meadowcottage.Playground.common.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy
{
    public void preinit()
    {

    }

    public void init()
    {

    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld()
    {
        return Minecraft.getMinecraft().theWorld;
    }
}
