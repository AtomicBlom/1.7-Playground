package com.meadowcottage.Playground.common.blocks;

import com.meadowcottage.Playground.client.render.ConnectedTextureISBRH;
import com.meadowcottage.Playground.client.render.IConnectedTextureBlock;
import com.meadowcottage.Playground.common.reference.Names;
import com.meadowcottage.Playground.common.reference.Textures;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class TestBlock extends BlockPlayground implements IConnectedTextureBlock
{
    private IIcon connectedTextureIcon;

    public TestBlock()
    {
        super();
        this.setBlockName(Names.Blocks.TestBlock);
        this.setBlockTextureName(Textures.Blocks.TestBlock);
    }

    @Override
    public int getRenderType()
    {
        return ConnectedTextureISBRH.INSTANCE.getRenderId();
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        connectedTextureIcon = iconRegister.registerIcon(String.format("%s-ctm", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    @Override
    public IIcon getConnectedTextureIcon(int side, int metadata)
    {
        return connectedTextureIcon;
    }
}
