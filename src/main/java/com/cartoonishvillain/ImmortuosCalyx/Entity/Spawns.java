package com.cartoonishvillain.ImmortuosCalyx.Entity;

import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import com.cartoonishvillain.ImmortuosCalyx.Register;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;


@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Spawns {

    @SubscribeEvent
    public static void DiverSpawner(BiomeLoadingEvent event){
        if(event.getCategory() == Biome.Category.OCEAN){
        MobSpawnInfo.Spawners spawners = new MobSpawnInfo.Spawners(Register.INFECTEDDIVER.get(), ImmortuosCalyx.commonConfig.DIVER.get(),1,1);
        event.getSpawns().addSpawn(EntityClassification.MONSTER, spawners);}
        else if (event.getCategory() != Biome.Category.THEEND || event.getCategory() != Biome.Category.NETHER){
            ArrayList<MobSpawnInfo.Spawners> spawners = new ArrayList<>();
            spawners.add(new MobSpawnInfo.Spawners(Register.INFECTEDIG.get(), ImmortuosCalyx.commonConfig.IG.get(), 1, 1 ));
            spawners.add(new MobSpawnInfo.Spawners(Register.INFECTEDHUMAN.get(), ImmortuosCalyx.commonConfig.HUMAN.get(), 1, 1 ));
            spawners.add(new MobSpawnInfo.Spawners(Register.INFECTEDVILLAGER.get(), ImmortuosCalyx.commonConfig.VILLAGER.get(), 1, 1 ));
            for(MobSpawnInfo.Spawners spawner : spawners){event.getSpawns().addSpawn(EntityClassification.MONSTER, spawner);}
        }
    }

    public static void PlacementManager(){
        EntitySpawnPlacementRegistry.register(Register.INFECTEDDIVER.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.OCEAN_FLOOR, MobEntity::checkMobSpawnRules);
        EntitySpawnPlacementRegistry.register(Register.INFECTEDIG.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
        EntitySpawnPlacementRegistry.register(Register.INFECTEDHUMAN.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
        EntitySpawnPlacementRegistry.register(Register.INFECTEDVILLAGER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
    }
}