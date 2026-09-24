package com.symbiote.mod.client;

import net.minecraftforge.fml.common.Mod;
import com.symbiote.mod.SymbioteMod;
import com.symbiote.mod.entity.ModEntities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SymbioteMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SymbioteClientEvents {

    private static final ResourceLocation SYMBIOTE_TEXTURE =
            new ResourceLocation(SymbioteMod.MOD_ID, "textures/entity/symbiote.png");

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SYMBIOTE.get(), SymbioteRenderer::new);
    }

    public static class SymbioteRenderer extends HumanoidMobRenderer<com.symbiote.mod.entity.SymbioteEntity, HumanoidModel<com.symbiote.mod.entity.SymbioteEntity>> {
        public SymbioteRenderer(EntityRendererProvider.Context context) {
            super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.6F);
        }

        @Override
        public ResourceLocation getTextureLocation(com.symbiote.mod.entity.SymbioteEntity entity) {
            return SYMBIOTE_TEXTURE;
        }
    }
}
