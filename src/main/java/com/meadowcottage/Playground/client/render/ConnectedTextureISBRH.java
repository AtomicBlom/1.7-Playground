package com.meadowcottage.Playground.client.render;

import com.meadowcottage.Playground.common.util.textures.BlockSideRotation;
import com.meadowcottage.Playground.common.util.textures.TextureDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.util.vector.Vector2f;
import javax.vecmath.Vector3d;

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
            if (block.shouldSideBeRendered(world, x, y, z, side.ordinal())) {
                renderSide(world, x, y, z, side, ctBlock, renderer);
            }
        }

        return true;
    }

    private void renderSide(IBlockAccess world, int x, int y, int z, ForgeDirection side, IConnectedTextureBlock block, RenderBlocks renderer)
    {
        //if (side != ForgeDirection.SOUTH && side != ForgeDirection.WEST) return;
        //if (side != ForgeDirection.EAST && side != ForgeDirection.NORTH) return;
        if (side == ForgeDirection.UP || side == ForgeDirection.DOWN) return;

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

    private static class TextureQuadrant {
        final ForgeDirection side;
        final ForgeDirection horizontalDirection;
        final ForgeDirection verticalDirection;
        final boolean offsetVerticalTexture;
        final boolean offsetHorizontalTexture;

        final Vec3 pointA;
        final Vec3 pointB;
        final Vec3 pointC;
        final Vec3 pointD;

        public TextureQuadrant(TextureDirection horizontal, TextureDirection vertical, ForgeDirection side)
        {
            this.side = side;
            this.horizontalDirection = BlockSideRotation.forOrientation(horizontal, side);
            this.verticalDirection = BlockSideRotation.forOrientation(vertical, side);

            offsetHorizontalTexture = horizontal == TextureDirection.LEFT;
            offsetVerticalTexture = vertical == TextureDirection.ABOVE;

            float tuStart = -0.5f;
            float tuEnd = 0.0f;
            float tvStart = -0.5f;
            float tvEnd = 0.0f;

            if (offsetHorizontalTexture) {
                tuStart += 0.5;
                tuEnd += 0.5;
            }
            if (offsetVerticalTexture) {
                tvStart += 0.5;
                tvEnd += 0.5;
            }

            Vec3 pointA, pointB, pointC, pointD;

            float angle = 0;

            switch(side) {
                case SOUTH:
                    angle = 0;
                    break;
                case WEST:
                    angle = (float)Math.toRadians(270);
                    break;
                case EAST:
                    angle = (float)Math.toRadians(90);
                    break;
                case NORTH:
                    angle = (float)Math.toRadians(180);
                    break;
            }

            pointA = Vec3.createVectorHelper(tuStart, tvStart, -0.5);
            pointB = Vec3.createVectorHelper(tuStart, tvEnd, -0.5);
            pointC = Vec3.createVectorHelper(tuEnd, tvEnd, -0.5);
            pointD = Vec3.createVectorHelper(tuEnd, tvStart, -0.5);

            pointA.rotateAroundY(angle);
            pointB.rotateAroundY(angle);
            pointC.rotateAroundY(angle);
            pointD.rotateAroundY(angle);


            this.pointA = pointA.addVector(0.5, 0.5, 0.5);
            this.pointB = pointB.addVector(0.5, 0.5, 0.5);
            this.pointC = pointC.addVector(0.5, 0.5, 0.5);
            this.pointD = pointD.addVector(0.5, 0.5, 0.5);
        }
    }

    private TextureCoordinates getSubTextureForBlock(IBlockAccess world, int x, int y, int z, IConnectedTextureBlock block, TextureQuadrant quadrant)
    {
        TextureCoordinates  coordinates = new TextureCoordinates();
        int metadata = world.getBlockMetadata(x, y, z);
        boolean horizontalConnected = block == world.getBlock(
                x + quadrant.horizontalDirection.offsetX,
                y + quadrant.horizontalDirection.offsetY,
                z + quadrant.horizontalDirection.offsetZ);
        boolean verticalConnected = block == world.getBlock(
                x + quadrant.verticalDirection.offsetX,
                y + quadrant.verticalDirection.offsetY,
                z + quadrant.verticalDirection.offsetZ);
        boolean cornerConnected = block == world.getBlock(
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
