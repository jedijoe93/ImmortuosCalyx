package com.cartoonishvillain.ImmortuosCalyx.Client;

import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.cartoonishvillain.ImmortuosCalyx.Entity.InfectedVillagerEntity;
import com.cartoonishvillain.ImmortuosCalyx.ImmortuosCalyx;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class RenderInfectedVillagerEntity extends MobRenderer<InfectedVillagerEntity, VillagerModel<InfectedVillagerEntity>> {
    public RenderInfectedVillagerEntity(EntityRendererManager renderManager) {
        super(renderManager, new VillagerModel<InfectedVillagerEntity>(0), 0.5F);
    }

    protected final static ResourceLocation TEXTURE = new ResourceLocation(ImmortuosCalyx.MOD_ID, "textures/entity/infectedvillager.png");

    private static class Model extends BipedModel<InfectedHumanEntity> {
        private static RenderType makeRenderType(ResourceLocation texture) {
            RenderType normal = RenderType.getEntityTranslucent(texture);
            return normal;}

        Model() {
            super(Model::makeRenderType, 0, 0, 64, 64);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(InfectedVillagerEntity entity) {
        return TEXTURE;
    }

}
