package com.jedijoe.mushroominfection.Infection;

import com.jedijoe.mushroominfection.MushroomInfection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

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
    public static void testTicker(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER){
        PlayerEntity entity = event.player;
        entity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h ->{
            if(entity.isCrouching()){h.setInfectionProgress(0);}
            else if(entity.isSwimming()){h.setInfectionProgress(1);}
            if(h.getInfectionProgress() >= 1){
                Random rand = new Random();
                if((1 + rand.nextFloat() * (100 - 1)) > 99.5){
                h.addInfectionProgress(1);
            }
                String msg = "Infection level: " + h.getInfectionProgress();
                StringTextComponent stringTextComponent = new StringTextComponent(msg);
                entity.sendStatusMessage(stringTextComponent, true);
            }
        });}
    }

}
