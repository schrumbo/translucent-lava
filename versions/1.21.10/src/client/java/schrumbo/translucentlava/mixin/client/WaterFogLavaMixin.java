package schrumbo.translucentlava.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.fog.WaterFogModifier;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import schrumbo.translucentlava.features.TransLava;

@Environment(EnvType.CLIENT)
@Mixin(WaterFogModifier.class)
public class WaterFogLavaMixin {

    @Inject(method = "shouldApply", at = @At("HEAD"), cancellable = true)
    private void applyWaterFogForLava(CameraSubmersionType submersionType, Entity cameraEntity, CallbackInfoReturnable<Boolean> cir) {
        if (TransLava.isEnabled() && submersionType == CameraSubmersionType.LAVA) {
            cir.setReturnValue(true);
        }
    }
}
