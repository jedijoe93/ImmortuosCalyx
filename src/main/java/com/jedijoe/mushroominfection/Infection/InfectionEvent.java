package com.jedijoe.mushroominfection.Infection;

import com.jedijoe.mushroominfection.MushroomInfection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = MushroomInfection.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InfectionEvent {

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof PlayerEntity){
            InfectionManager provider = new InfectionManager();
            event.addCapability(new ResourceLocation(MushroomInfection.MOD_ID, "infection"), provider);
        }
    }

    @SubscribeEvent
    public static void InfectionTicker(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START){
        PlayerEntity player = event.player;
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h ->{
            if(player.isCrouching()){h.addInfectionProgress(1);}//TEST LINE
            else if(player.isSwimming()){h.setInfectionProgress(0);}//TEST LINE
            if(h.getInfectionProgress() >= 1){
                h.addInfectionTimer(1);
                if(h.getInfectionTimer() == 450){
                    h.addInfectionProgress(1);
                    h.addInfectionTimer(-450);
                    EffectController(event.player);
                }
                String msg = "Infection level: " + h.getInfectionProgress(); //THIS ENTIRE SET OF CODE IS DEBUG TO VIEW INFECTION LEVELS AT WORK
                StringTextComponent stringTextComponent = new StringTextComponent(msg);
                player.sendStatusMessage(stringTextComponent, true);
            }
        });}
    }
    

    @SubscribeEvent
    public static void InfectionChat(ServerChatEvent event){
        PlayerEntity player = event.getPlayer();
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() >= 40) event.setCanceled(true); //if the player's infection is @ or above 40%, they can no longer speak in text chat.
        });
    }

    @SubscribeEvent public static void InfectOtherPlayer(AttackEntityEvent event){
        if(event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof PlayerEntity){
            PlayerEntity target = (PlayerEntity) event.getTarget(); //the player who got hit
            PlayerEntity aggro = (PlayerEntity) event.getEntity(); //the player that hit
            int protection = target.getTotalArmorValue(); //grabs the armor value of the target
            AtomicInteger infectionRateGrabber = new AtomicInteger(); //funky value time
            aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                int infectionProgression = h.getInfectionProgress();
                infectionRateGrabber.addAndGet(infectionProgression); // sets the atomic int equal to the infection % of the attacker

            });
            AtomicBoolean infectedGrabber = new AtomicBoolean(false); //A surprise bool that will help us later
            int convert = infectionRateGrabber.intValue();// converts the atomic int into a normal int for better math
            if(convert > 49){ // if the attacker's infection rate is at or above 50%
                int conversionThreshold = (convert - (protection*2)); // creates mimimum score needed to not get infected
                Random rand = new Random();
                if(conversionThreshold > rand.nextInt(100)){ // rolls for infection. If random value rolls below threshold, target is at risk of infection.
                    target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                        if(h.getInfectionProgress() == 0){h.addInfectionProgress(1); infectedGrabber.set(true);} // if target is not already infected, and the risk for infection exists, they are freshly infected. Surprise bool gets activated
                    });
                }
            }
            if(infectedGrabber.get()){aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{ //if surprise bool is activated, it means the aggressor player successfully infected someone. Removing part of the parasite and having it get on someone else.
                h.addInfectionProgress(-5); });}  //because of that, symptoms are reduced.
        }
    }

    private static void EffectController(PlayerEntity inflictedPlayer){
        inflictedPlayer.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            int progressionLogic = h.getInfectionProgress();
            switch(progressionLogic){
                default:
                    break;
                case 10:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your throat feels sore"), inflictedPlayer.getUniqueID());
                    break;
                case 25:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your mind feels foggy"), inflictedPlayer.getUniqueID());
                    break;
                case 40:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You feel something moving around in your head, you try to yell, but nothing comes out"), inflictedPlayer.getUniqueID());
                    break;
                case 50:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "There is something on your skin and you can't get it off.."), inflictedPlayer.getUniqueID());
                    break;
                case 60:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You start feeling ill in hot environments, and better in cool ones.."), inflictedPlayer.getUniqueID());
                    break;
            }
        });
    }

}
