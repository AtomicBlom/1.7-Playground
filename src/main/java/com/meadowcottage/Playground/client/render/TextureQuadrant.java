package com.meadowcottage.Playground.client.render;

import com.meadowcottage.Playground.common.util.textures.BlockSideRotation;
import com.meadowcottage.Playground.common.util.textures.TextureDirection;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

class TextureQuadrant
{
    final ForgeDirection side;
    final ForgeDirection horizontalDirection;
    final ForgeDirection verticalDirection;
    final boolean offsetVerticalTexture;
    final boolean offsetHorizontalTexture;

    final Vec3 pointA;
    final Vec3 pointB;
    final Vec3 pointC;
    final Vec3 pointD;

    private static final float xRotations[] = { (float)Math.toRadians(270), (float)Math.toRadians(90), 0, 0, 0, 0, 0 };
    private static final float yRotations[] = { 0, (float)Math.toRadians(180), (float)Math.toRadians(180), 0, (float)Math.toRadians(270), (float)Math.toRadians(90), 0};

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

        float rotateY = yRotations[side.ordinal()];
        float rotateX = xRotations[side.ordinal()];

        pointA = Vec3.createVectorHelper(tuStart, tvStart, -0.5);
        pointB = Vec3.createVectorHelper(tuStart, tvEnd, -0.5);
        pointC = Vec3.createVectorHelper(tuEnd, tvEnd, -0.5);
        pointD = Vec3.createVectorHelper(tuEnd, tvStart, -0.5);

        pointA.rotateAroundX(rotateX);
        pointB.rotateAroundX(rotateX);
        pointC.rotateAroundX(rotateX);
        pointD.rotateAroundX(rotateX);

        pointA.rotateAroundY(rotateY);
        pointB.rotateAroundY(rotateY);
        pointC.rotateAroundY(rotateY);
        pointD.rotateAroundY(rotateY);




        this.pointA = pointA.addVector(0.5, 0.5, 0.5);
        this.pointB = pointB.addVector(0.5, 0.5, 0.5);
        this.pointC = pointC.addVector(0.5, 0.5, 0.5);
        this.pointD = pointD.addVector(0.5, 0.5, 0.5);
    }
}
