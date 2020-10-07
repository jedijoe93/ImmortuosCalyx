package com.jedijoe.ImmortuosCalyx;

import com.jedijoe.ImmortuosCalyx.Blocks.InfectionScanner;
import com.jedijoe.ImmortuosCalyx.Blocks.ScannerBlockItem;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.Items.BaseItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ImmortuosCalyx.MOD_ID);

    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());}

    public static final RegistryObject<Block> INFECTIONSCANNER = BLOCKS.register("infection_scanner", InfectionScanner::new);
    public static final RegistryObject<Item> INFECTIONSCANNER_ITEM = BLOCKITEMS.register("infection_scanner", () -> new ScannerBlockItem(INFECTIONSCANNER.get()));

    public static final RegistryObject<Item> CALYXANIDE = ITEMS.register("calyxanide", () -> new BaseItems(TextFormatting.BLUE + "Kills the Immortuos Calyx Parasite", TextFormatting.BLUE + "May need multiple doses for later stage infections", TextFormatting.RED + "May be lethal if the parasite is ingrained too heavily", ""));
    public static final RegistryObject<Item> GENERALANTIPARASITIC = ITEMS.register("antiparasitic", () -> new BaseItems(TextFormatting.BLUE + "Strengthens Immune System to the Immortuos Calyx Parasite", TextFormatting.BLUE + "Does not make you immune. May also kill early forms of infection", TextFormatting.RED + "Will cause light organ damage", ""));
    public static final RegistryObject<Item> IMMORTUOSCALYXEGGS = ITEMS.register("immortuoseggs", () -> new BaseItems(TextFormatting.RED + "Infects humans with the Immortuos Calyx Parasite,", "", "", ""));
    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe", () -> new BaseItems(TextFormatting.GRAY + "Allows you to harvest biomaterials necessary to make medicines", "", "", ""));

    public static final RegistryObject<EntityType<InfectedHumanEntity>> INFECTEDHUMAN = ENTITY_TYPES.register("infectedhuman", () -> EntityType.Builder.create(InfectedHumanEntity::new, EntityClassification.MONSTER).size(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infectedhuman").toString()));
    public static final RegistryObject<EntityType<InfectedDiverEntity>> INFECTEDDIVER = ENTITY_TYPES.register("infecteddiver", () -> EntityType.Builder.create(InfectedDiverEntity::new, EntityClassification.MONSTER).size(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infecteddiver").toString()));

}

