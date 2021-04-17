package com.jedijoe.ImmortuosCalyx.Client;

import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedPlayerEntity;
import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.layers.IronGolemCracksLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.TeleportationRepositioner;

public class RenderInfectedPlayerEntity extends BipedRenderer<InfectedPlayerEntity, BipedModel<InfectedPlayerEntity>> {
    public RenderInfectedPlayerEntity(EntityRendererManager renderManager) {
        super(renderManager, new Model(), 0.5F);
        this.addLayer(new BloodiedPlayerLayer(this));
    }

    protected final static ResourceLocation TEXTURE = new ResourceLocation(ImmortuosCalyx.MOD_ID, "textures/entity/infectedhuman.png");

    private static class Model extends BipedModel<InfectedPlayerEntity> {
        private static RenderType makeRenderType(ResourceLocation texture) {
            RenderType normal = RenderType.getEntityTranslucent(texture);
            return normal;}

        Model() {
            super(Model::makeRenderType, 0, 0, 64, 64);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(InfectedPlayerEntity entity) {
        try{
        return PlayerSkinManager.getSkin(entity.getUUID(), entity.getName().getString());
        } catch (NullPointerException e){
            return TEXTURE;
        }
    }

}
