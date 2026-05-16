package plastfps.bust.client.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import plastfps.bust.client.FpsBoostParticles;
import plastfps.bust.client.config.ClientConfig;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
	@Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
	private void plastfps$suppressAmbientParticles(ParticleOptions particleData, double x, double y, double z, double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir) {
		if (ClientConfig.isFpsBoost() && FpsBoostParticles.shouldSuppress(particleData.getType())) {
			cir.setReturnValue(null);
		}
	}
}
