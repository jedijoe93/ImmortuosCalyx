package com.jedijoe.ImmortuosCalyx.Client;

import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedIGEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedVillagerEntity;
import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class RenderInfectedIGEntity extends MobRenderer<InfectedIGEntity, IronGolemModel<InfectedIGEntity>> {

    public RenderInfectedIGEntity(EntityRendererManager renderManager) {
        super(renderManager, new IronGolemModel<>(), 0.5F);
    }


    protected final static ResourceLocation TEXTURE = new ResourceLocation(ImmortuosCalyx.MOD_ID, "textures/entity/infectedig.png");

    private static class Model extends BipedModel<InfectedHumanEntity> {
        private static RenderType makeRenderType(ResourceLocation texture) {
            RenderType normal = RenderType.getEntityTranslucent(texture);
            return normal;}

        Model() {
            super(Model::makeRenderType, 0, 0, 64, 64);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(InfectedIGEntity entity) {
        return TEXTURE;
    }

}
