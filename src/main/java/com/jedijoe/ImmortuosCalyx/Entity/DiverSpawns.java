package com.jedijoe.ImmortuosCalyx.Entity;

import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import com.jedijoe.ImmortuosCalyx.Register;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DiverSpawns {

    @SubscribeEvent
    public static void stu(BiomeLoadingEvent event){
        if(event.getCategory() == Biome.Category.OCEAN){
        MobSpawnInfo.Spawners spawners = new MobSpawnInfo.Spawners(Register.INFECTEDDIVER.get(), 1,1,1);
        event.getSpawns().withSpawner(EntityClassification.MONSTER, spawners);}
    }
}