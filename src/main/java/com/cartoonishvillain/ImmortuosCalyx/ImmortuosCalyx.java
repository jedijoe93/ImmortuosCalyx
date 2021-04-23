package com.cartoonishvillain.ImmortuosCalyx;


import com.cartoonishvillain.ImmortuosCalyx.Configs.CommonConfig;
import com.cartoonishvillain.ImmortuosCalyx.Configs.ConfigHelper;
import com.cartoonishvillain.ImmortuosCalyx.Configs.ServerConfig;
import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedIGEntity;
import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedPlayerEntity;
import com.cartoonishvillain.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("immortuoscalyx")
public class ImmortuosCalyx
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "immortuoscalyx";
    public static ServerConfig config;
    public static CommonConfig commonConfig;
    public ImmortuosCalyx() {
        config = ConfigHelper.register(ModConfig.Type.SERVER, ServerConfig::new);
        commonConfig = ConfigHelper.register(ModConfig.Type.COMMON, CommonConfig::new);
        // Register the setup method for modloading
        Register.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        InfectionManagerCapability.register();
        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(Register.INFECTEDHUMAN.get(), InfectedHumanEntity.customAttributes().build());
            GlobalEntityTypeAttributes.put(Register.INFECTEDDIVER.get(), InfectedDiverEntity.customAttributes().build());
            GlobalEntityTypeAttributes.put(Register.INFECTEDVILLAGER.get(), InfectedDiverEntity.customAttributes().build());
            GlobalEntityTypeAttributes.put(Register.INFECTEDIG.get(), InfectedIGEntity.customAttributes().build());
            GlobalEntityTypeAttributes.put(Register.INFECTEDPLAYER.get(), InfectedPlayerEntity.customAttributes().build());

        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }
    public static final ItemGroup TAB = new ItemGroup("ImmortuosCalyx") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Register.IMMORTUOSCALYXEGGS.get());
        }
    };

}
