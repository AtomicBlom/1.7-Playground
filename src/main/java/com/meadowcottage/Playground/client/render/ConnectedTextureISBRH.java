package com.meadowcottage.Playground.client.render;

import com.meadowcottage.Playground.common.util.textures.TextureDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

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
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (!(block instanceof IConnectedTextureBlock)) {
            return false;
        }

        Tessellator tessellator = Tessellator.instance;
        int colourMultiplier = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float red = (float)(colourMultiplier >> 16 & 255) / 255.0F;
        float green = (float)(colourMultiplier >> 8 & 255) / 255.0F;
        float blue = (float)(colourMultiplier & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float adjustedRed = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float adjustedGreen = (red * 30.0F + green * 70.0F) / 100.0F;
            float adjustedBlue = (red * 30.0F + blue * 70.0F) / 100.0F;
            red = adjustedRed;
            green = adjustedGreen;
            blue = adjustedBlue;
        }

        tessellator.setColorOpaque_F(red, green, blue);

        IConnectedTextureBlock ctBlock = (IConnectedTextureBlock)block;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            //world will be null when rendering inventory
            if (world == null || block.shouldSideBeRendered(world, x, y, z, side.ordinal())) {
                renderSide(world, x, y, z, side, ctBlock);
            }
        }

        return true;
    }

    private void renderSide(IBlockAccess world, int x, int y, int z, ForgeDirection side, IConnectedTextureBlock block)
    {
        Tessellator tessellator = Tessellator.instance;

        TextureQuadrant[] quadrants = {
                new TextureQuadrant(TextureDirection.LEFT, TextureDirection.ABOVE, side),
                new TextureQuadrant(TextureDirection.RIGHT, TextureDirection.ABOVE, side),
                new TextureQuadrant(TextureDirection.LEFT, TextureDirection.BELOW, side),
                new TextureQuadrant(TextureDirection.RIGHT, TextureDirection.BELOW, side),
        };

        TextureCoordinates coordinates;
        for (final TextureQuadrant quadrant : quadrants)
        {
            coordinates = getSubTextureForBlock(world, x, y, z, block, quadrant);

            tessellator.addVertexWithUV(x + quadrant.pointA.xCoord, y + quadrant.pointA.yCoord, z + quadrant.pointA.zCoord, coordinates.tuFrom, coordinates.tvFrom);
            tessellator.addVertexWithUV(x + quadrant.pointB.xCoord, y + quadrant.pointB.yCoord, z + quadrant.pointB.zCoord, coordinates.tuFrom, coordinates.tvTo);
            tessellator.addVertexWithUV(x + quadrant.pointC.xCoord, y + quadrant.pointC.yCoord, z + quadrant.pointC.zCoord, coordinates.tuTo, coordinates.tvTo);
            tessellator.addVertexWithUV(x + quadrant.pointD.xCoord, y + quadrant.pointD.yCoord, z + quadrant.pointD.zCoord, coordinates.tuTo, coordinates.tvFrom);
        }
    }

    private TextureCoordinates getSubTextureForBlock(IBlockAccess world, int x, int y, int z, IConnectedTextureBlock block, TextureQuadrant quadrant)
    {
        TextureCoordinates  coordinates = new TextureCoordinates();
        int metadata = world != null ? world.getBlockMetadata(x, y, z) : 0;
        boolean horizontalConnected = world != null && block == world.getBlock(
                x + quadrant.horizontalDirection.offsetX,
                y + quadrant.horizontalDirection.offsetY,
                z + quadrant.horizontalDirection.offsetZ);
        boolean verticalConnected = world != null &&block == world.getBlock(
                x + quadrant.verticalDirection.offsetX,
                y + quadrant.verticalDirection.offsetY,
                z + quadrant.verticalDirection.offsetZ);
        boolean cornerConnected = world != null &&block == world.getBlock(
                x + quadrant.horizontalDirection.offsetX + quadrant.verticalDirection.offsetX,
                y + quadrant.horizontalDirection.offsetY + quadrant.verticalDirection.offsetY,
                z + quadrant.horizontalDirection.offsetZ + quadrant.verticalDirection.offsetZ);

        int uStart = 0;
        int vStart = 0;
        int subIconsPerIcon;
        IIcon icon;

        //Use the default texture, both borders
        if (!horizontalConnected && !verticalConnected && !cornerConnected) {
            icon = block.getIcon(quadrant.side.ordinal(), metadata);
            subIconsPerIcon = 2;
        } else
        {
            //Use the -CTM.png file
            icon = block.getConnectedTextureIcon(quadrant.side.ordinal(), metadata);
            subIconsPerIcon = 4;

            if (!verticalConnected && horizontalConnected)
            {
                vStart |= 2;
            }
            if (verticalConnected && !horizontalConnected)
            {
                uStart |= 2;
            }
            if (verticalConnected && horizontalConnected && !cornerConnected)
            {
                uStart |= 2;
                vStart |= 2;
            }
        }

        if (quadrant.offsetHorizontalTexture)
        {
            uStart |= 1;
        }
        if (quadrant.offsetVerticalTexture)
        {
            vStart |= 1;
        }

        float minU = icon.getMinU();
        float minV = icon.getMinV();
        float maxU = icon.getMaxU();
        float maxV = icon.getMaxV();

        final float textureSizeU = (maxU - minU);
        final float textureSizeV = (maxV - minV);
        final float localTUFrom = (uStart / (float)subIconsPerIcon);
        final float localTVFrom = (vStart / (float)subIconsPerIcon);
        final float localTUTo = localTUFrom + (1.0f / subIconsPerIcon);
        final float localTVTo = localTVFrom + (1.0f / subIconsPerIcon);

        coordinates.tuFrom = minU + textureSizeU * localTUFrom;
        coordinates.tvFrom = minV + textureSizeV * localTVFrom;
        coordinates.tuTo = minU + textureSizeU * localTUTo;
        coordinates.tvTo = minV + textureSizeV * localTVTo;

        return coordinates;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }
}
