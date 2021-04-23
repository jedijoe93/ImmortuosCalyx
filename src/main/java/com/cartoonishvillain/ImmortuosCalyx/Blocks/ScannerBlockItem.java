package com.cartoonishvillain.ImmortuosCalyx.Blocks;

import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ScannerBlockItem extends BlockItem {
    public ScannerBlockItem(Block block) {
        super(block, new Item.Properties().tab(ImmortuosCalyx.TAB));
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> list, ITooltipFlag p_77624_4_) {
        list.add(new StringTextComponent(TextFormatting.BLUE + "Scans for infection when stepped on."));
        list.add(new StringTextComponent(TextFormatting.BLUE + "If an infection is found, sends redstone signal."));
        list.add(new StringTextComponent(TextFormatting.BLUE + "If one is not found, it removes it's signal."));

    }
}
