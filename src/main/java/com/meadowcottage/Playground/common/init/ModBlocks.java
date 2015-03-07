package com.meadowcottage.Playground.common.init;

import com.meadowcottage.Playground.common.blocks.TestBlock;
import com.meadowcottage.Playground.common.reference.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static final TestBlock TestBlock = new TestBlock();

    public static void init()
    {
        GameRegistry.registerBlock(TestBlock, Names.Blocks.TestBlock);
    }
}
