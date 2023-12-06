package net.yam7da.artisexplosion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.yam7da.artisexplosion.entity.DynamiteEntity;
import net.yam7da.artisexplosion.block.ArtIsExplosionBlocks;
import net.yam7da.artisexplosion.entity.ArtIsExplosionEntities;
import net.yam7da.artisexplosion.renderers.ArtIsExplosionBlockEntityRenderer;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ArtIsExplosionModClient implements ClientModInitializer {

    public static void registerRenders() {
        registerItemEntityRenders(
          ArtIsExplosionEntities.DYNAMITE,
          ArtIsExplosionEntities.TRIGGER_DYNAMITE
        );
        registerBlockEntityRender(ArtIsExplosionEntities.ICTU_DE_CAELO, e -> ArtIsExplosionBlocks.ICTU_DE_CAELO.getDefaultState());

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
            ArtIsExplosionBlocks.ICTU_DE_CAELO
        );
    }

    @SafeVarargs
    private static void registerItemEntityRenders(EntityType<? extends FlyingItemEntity>... entityTypes) {
        for (EntityType<? extends FlyingItemEntity> entityType : entityTypes) {
            registerItemEntityRender(entityType);
        }
    }

    private static <T extends Entity & FlyingItemEntity> void registerItemEntityRender(EntityType<T> entityType) {
        EntityRendererRegistry.register(entityType, FlyingItemEntityRenderer::new);
    }

    private static <T extends DynamiteEntity> void registerBlockEntityRender(EntityType<T> block, Function<T, BlockState> stateGetter) {
        EntityRendererRegistry.register(block, ctx -> new ArtIsExplosionBlockEntityRenderer<>(ctx, stateGetter));
    }

    @Override
    public void onInitializeClient() {

    }
}
