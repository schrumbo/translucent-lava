package schrumbo.translucentlava.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.render.fog.LavaFogModifier;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import schrumbo.translucentlava.features.TransLava;

@Environment(EnvType.CLIENT)
@Mixin(LavaFogModifier.class)
public class LavaFogMixin {

    @Inject(method = "shouldApply", at = @At("HEAD"), cancellable = true)
    private void disableLavaFog(CameraSubmersionType type, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (TransLava.isEnabled() && type == CameraSubmersionType.LAVA) {
            cir.setReturnValue(false);
        }
    }
}