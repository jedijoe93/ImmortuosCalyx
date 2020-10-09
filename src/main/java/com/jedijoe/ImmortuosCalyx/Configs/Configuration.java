package com.jedijoe.ImmortuosCalyx.Configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Configuration {
    public static final String SCATEGORY_SYMPTOMS = "symptoms";
    public static final String SCATEGORY_CONTAGION = "contagionMechanics";
    public static final String SCATEGORY_OTHERS = "others";

    public ConfigHelper.ConfigValueListener<Boolean> ANTICHAT;
    public ConfigHelper.ConfigValueListener<Boolean> INFECTEDCHATNOISE;
    public ConfigHelper.ConfigValueListener<Boolean> PVPCONTAGION;
    public ConfigHelper.ConfigValueListener<Boolean> HEATSLOW;
    public ConfigHelper.ConfigValueListener<Boolean> COLDFAST;
    public ConfigHelper.ConfigValueListener<Boolean> WARMWEAKNESS;
    public ConfigHelper.ConfigValueListener<Boolean> COLDSTRENGTH;
    public ConfigHelper.ConfigValueListener<Boolean> BLINDNESS;

    public ConfigHelper.ConfigValueListener<Float> ARMORRESISTMULTIPLIER;
    public ConfigHelper.ConfigValueListener<Float> RESISTGIVENAP;
    public ConfigHelper.ConfigValueListener<Integer> PLAYERINFECTIONTHRESHOLD;
    public ConfigHelper.ConfigValueListener<Integer> INFECTEDENTITYINFECTIONVALUE;
    public ConfigHelper.ConfigValueListener<Integer> ZOMBIEINFECTIONVALUE;
    public ConfigHelper.ConfigValueListener<Integer> RAWFOODINFECTIONVALUE;

    public ConfigHelper.ConfigValueListener<Integer> EGGINFECTIONSTART;
    public ConfigHelper.ConfigValueListener<Integer> INFECTIONDAMAGE;
    public ConfigHelper.ConfigValueListener<Integer> PVPCONTAGIONRELIEF;
    public ConfigHelper.ConfigValueListener<Integer> PVPCONTAGIONAMOUNT;

    public Configuration(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
        builder.comment("Modify Infection Components").push(SCATEGORY_SYMPTOMS);
        this.ANTICHAT = subscriber.subscribe(builder.comment("Enables or disables the blocking of an infected individual's chat messages at 40% infection.").define("enableInfectedChatBlock", true));
        this.INFECTEDCHATNOISE = subscriber.subscribe(builder.comment("Enables or disables the noises coming from players when trying to chat at 40% infection.").define("enableInfectedChatNoise", true));
        this.PVPCONTAGION = subscriber.subscribe(builder.comment("Enables or disables contracting the virus from being hit by players with a high enough infection.").define("pvpContagion", true));
        this.HEATSLOW = subscriber.subscribe(builder.comment("Enables or disables the slowing down of players at 60% in warm environments").define("heatSlow", true));
        this.COLDFAST = subscriber.subscribe(builder.comment("Enables or disables the speeding up of players at 60% in cold environments").define("coldFast", true));
        this.WARMWEAKNESS = subscriber.subscribe(builder.comment("Enables or disables the weakening of players at 85% outside of cold environments").define("warmWeakness", true));
        this.COLDSTRENGTH = subscriber.subscribe(builder.comment("Enables or disables the stregthening of players at 85% in cold environments").define("coldStrength", true));
        this.BLINDNESS = subscriber.subscribe(builder.comment("Enables or disables the blindness of players at 95% infection").define("theColdDarkAbyss", true));
        builder.pop();

        builder.comment("Modify Contagion Components - you're given freedom, but nonsensical stats will break things.").push(SCATEGORY_CONTAGION);
        this.ARMORRESISTMULTIPLIER = subscriber.subscribe(builder.comment("Changes the multiplier that controls how much defense to the infection armor gives").define("armorInfectResist", 2f));
        this.RESISTGIVENAP = subscriber.subscribe(builder.comment("Effects how much resistance general antiparasitic gives the player when used").define("playerResistanceGiven", 6f));
        this.PLAYERINFECTIONTHRESHOLD = subscriber.subscribe(builder.comment("Changes where players can start infecting each other in infection percentage").define("playerContagionThreshold", 50));
        this.INFECTEDENTITYINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by fully converted entities.").define("infectedEntityInfection", 95));
        this.ZOMBIEINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by ZombieEntity and it's derivatives").define("zombieEntityInfection", 20));
        this.RAWFOODINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by eating vanilla raw food").define("rawFoodInfection", 10));
        builder.pop();

        builder.comment("Modify other properties of the mod, for the more wacky fun times.").push(SCATEGORY_OTHERS);
        this.EGGINFECTIONSTART = subscriber.subscribe(builder.comment("Changes how much infection is given when a player when injected with the Immortuos eggs").define("infectAmountEgg", 1));
        this.INFECTIONDAMAGE = subscriber.subscribe(builder.comment("Changes how much damage the infection deals to players at 100%").define("infectionDamage", 1));
        this.PVPCONTAGIONRELIEF = subscriber.subscribe(builder.comment("Changes how much infecting other players relieves a player of the infection").define("infectionRelief", 5));
        this.PVPCONTAGIONAMOUNT = subscriber.subscribe(builder.comment("Changes how much you infect a player by infecting them via pvp").define("infectionPVPContagion", 1));


    }

    public static void loadConfig(ForgeConfigSpec spec, Path path){
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();;
        spec.setConfig(configData);
    }
}
