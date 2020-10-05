package com.jedijoe.ImmortuosCalyx.Client;

import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class RenderInfectedHumanEntity extends BipedRenderer<InfectedHumanEntity, BipedModel<InfectedHumanEntity>> {
    public RenderInfectedHumanEntity(EntityRendererManager renderManager) {
        super(renderManager, new Model(), 1F);
    }


    private static class Model extends BipedModel<InfectedHumanEntity> {
        private static RenderType makeRenderType(ResourceLocation texture) {
            RenderType normal = RenderType.getEntityTranslucent(texture);
            return normal;}

        Model() {
            super(Model::makeRenderType, 0, 0, 64, 64);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(InfectedHumanEntity entity) {
        Minecraft mc = Minecraft.getInstance();
        if(!(mc.getRenderViewEntity() instanceof AbstractClientPlayerEntity)){
            return DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID());
        }
        return ((AbstractClientPlayerEntity) mc.getRenderViewEntity()).getLocationSkin();
    }
}
