package com.jedijoe.ImmortuosCalyx.Infection;

import com.google.common.util.concurrent.AtomicDouble;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedIGEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedVillagerEntity;
import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import com.jedijoe.ImmortuosCalyx.Register;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerInfectionEventManager {

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof LivingEntity){
            InfectionManager provider = new InfectionManager();
            event.addCapability(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infection"), provider);
        }
    }

    @SubscribeEvent
    public static void InfectionTicker(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START){
        PlayerEntity player = event.player;
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h ->{
            if(h.getInfectionProgress() >= 1){
                h.addInfectionTimer(1);
                int timer = ImmortuosCalyx.config.INFECTIONTIMER.get();
                if(h.getInfectionTimer() >= timer){
                    h.addInfectionProgress(1);
                    h.addInfectionTimer(-timer);
                    EffectController(event.player);
                }
            }
        });}
    }

    @SubscribeEvent public static void InfectionTickEffects(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START){
            PlayerEntity player = event.player;
            player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTSPEED.get()){
                    BlockPos CurrentPosition = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
                    float temperature = player.world.getBiomeManager().getBiome(CurrentPosition).getTemperature(CurrentPosition);
                    if(temperature > 0.9 && ImmortuosCalyx.config.HEATSLOW.get()){player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0, true, false));}
                    else if(temperature < 0.275 && ImmortuosCalyx.config.COLDFAST.get()){player.addPotionEffect(new EffectInstance(Effects.SPEED, 5, 0, true, false));}
                }
                if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTSTRENGTH.get()){
                    BlockPos CurrentPosition = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
                    float temperature = player.world.getBiomeManager().getBiome(CurrentPosition).getTemperature(CurrentPosition);
                    if(temperature > 0.275 && ImmortuosCalyx.config.WARMWEAKNESS.get()){player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5, 0, true, false));}
                    else if (temperature <= 0.275 && ImmortuosCalyx.config.COLDSTRENGTH.get()) {player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 5, 0, true, false));}
                }
                if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTBLIND.get() && ImmortuosCalyx.config.BLINDNESS.get()){
                    player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 50, 1, true, false));
                }
                if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTDAMAGE.get()){
                    Random random = new Random();
                    int randomValue = random.nextInt(100);
                    if(randomValue < 1 && ImmortuosCalyx.config.INFECTIONDAMAGE.get() > 0){
                        player.attackEntityFrom(InfectionDamage.causeInfectionDamage(player), ImmortuosCalyx.config.INFECTIONDAMAGE.get());
                    }
                }
            });

        }
    }

    @SubscribeEvent
    public static void InfectionChat(ServerChatEvent event){
        PlayerEntity player = event.getPlayer();
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            String name = player.getName().getString();
            String format = "<" + name + "> ";
            if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTCHAT.get() && ImmortuosCalyx.config.ANTICHAT.get() && ImmortuosCalyx.config.FORMATTEDINFECTCHAT.get()) {event.setComponent(new StringTextComponent(format +TextFormatting.OBFUSCATED + event.getMessage()));}
            if(h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTCHAT.get() && ImmortuosCalyx.config.ANTICHAT.get() && !ImmortuosCalyx.config.FORMATTEDINFECTCHAT.get()) {event.setCanceled(true);};//if the player's infection is @ or above 40%, they can no longer speak in text chat.
            if((h.getInfectionProgress() >= ImmortuosCalyx.config.EFFECTCHAT.get() && !player.getEntityWorld().isRemote() && ImmortuosCalyx.config.INFECTEDCHATNOISE.get()))player.world.playSound(null, player.getPosition(), Register.HUMANAMBIENT.get(), SoundCategory.PLAYERS, 0.5f, 2f);
        });
    }

    @SubscribeEvent public static void InfectOtherPlayer(AttackEntityEvent event){
        if(event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof PlayerEntity && ImmortuosCalyx.config.PVPCONTAGION.get()){
            PlayerEntity target = (PlayerEntity) event.getTarget(); //the player who got hit
            PlayerEntity aggro = (PlayerEntity) event.getEntity(); //the player that hit
            int protection = target.getTotalArmorValue(); //grabs the armor value of the target
            AtomicInteger infectionRateGrabber = new AtomicInteger(); //funky value time
            aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                int infectionProgression = h.getInfectionProgress();
                infectionRateGrabber.addAndGet(infectionProgression); // sets the atomic int equal to the infection % of the attacker

            });
            AtomicDouble resistRateGrabber = new AtomicDouble(); //grab resistance from
            target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                Double resist = Double.valueOf(h.getResistance());
                resistRateGrabber.addAndGet(resist);
            });
            AtomicBoolean infectedGrabber = new AtomicBoolean(false); //A surprise bool that will help us later
            int convert = infectionRateGrabber.intValue();// converts the atomic int into a normal int for better math
            double resist = (double) resistRateGrabber.get();
            int threshold = ImmortuosCalyx.config.PLAYERINFECTIONTHRESHOLD.get();
            double armorInfectResist = ImmortuosCalyx.config.ARMORRESISTMULTIPLIER.get();
            if(convert >= threshold){ // if the attacker's infection rate is at or above the threshold.
                int conversionThreshold = (int) ((convert - (protection*armorInfectResist))/resist); // creates mimimum score needed to not get infected
                Random rand = new Random();
                if(conversionThreshold > rand.nextInt(100)){ // rolls for infection. If random value rolls below threshold, target is at risk of infection.
                    target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                        int infect = ImmortuosCalyx.config.PVPCONTAGIONAMOUNT.get();
                        if(h.getInfectionProgress() == 0){h.addInfectionProgress(infect); infectedGrabber.set(true);} // if target is not already infected, and the risk for infection exists, they are freshly infected. Surprise bool gets activated
                    });
                }
            }
            if(infectedGrabber.get()){aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{ //if surprise bool is activated, it means the aggressor player successfully infected someone. Removing part of the parasite and having it get on someone else.
                h.addInfectionProgress(-ImmortuosCalyx.config.PVPCONTAGIONRELIEF.get()); });//because of that, symptoms are reduced.
                if(!aggro.getEntityWorld().isRemote)aggro.world.playSound(null, aggro.getPosition(), Register.HUMANHURT.get(), SoundCategory.PLAYERS, 1f, 1.2f);}
        }
    }

    @SubscribeEvent public static void InfectFromMobAttack(net.minecraftforge.event.entity.living.LivingAttackEvent event){
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity aggro = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            int convert = 0;
            int protection = (int) (target.getTotalArmorValue() * ImmortuosCalyx.config.ARMORRESISTMULTIPLIER.get());
            AtomicDouble InfectionResGrabber = new AtomicDouble(1);
            AtomicBoolean InfectedGrabber = new AtomicBoolean(false);
            target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                InfectionResGrabber.set(h.getResistance());
                if (h.getInfectionProgress() > 0) {
                    InfectedGrabber.getAndSet(true);
                }
            });
            double resist = InfectionResGrabber.get();
            if (!InfectedGrabber.get()) { // if target is already infected, it isn't worth running all of this.
                if (aggro instanceof PlayerEntity) {
                } else if (aggro instanceof InfectedHumanEntity || aggro instanceof InfectedDiverEntity) {
                    convert = 95;
                } else if (aggro instanceof InfectedIGEntity) {
                    convert = 75;
                } else if (aggro instanceof InfectedVillagerEntity) {
                    if (target instanceof IronGolemEntity || target instanceof AbstractVillagerEntity) {
                        convert = 100; //villagers have a higher chance of infecting iron golems and other villagers.
                    } else convert = 55;
                } else {
                    AtomicInteger inf = new AtomicInteger(0);
                    aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                        inf.set(h.getInfectionProgress());
                    });
                    convert = inf.get();
                }
                int InfectThreshold = (int) ((convert - protection)/resist);
                Random rand = new Random();
                if(InfectThreshold > rand.nextInt(100)){
                    target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                        h.addInfectionProgress(1);
                    });
                }
            }
        }
    }

    static Item[] rawItem = new Item[]{Items.BEEF, Items.RABBIT, Items.CHICKEN, Items.PORKCHOP, Items.MUTTON, Items.COD, Items.SALMON, Items.ROTTEN_FLESH};
    @SubscribeEvent
    public static void rawFood(LivingEntityUseItemEvent.Finish event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            boolean raw = false;
            for(Item item : rawItem){if(item.equals(event.getItem().getItem())){raw = true; break;}}
            if(raw){
                player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                    if(h.getInfectionProgress() == 0){
                        Random rand = new Random();
                        if(rand.nextInt(100) < (ImmortuosCalyx.config.RAWFOODINFECTIONVALUE.get()/(h.getResistance()))) h.setInfectionProgress(1);
                    }
                });
            }
        }
    }

    private static void EffectController(PlayerEntity inflictedPlayer){
        inflictedPlayer.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            int progressionLogic = h.getInfectionProgress(); //this used to be a switch. I love switches. But switches require constants. These are not constant values anymore. Too bad.
                if(progressionLogic == ImmortuosCalyx.config.EFFECTMESSAGEONE.get()){
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your throat feels sore"), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTMESSAGETWO.get()){
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your mind feels foggy"), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTCHAT.get()){
                    if (ImmortuosCalyx.config.ANTICHAT.get())
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You feel something moving around in your head, you try to yell, but nothing comes out"), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.PLAYERINFECTIONTHRESHOLD.get()){
                    if (ImmortuosCalyx.config.PVPCONTAGION.get())
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "There is something on your skin and you can't get it off.."), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTSPEED.get()){
                    if (ImmortuosCalyx.config.COLDFAST.get() && ImmortuosCalyx.config.HEATSLOW.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You start feeling ill in warm environments, and better in cool ones.."), inflictedPlayer.getUniqueID());
                    else if (ImmortuosCalyx.config.COLDFAST.get() && !ImmortuosCalyx.config.HEATSLOW.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin to feel better in cool environments.."), inflictedPlayer.getUniqueID());
                    else if (ImmortuosCalyx.config.HEATSLOW.get() && !ImmortuosCalyx.config.COLDFAST.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin feeling ill in warm environments..."), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTSTRENGTH.get()){
                    if (ImmortuosCalyx.config.COLDSTRENGTH.get() && ImmortuosCalyx.config.WARMWEAKNESS.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin to feel weak in all but the coldest environments.."), inflictedPlayer.getUniqueID());
                    else if (ImmortuosCalyx.config.COLDSTRENGTH.get() && !ImmortuosCalyx.config.WARMWEAKNESS.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin to feel strong in cold environments.."), inflictedPlayer.getUniqueID());
                    else if (ImmortuosCalyx.config.WARMWEAKNESS.get() && !ImmortuosCalyx.config.COLDSTRENGTH.get())
                        inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin to feel weak in warm environments.."), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTBLIND.get()){
                    if(ImmortuosCalyx.config.BLINDNESS.get())
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your vision gets darker and darker.."), inflictedPlayer.getUniqueID());}

                else if(progressionLogic == ImmortuosCalyx.config.EFFECTDAMAGE.get()){
                    if(ImmortuosCalyx.config.INFECTIONDAMAGE.get() > 0)
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You feel an overwhelming pain in your head..."), inflictedPlayer.getUniqueID());}
        });
    }
}
