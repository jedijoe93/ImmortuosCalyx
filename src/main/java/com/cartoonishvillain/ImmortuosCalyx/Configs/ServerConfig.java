package com.cartoonishvillain.ImmortuosCalyx.Configs;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class ServerConfig {
    public static final String SCATEGORY_SYMPTOMS = "symptoms";
    public static final String SCATEGORY_CONTAGION = "contagionMechanics";
    public static final String SCATEGORY_EFFECTS = "effectPercentages";
    public static final String SCATEGORY_OTHERS = "others";
    public static final String SCATEGORY_VILLAGERINF = "villagerInfections";

    public ConfigHelper.ConfigValueListener<Boolean> ANTICHAT;
    public ConfigHelper.ConfigValueListener<Boolean> INFECTEDCHATNOISE;
    public ConfigHelper.ConfigValueListener<Boolean> PVPCONTAGION;
    public ConfigHelper.ConfigValueListener<Boolean> HEATSLOW;
    public ConfigHelper.ConfigValueListener<Boolean> COLDFAST;
    public ConfigHelper.ConfigValueListener<Boolean> WARMWEAKNESS;
    public ConfigHelper.ConfigValueListener<Boolean> COLDSTRENGTH;
    public ConfigHelper.ConfigValueListener<Boolean> BLINDNESS;

    public ConfigHelper.ConfigValueListener<Double> ARMORRESISTMULTIPLIER;
    public ConfigHelper.ConfigValueListener<Double> RESISTGIVENAP;
    public ConfigHelper.ConfigValueListener<Integer> INFECTEDENTITYINFECTIONVALUE;
    public ConfigHelper.ConfigValueListener<Integer> ZOMBIEINFECTIONVALUE;
    public ConfigHelper.ConfigValueListener<Integer> RAWFOODINFECTIONVALUE;
    public ConfigHelper.ConfigValueListener<Integer> INFECTEDAERIALRATE;
    public ConfigHelper.ConfigValueListener<Integer> ZOMBIEAERIALRATE;
    public ConfigHelper.ConfigValueListener<Integer> COMMONAERIALRATE;


    public ConfigHelper.ConfigValueListener<Integer> EFFECTMESSAGEONE;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTMESSAGETWO;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTCHAT;
    public ConfigHelper.ConfigValueListener<Integer> PLAYERINFECTIONTHRESHOLD;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTSPEED;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTSTRENGTH;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTBLIND;
    public ConfigHelper.ConfigValueListener<Integer> EFFECTDAMAGE;


    public ConfigHelper.ConfigValueListener<Integer> EGGINFECTIONSTART;
    public ConfigHelper.ConfigValueListener<Integer> INFECTIONDAMAGE;
    public ConfigHelper.ConfigValueListener<Integer> PVPCONTAGIONRELIEF;
    public ConfigHelper.ConfigValueListener<Integer> PVPCONTAGIONAMOUNT;
    public ConfigHelper.ConfigValueListener<Integer> INFECTIONTIMER;
    public ConfigHelper.ConfigValueListener<Boolean> FORMATTEDINFECTCHAT;

    public ConfigHelper.ConfigValueListener<Integer> VILLAGERINFECTIONTIMER;
    public ConfigHelper.ConfigValueListener<Integer> VILLAGERSLOWONE;
    public ConfigHelper.ConfigValueListener<Integer> VILLAGERSLOWTWO;
    public ConfigHelper.ConfigValueListener<Integer> VILLAGERNOTRADE;
    public ConfigHelper.ConfigValueListener<Integer> VILLAGERLETHAL;
    public ConfigHelper.ConfigValueListener<Integer> IRONGOLEMTIMER;
    public ConfigHelper.ConfigValueListener<Integer> IRONGOLEMSLOW;
    public ConfigHelper.ConfigValueListener<Integer> IRONGOLEMWEAK;
    public ConfigHelper.ConfigValueListener<Integer> IRONGOLEMLETHAL;




    public ServerConfig(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber){
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

        builder.comment("Modify Contagion Components - you're given as much freedom as I can give, but some things absolutely break the code. Keep decimals where there are decimals, use no decimals where there aren't decimals.").push(SCATEGORY_CONTAGION);
        this.ARMORRESISTMULTIPLIER = subscriber.subscribe(builder.comment("Changes the multiplier that controls how much defense to the infection armor gives").defineInRange("armorInfectResist", 2d, 0d, 100d));
        this.RESISTGIVENAP = subscriber.subscribe(builder.comment("Effects how much resistance general antiparasitic gives the player when used").defineInRange("playerResistanceGiven", 6d, 1d, 100d));
        this.INFECTEDENTITYINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by fully converted entities.").define("infectedEntityInfection", 95));
        this.ZOMBIEINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by ZombieEntity and it's derivatives").define("zombieEntityInfection", 20));
        this.RAWFOODINFECTIONVALUE = subscriber.subscribe(builder.comment("Changes the base infection chance provided by eating vanilla raw food").define("rawFoodInfection", 10));
        this.INFECTEDAERIALRATE = subscriber.subscribe(builder.comment("Higher numbers reduces aerosol infection attempts from custom infected entities").define("infectedAerosol", 5000));
        this.ZOMBIEAERIALRATE = subscriber.subscribe(builder.comment("Higher numbers reduces aerosol infection attempts from zombies, and zombie-like mobs").define("zombieAerosol", 7500));
        this.COMMONAERIALRATE = subscriber.subscribe(builder.comment("Higher numbers reduces aerosol infection attempts from any entity with partial infections.").define("commonAerosol", 10000));
        builder.pop();

        builder.comment("Modify Infection Side effects - when do side effects occur when enabled?").push(SCATEGORY_EFFECTS);
        this.EFFECTMESSAGEONE = subscriber.subscribe(builder.comment("Changes when the first warning message for the infection will send").defineInRange("effectMessageOneTime", 10, 0, Integer.MAX_VALUE));
        this.EFFECTMESSAGETWO = subscriber.subscribe(builder.comment("Changes when the second warning message for the infection will send").defineInRange("effectMessageTwoTime", 25, 0, Integer.MAX_VALUE));
        this.EFFECTCHAT = subscriber.subscribe(builder.comment("Changes when the chat blocking side effect occurs").defineInRange("effectChatTime", 40, 0, Integer.MAX_VALUE));
        this.PLAYERINFECTIONTHRESHOLD = subscriber.subscribe(builder.comment("Changes where players can start infecting each other in infection percentage").defineInRange("effectContagionTime", 50, 0, Integer.MAX_VALUE));
        this.EFFECTSPEED = subscriber.subscribe(builder.comment("Changes when the speed/slowdown side effects occurs").defineInRange("effectSpeedTime", 60, 0, Integer.MAX_VALUE));
        this.EFFECTSTRENGTH = subscriber.subscribe(builder.comment("Changes then the strength/weakness side effects occurs").defineInRange("effectStrengthTime", 85, 0, Integer.MAX_VALUE));
        this.EFFECTBLIND = subscriber.subscribe(builder.comment("Changes then the blindness side effects occur").defineInRange("effectBlindnessTime", 95, 0, Integer.MAX_VALUE));
        this.EFFECTDAMAGE = subscriber.subscribe(builder.comment("Changes when players will be attacked by the parasite").defineInRange("effectAttackTime", 100, 0, Integer.MAX_VALUE));
        builder.pop();

        builder.comment("Modify other properties of the mod, for the more wacky fun times.").push(SCATEGORY_OTHERS);
        this.EGGINFECTIONSTART = subscriber.subscribe(builder.comment("Changes how much infection is given to an entity when injected with the Immortuos eggs").define("infectAmountEgg", 1));
        this.INFECTIONDAMAGE = subscriber.subscribe(builder.comment("Changes how much damage the infection deals to players at 100%").define("infectionDamage", 1));
        this.PVPCONTAGIONRELIEF = subscriber.subscribe(builder.comment("Changes how much infecting other players relieves a player of the infection").define("infectionRelief", 5));
        this.PVPCONTAGIONAMOUNT = subscriber.subscribe(builder.comment("Changes how much you infect a player by infecting them via pvp").define("infectionPVPContagion", 1));
        this.INFECTIONTIMER = subscriber.subscribe(builder.comment("Changes how long it takes for the infection to go up 1% in ticks (assuming no lag, 20 per second)").define("infectionTicker", 450));
        this.FORMATTEDINFECTCHAT = subscriber.subscribe(builder.comment("Enables formatted chat for infected users. A middle ground for antichat where infected user's chat is obfuscated instead of outright removed. When false, chat is outright removed.").define("ObfuscateInfChat", false));
        builder.pop();

        builder.comment("Modify the infections of Villagers").push(SCATEGORY_VILLAGERINF);
        this.VILLAGERINFECTIONTIMER = subscriber.subscribe(builder.comment("Changes how long it takes for the infection to go up 1% in ticks (assuming no lag, 20 per second)").define("villagerInfectionTicker", 450));
        this.VILLAGERSLOWONE = subscriber.subscribe(builder.comment("How much infection a villager needs for slowness 1").define("villagerSlowOne", 5));
        this.VILLAGERSLOWTWO = subscriber.subscribe(builder.comment("How much infection a villager needs for slowness 2").define("villagerSlowTwo", 25));
        this.VILLAGERNOTRADE = subscriber.subscribe(builder.comment("How much infection a villager needs to deny trading").define("villagerDenyTrade", 37));
        this.VILLAGERLETHAL = subscriber.subscribe(builder.comment("How much infection a villager needs to be lethal to the villager").define("villagerDeath", 60));
        this.IRONGOLEMTIMER = subscriber.subscribe(builder.comment("How many ticks it takes an iron golem to go up 1% infection").define("igInfectionTicker", 450));
        this.IRONGOLEMSLOW = subscriber.subscribe(builder.comment("How much infection an iron golem needs to be slowed down").define("igSlow", 30));
        this.IRONGOLEMWEAK = subscriber.subscribe(builder.comment("How much infection an iron golem needs to be weakened").define("igWeak", 65));
        this.IRONGOLEMLETHAL = subscriber.subscribe(builder.comment("How much infection an iron golem needs to have the infection be lethal").define("igDeath", 110));
        builder.pop();

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
