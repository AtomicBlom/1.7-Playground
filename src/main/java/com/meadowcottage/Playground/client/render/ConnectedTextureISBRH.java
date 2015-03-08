package com.meadowcottage.Playground.client.render;

import com.meadowcottage.Playground.common.util.textures.BlockSideRotation;
import com.meadowcottage.Playground.common.util.textures.TextureDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ConnectedTextureISBRH implements ISimpleBlockRenderingHandler
{
    private final int id;

    private ConnectedTextureISBRH() {
        this.id = RenderingRegistry.getNextAvailableRenderId();
    }

    public static final ConnectedTextureISBRH INSTANCE = new ConnectedTextureISBRH();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (!(block instanceof IConnectedTextureBlock)) {
            return false;
        }

        IConnectedTextureBlock ctBlock = (IConnectedTextureBlock)block;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (block.shouldSideBeRendered(world, x, y, z, side.ordinal())) {
                renderSide(world, x, y, z, side, ctBlock, renderer);
            }
        }

        return false;
    }

    private void renderSide(IBlockAccess world, int x, int y, int z, ForgeDirection side, IConnectedTextureBlock block, RenderBlocks renderer)
    {
        final ForgeDirection belowDirection = BlockSideRotation.forOrientation(TextureDirection.BELOW, side);
        ForgeDirection[] verticalDirections = {
                BlockSideRotation.forOrientation(TextureDirection.ABOVE, side),
                belowDirection
        };
        final ForgeDirection rightDirection = BlockSideRotation.forOrientation(TextureDirection.RIGHT, side);
        ForgeDirection[] horizontalDirections = {
                BlockSideRotation.forOrientation(TextureDirection.LEFT, side),
                rightDirection
        };

        TextureCoordinates coordinates;
        for (ForgeDirection horizontalDirection : horizontalDirections) {
            for (ForgeDirection verticalDirection : verticalDirections) {
                coordinates = getSubTextureForBlock(world, x, y, z, side.ordinal(), block,
                        horizontalDirection, verticalDirection,
                        horizontalDirection == rightDirection,
                        verticalDirection == belowDirection

                );
            }
        }
    }

    private TextureCoordinates getSubTextureForBlock(IBlockAccess world, int x, int y, int z, int side, IConnectedTextureBlock block,
                                                     ForgeDirection horizontalDirection, ForgeDirection verticalDirection,
                                                     boolean offsetHorizontalTexture, boolean offsetVerticalTexture)
    {
        TextureCoordinates coordinates = new TextureCoordinates();
        int metadata = world.getBlockMetadata(x, y, z);
        boolean horizontalConnected = block == world.getBlock(
                x + horizontalDirection.offsetX,
                y + horizontalDirection.offsetY,
                z + horizontalDirection.offsetZ);
        boolean verticalConnected = block == world.getBlock(
                x + verticalDirection.offsetX,
                y + verticalDirection.offsetY,
                z + verticalDirection.offsetZ);
        boolean cornerConnected = block == world.getBlock(
                x + horizontalDirection.offsetX + verticalDirection.offsetX,
                y + horizontalDirection.offsetY + verticalDirection.offsetY,
                z + horizontalDirection.offsetZ + verticalDirection.offsetZ);

        if (!horizontalConnected && !verticalConnected && !cornerConnected) {
            final IIcon icon = block.getIcon(side, metadata);
            coordinates.tuFrom = icon.getMinU();
            coordinates.tvFrom = icon.getMinV();

            coordinates.tuTo = icon.getMaxU();
            coordinates.tvTo = icon.getMaxV();
        } else
        {
            final IIcon icon = block.getConnectedTextureIcon(side, metadata);

            float minU = icon.getMinU();
            float minV = icon.getMinV();
            float maxU = icon.getMaxU();
            float maxV = icon.getMaxV();

            int uStart = 0;
            int vStart = 0;

            if (!verticalConnected && horizontalConnected)
            {
                vStart |= 2;
            } else if (verticalConnected && !horizontalConnected)
            {
                uStart |= 2;
            } else if (!verticalConnected && !horizontalConnected && cornerConnected)
            {
                uStart |= 2;
                vStart |= 2;
            }

            if (offsetHorizontalTexture)
            {
                uStart |= 1;
            }
            if (offsetVerticalTexture)
            {
                vStart |= 1;
            }

            final float textureSizeU = (maxU - minU) / 4;
            final float textureSizeV = (maxV - minV) / 4;
            coordinates.tuFrom = textureSizeU * (1.0f / uStart);
            coordinates.tvFrom = textureSizeV * (1.0f / vStart);

            coordinates.tuTo = coordinates.tuFrom + textureSizeU;
            coordinates.tvTo = coordinates.tvFrom + textureSizeV;
        }
        return coordinates;
    }

    private static class TextureCoordinates {
        float tuFrom;
        float tvFrom;
        float tuTo;
        float tvTo;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }
}
