package com.meadowcottage.Playground.common.util.textures;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockSideRotation
{
    private static final ForgeDirection[][] ROTATION_MATRIX = {
            {ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN}
    };

    public static ForgeDirection forOrientation(TextureDirection direction, ForgeDirection orientation)
    {
        return ROTATION_MATRIX[orientation.ordinal()][direction.ordinal()];
    }
}
