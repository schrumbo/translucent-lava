package schrumbo.translucentlava.features;


import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class TransLava {

    private static boolean currentState = false;
    private static final DelegateFluidHandler WATER_HANDLER = new DelegateFluidHandler();
    private static FluidRenderHandler originalLavaHandler;
    private static FluidRenderHandler originalFlowingLavaHandler;
    private static FluidRenderHandler originalWaterHandler;

    public static void init() {
        originalLavaHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.LAVA);
        originalFlowingLavaHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.FLOWING_LAVA);
        originalWaterHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER);
        WATER_HANDLER.setDelegate(originalWaterHandler);
    }

    public static void update(boolean enabled) {
        if (currentState == enabled) return;
        currentState = enabled;
        applyState();
    }

    public static boolean isEnabled() {
        return currentState;
    }

    private static void applyState() {
        if (currentState) {
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, WATER_HANDLER);
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.FLOWING_LAVA, WATER_HANDLER);
            BlockRenderLayerMap.putFluids(BlockRenderLayer.TRANSLUCENT, Fluids.LAVA, Fluids.FLOWING_LAVA);
        } else {
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.LAVA, originalLavaHandler);
            FluidRenderHandlerRegistry.INSTANCE.register(Fluids.FLOWING_LAVA, originalFlowingLavaHandler);
            BlockRenderLayerMap.putFluids(BlockRenderLayer.SOLID, Fluids.LAVA, Fluids.FLOWING_LAVA);
        }
        reloadWorld();
    }

    private static void reloadWorld() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    private static class DelegateFluidHandler implements FluidRenderHandler {
        private FluidRenderHandler delegate;

        void setDelegate(FluidRenderHandler d) {
            this.delegate = d;
        }

        @Override
        public Sprite[] getFluidSprites(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
            return delegate.getFluidSprites(view, pos, state);
        }

        @Override
        public void reloadTextures(SpriteAtlasTexture atlas) {
        }

        @Override
        public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
            return delegate.getFluidColor(view, pos, state);
        }
    }
}