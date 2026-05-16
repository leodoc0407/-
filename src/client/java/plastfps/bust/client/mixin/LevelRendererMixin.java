package plastfps.bust.client.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import plastfps.bust.client.config.ClientConfig;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
	private void plastfps$customSkyColor(Vec3 cameraPosition, float partialTick, CallbackInfoReturnable<Integer> cir) {
		if (ClientConfig.isWorldCustomizer()) {
			cir.setReturnValue(ClientConfig.getSkyColor());
		}
	}
}
