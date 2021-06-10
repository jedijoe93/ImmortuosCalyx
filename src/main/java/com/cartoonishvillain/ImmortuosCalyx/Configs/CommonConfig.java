package com.cartoonishvillain.ImmortuosCalyx.Configs;

import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class CommonConfig {
    public static final String CATEGORY_NUMBERS = "Spawn Weights";
    public static final String SCATEGORY_CLEANSED = "cleansedDimensions";

    public ConfigHelper.ConfigValueListener<Integer> IG;
    public ConfigHelper.ConfigValueListener<Integer> VILLAGER;
    public ConfigHelper.ConfigValueListener<Integer> HUMAN;
    public ConfigHelper.ConfigValueListener<Integer> DIVER;
    public ConfigHelper.ConfigValueListener<String> DIMENSIONALCLEANSE;
    public ConfigHelper.ConfigValueListener<Boolean> HOSTILEINFECTIONINCLEANSE;
    public ConfigHelper.ConfigValueListener<Boolean> HOSTILEAEROSOLINFECTIONINCLEANSE;
    public ConfigHelper.ConfigValueListener<Boolean> PLAYERINFECTIONINCLEANSE;
    public ConfigHelper.ConfigValueListener<Boolean> RAWFOODINFECTIONINCLEANSE;

    public CommonConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
        builder.comment("Modify chances of natural Heavily Infected Spawns. Higher values means higher spawn rates").push(CATEGORY_NUMBERS);
        this.IG = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Iron Golems").defineInRange("infectedIGSpawn", 1, 0, 1000));
        this.VILLAGER = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Villagers").defineInRange("infectedVillagerSpawn", 2, 0, 1000));
        this.DIVER = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Divers").defineInRange("infectedDiverSpawn", 5, 0, 1000));
        this.HUMAN = subscriber.subscribe(builder.comment("Changes natural spawn weight of infected Humans").defineInRange("infectedHumanSpawn", 5, 0, 1000));
        builder.pop();

        builder.comment("Modify game mechanics based on dimensions").push(SCATEGORY_CLEANSED);
        this.DIMENSIONALCLEANSE = subscriber.subscribe(builder.comment("EXPERIMENTAL! MUST BE ALL CHARACTERS FROM [a-z0-9/._-] OR THE GAME WILL CRASH. List the dimension names that you want the following configs to interact with. (e.g. the_bumblezone:the_bumblezone,minecraft:overworld").define("infectionDimensionCleanse", "notadimension"));
        this.HOSTILEAEROSOLINFECTIONINCLEANSE = subscriber.subscribe(builder.comment("Disables hostile mob aerosol infections in cleansed dimensions").define("hostileAerosolInCleanse", true));
        this.HOSTILEINFECTIONINCLEANSE = subscriber.subscribe(builder.comment("Disables hostile mob attack based infections in cleansed dimensions").define("hostileInfectInCleanse", true));
        this.PLAYERINFECTIONINCLEANSE = subscriber.subscribe(builder.comment("Disables player attack based infections in cleansed dimensions").define("playerInfectInCleanse", false));
        this.RAWFOODINFECTIONINCLEANSE = subscriber.subscribe(builder.comment("Disables raw food infections in cleansed dimensions").define("rawFoodInfectedInCleanse", true));

    }

    public static ArrayList<ResourceLocation> getDimensions() {
        final String DimensionList = ImmortuosCalyx.commonConfig.DIMENSIONALCLEANSE.get();
        String[] DimensionExclusion = DimensionList.split(",");
        int exclusionLength = DimensionExclusion.length;
        ArrayList<ResourceLocation> finalDimensionExclusion = new ArrayList<>();
        int counter = 0;
        for (String i : DimensionExclusion) {
            ResourceLocation newResource = new ResourceLocation(i);
            finalDimensionExclusion.add(newResource);
            counter++;
        }
        return finalDimensionExclusion;
    }

}
