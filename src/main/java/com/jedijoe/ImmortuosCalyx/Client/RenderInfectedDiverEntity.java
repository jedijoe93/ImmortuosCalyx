package com.jedijoe.ImmortuosCalyx.Client;

import com.jedijoe.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class RenderInfectedDiverEntity extends BipedRenderer<InfectedDiverEntity, BipedModel<InfectedDiverEntity>> {
    public RenderInfectedDiverEntity(EntityRendererManager renderManager) {
        super(renderManager, new Model(), 0.5F);
    }
    protected final static ResourceLocation TEXTURE = new ResourceLocation(ImmortuosCalyx.MOD_ID, "textures/entity/infecteddiver.png");


    private static class Model extends BipedModel<InfectedDiverEntity> {
        private static RenderType makeRenderType(ResourceLocation texture) {
            RenderType normal = RenderType.getEntityTranslucent(texture);
            return normal;}

        Model() {
            super(Model::makeRenderType, 0, 0, 64, 64);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(InfectedDiverEntity entity) {
    return TEXTURE;
    }
}
