package com.meadowcottage.Playground.client.render;

import net.minecraft.util.IIcon;

public interface IConnectedTextureBlock
{
    IIcon getConnectedTextureIcon(int side, int metadata);
    IIcon getIcon(int side, int metadata);
}
