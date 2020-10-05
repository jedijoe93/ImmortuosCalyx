package com.jedijoe.ImmortuosCalyx;

import com.jedijoe.ImmortuosCalyx.Blocks.InfectionScanner;
import com.jedijoe.ImmortuosCalyx.Blocks.ScannerBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);

    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());}

    public static final RegistryObject<Block> INFECTIONSCANNER = BLOCKS.register("infection_scanner", InfectionScanner::new);
    public static final RegistryObject<Item> INFECTIONSCANNER_ITEM = BLOCKITEMS.register("infection_scanner", () -> new ScannerBlockItem(INFECTIONSCANNER.get()));
}
