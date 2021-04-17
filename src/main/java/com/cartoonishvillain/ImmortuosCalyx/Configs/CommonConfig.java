package com.cartoonishvillain.ImmortuosCalyx.Configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonConfig {
     public static final String CATEGORY_NUMBERS = "Spawn Weights";
     public ConfigHelper.ConfigValueListener<Integer> IG;
     public ConfigHelper.ConfigValueListener<Integer> VILLAGER;
     public ConfigHelper.ConfigValueListener<Integer> HUMAN;
     public ConfigHelper.ConfigValueListener<Integer> DIVER;

     public CommonConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
         builder.comment("Modify chances of natural Heavily Infected Spawns. Higher values means higher spawn rates").push(CATEGORY_NUMBERS);
         this.IG = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Iron Golems").defineInRange("infectedIGSpawn", 1, 0, 1000));
         this.VILLAGER = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Villagers").defineInRange("infectedVillagerSpawn", 2, 0, 1000));
         this.DIVER = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Divers").defineInRange("infectedDiverSpawn", 5, 0, 1000));
         this.HUMAN = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Humans").defineInRange("infectedHumanSpawn", 5, 0, 1000));
     }

}
