package com.cartoonishvillain.ImmortuosCalyx;

import com.cartoonishvillain.ImmortuosCalyx.Blocks.InfectionScanner;
import com.cartoonishvillain.ImmortuosCalyx.Blocks.ScannerBlockItem;
import com.cartoonishvillain.ImmortuosCalyx.Entity.*;
import com.cartoonishvillain.ImmortuosCalyx.Items.BaseItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Register {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, ImmortuosCalyx.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENT = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ImmortuosCalyx.MOD_ID);

    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_EVENT.register(FMLJavaModLoadingContext.get().getModEventBus());}

    public static final RegistryObject<Block> INFECTIONSCANNER = BLOCKS.register("infection_scanner", InfectionScanner::new);
    public static final RegistryObject<Item> INFECTIONSCANNER_ITEM = BLOCKITEMS.register("infection_scanner", () -> new ScannerBlockItem(INFECTIONSCANNER.get()));

    public static final RegistryObject<Item> CALYXANIDE = ITEMS.register("calyxanide", () -> new BaseItems(TextFormatting.BLUE + "Kills the Immortuos Calyx Parasite", TextFormatting.BLUE + "May need multiple doses for later stage infections", TextFormatting.RED + "May be lethal if the parasite is ingrained too heavily", ""));
    public static final RegistryObject<Item> GENERALANTIPARASITIC = ITEMS.register("antiparasitic", () -> new BaseItems(TextFormatting.BLUE + "Strengthens Immune System to the Immortuos Calyx Parasite", TextFormatting.BLUE + "Does not make you immune. May also kill early forms of infection", TextFormatting.RED + "Will cause light organ damage", TextFormatting.GRAY + "Obtained through syringe extraction from a slime"));
    public static final RegistryObject<Item> IMMORTUOSCALYXEGGS = ITEMS.register("immortuoseggs", () -> new BaseItems(TextFormatting.RED + "Infects humans with the Immortuos Calyx Parasite,", TextFormatting.GRAY + "Obtained through syringe extraction from fully converted entities", "", ""));
    public static final RegistryObject<Item> SYRINGE = ITEMS.register("syringe", () -> new BaseItems(TextFormatting.GRAY + "Allows you to harvest biomaterials necessary to make medicines", "", "", ""));
    public static final RegistryObject<Item> SCANNER = ITEMS.register("healthscanner", () -> new BaseItems(TextFormatting.BLUE + "Gives you information about infection", TextFormatting.BLUE + "levels in players, and yourself.",TextFormatting.GRAY + "Shift rightclick to view your stats,", TextFormatting.GRAY + "left click players to view theirs."));

    public static final RegistryObject<EntityType<InfectedHumanEntity>> INFECTEDHUMAN = ENTITY_TYPES.register("infectedhuman", () -> EntityType.Builder.of(InfectedHumanEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infectedhuman").toString()));
    public static final RegistryObject<EntityType<InfectedDiverEntity>> INFECTEDDIVER = ENTITY_TYPES.register("infecteddiver", () -> EntityType.Builder.of(InfectedDiverEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infecteddiver").toString()));
    public static final RegistryObject<EntityType<InfectedVillagerEntity>> INFECTEDVILLAGER = ENTITY_TYPES.register("infectedvillager", () -> EntityType.Builder.of(InfectedVillagerEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infectedvillager").toString()));
    public static final RegistryObject<EntityType<InfectedIGEntity>> INFECTEDIG = ENTITY_TYPES.register("infectedig", ()-> EntityType.Builder.of(InfectedIGEntity::new, EntityClassification.MONSTER).sized(1.6f, 2.6f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infectedig").toString()));
    public static final RegistryObject<EntityType<InfectedPlayerEntity>> INFECTEDPLAYER = ENTITY_TYPES.register("infectedplayer", ()-> EntityType.Builder.of(InfectedPlayerEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f).build(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infectedplayer").toString()));


    public static final RegistryObject<SoundEvent> HUMANAMBIENT = SOUND_EVENT.register("infected_idle", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infected_idle")));
    public static final RegistryObject<SoundEvent> HUMANHURT = SOUND_EVENT.register("infected_hurt", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infected_hurt")));
    public static final RegistryObject<SoundEvent> HUMANDEATH = SOUND_EVENT.register("infected_death", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infected_hurt")));
    public static final RegistryObject<SoundEvent> INJECT = SOUND_EVENT.register("inject", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "inject")));
    public static final RegistryObject<SoundEvent> EXTRACT = SOUND_EVENT.register("extract", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "extract")));
    public static final RegistryObject<SoundEvent> IGHURT = SOUND_EVENT.register("iginfected_hurt", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "iginfected_hurt")));
    public static final RegistryObject<SoundEvent> IGDEATH = SOUND_EVENT.register("iginfected_death", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "iginfected_death")));
    public static final RegistryObject<SoundEvent> VILIDLE = SOUND_EVENT.register("villagerinfected_idle", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "villagerinfected_idle")));
    public static final RegistryObject<SoundEvent> VILHURT = SOUND_EVENT.register("villagerinfected_hurt", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "villagerinfected_hurt")));
    public static final RegistryObject<SoundEvent> VILDEATH = SOUND_EVENT.register("villagerinfected_death", () -> new SoundEvent(new ResourceLocation(ImmortuosCalyx.MOD_ID, "villagerinfected_death")));


}

