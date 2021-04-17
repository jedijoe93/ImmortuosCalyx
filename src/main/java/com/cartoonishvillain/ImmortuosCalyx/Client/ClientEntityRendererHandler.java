package com.cartoonishvillain.ImmortuosCalyx.Client;

import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import com.cartoonishvillain.ImmortuosCalyx.Register;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEntityRendererHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(Register.INFECTEDHUMAN.get(), RenderInfectedHumanEntity::new);
        RenderingRegistry.registerEntityRenderingHandler(Register.INFECTEDDIVER.get(), RenderInfectedDiverEntity::new);
        RenderingRegistry.registerEntityRenderingHandler(Register.INFECTEDVILLAGER.get(), RenderInfectedVillagerEntity::new);
        RenderingRegistry.registerEntityRenderingHandler(Register.INFECTEDIG.get(), RenderInfectedIGEntity::new);
        RenderingRegistry.registerEntityRenderingHandler(Register.INFECTEDPLAYER.get(), RenderInfectedPlayerEntity::new);
    }
}
