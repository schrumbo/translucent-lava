package schrumbo.translucentlava.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TransLava {

    private static boolean currentState = false;
    private static final WaterLikeFluidHandler WATER_HANDLER = new WaterLikeFluidHandler();
    private static FluidRenderHandler originalLavaHandler = null;
    private static FluidRenderHandler originalFlowingLavaHandler = null;

    public static void init() {
        originalLavaHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.LAVA);
        originalFlowingLavaHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.FLOWING_LAVA);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of("translucent-lava", "fluid_reload");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        var atlasId = Identifier.ofVanilla("textures/atlas/blocks.png");
                        SpriteAtlasTexture atlas = MinecraftClient.getInstance().getBakedModelManager().getAtlas(atlasId);
                        WATER_HANDLER.reloadTextures(atlas);
                    }
                }
        );
    }


    public static void update(boolean configEnabled) {
        if (currentState != configEnabled) {
            currentState = configEnabled;
            applyState();
        }
    }

    /**
     * Wendet den aktuellen Status an
     */
    private static void applyState() {
        if (currentState) {
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, WATER_HANDLER);
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.FLOWING_LAVA, WATER_HANDLER);

            BlockRenderLayerMap.putFluid(Fluids.LAVA, BlockRenderLayer.TRANSLUCENT);
            BlockRenderLayerMap.putFluid(Fluids.FLOWING_LAVA, BlockRenderLayer.TRANSLUCENT);
            BlockRenderLayerMap.putBlock(Blocks.LAVA, BlockRenderLayer.TRANSLUCENT);
        } else {
            if (originalLavaHandler != null) {
                FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, originalLavaHandler);
                FluidRenderHandlerRegistry.INSTANCE.register(Fluids.FLOWING_LAVA, originalFlowingLavaHandler);
            }
            BlockRenderLayerMap.putFluid(Fluids.LAVA, BlockRenderLayer.SOLID);
            BlockRenderLayerMap.putFluid(Fluids.FLOWING_LAVA, BlockRenderLayer.SOLID);
            BlockRenderLayerMap.putBlock(Blocks.LAVA, BlockRenderLayer.SOLID);
        }

        reloadWorld();
    }


    public static boolean isEnabled() {
        return currentState;
    }

    private static void reloadWorld() {
        MinecraftClient client = MinecraftClient.getInstance();
        WorldRenderer renderer = client.worldRenderer;
        if (renderer != null) {
            renderer.reload();
        }
    }

    private static class WaterLikeFluidHandler implements FluidRenderHandler {
        private Sprite[] waterSprites = new Sprite[2];

        @Override
        public Sprite[] getFluidSprites(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
            return waterSprites;
        }

        @Override
        public void reloadTextures(SpriteAtlasTexture atlas) {
            waterSprites[0] = atlas.getSprite(Identifier.ofVanilla("block/water_still"));
            waterSprites[1] = atlas.getSprite(Identifier.ofVanilla("block/water_flow"));
        }

        @Override
        public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
            if (view != null && pos != null) {
                return BiomeColors.getWaterColor(view, pos);
            }
            return 0x3F76E4;
        }
    }
}