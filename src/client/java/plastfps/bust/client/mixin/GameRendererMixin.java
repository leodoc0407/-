package plastfps.bust.client.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import plastfps.bust.client.config.ClientConfig;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "getProjectionMatrix", at = @At("RETURN"), cancellable = true)
	private void plastfps$stretchAspect(float tickDelta, CallbackInfoReturnable<Matrix4f> cir) {
		if (!ClientConfig.isAspectRatio()) {
			return;
		}
		float stretch = ClientConfig.getAspectStretch();
		if (Math.abs(stretch - 1.0f) < 0.001f) {
			return;
		}
		Matrix4f matrix = new Matrix4f(cir.getReturnValue());
		matrix.m00(matrix.m00() / stretch);
		cir.setReturnValue(matrix);
	}
}
