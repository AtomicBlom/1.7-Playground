package com.meadowcottage.Playground.common;

import com.meadowcottage.Playground.common.init.ModTools;
import com.meadowcottage.Playground.common.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class PlaygroundTab
{
    public static final CreativeTabs PlaygroundTab = new CreativeTabs(Reference.MODID.toLowerCase())
    {
        @Override
        public Item getTabIconItem()
        {
            return Items.apple;
        }
    };
}
