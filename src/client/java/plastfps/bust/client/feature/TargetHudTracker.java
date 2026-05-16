package plastfps.bust.client.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import plastfps.bust.client.config.ClientConfig;

public final class TargetHudTracker {
	private static final int TARGET_TICKS = 80;
	private static Entity target;
	private static int ticksLeft;

	private TargetHudTracker() {
	}

	public static void tick(Minecraft client) {
		if (!ClientConfig.isTargetHud()) {
			target = null;
			ticksLeft = 0;
			return;
		}

		if (client.player == null || client.level == null) {
			target = null;
			ticksLeft = 0;
			return;
		}

		Entity crosshair = client.crosshairPickEntity;
		if (crosshair != null) {
			onTargetInteract(crosshair);
		}

		if (ticksLeft > 0) {
			ticksLeft--;
		}

		if (target == null || !isVisibleTarget(target) || ticksLeft <= 0) {
			target = null;
			return;
		}

		if (client.level.getEntity(target.getId()) == null) {
			target = null;
			return;
		}

		if (client.player.tickCount % 2 != 0) {
			return;
		}

		spawnOrbitParticles(client, target);
	}

	public static void onTargetInteract(Entity entity) {
		if (!ClientConfig.isTargetHud() || !isVisibleTarget(entity)) {
			return;
		}
		target = entity;
		ticksLeft = TARGET_TICKS;
	}

	private static boolean isVisibleTarget(Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return false;
		}
		if (!living.isAlive()) {
			return false;
		}
		if (living.isInvisible()) {
			return false;
		}
		if (living.hasEffect(MobEffects.INVISIBILITY)) {
			return false;
		}
		return true;
	}

	private static void spawnOrbitParticles(Minecraft client, Entity entity) {
		double time = client.level.getGameTime() * 0.12;
		double radius = entity.getBbWidth() + 0.55;
		double height = entity.getY() + entity.getBbHeight() * 0.55;

		for (int i = 0; i < 6; i++) {
			double angle = time + (Math.PI * 2.0 * i / 6.0);
			double px = entity.getX() + Math.cos(angle) * radius;
			double pz = entity.getZ() + Math.sin(angle) * radius;
			double py = height + Math.sin(time * 2.0 + i) * 0.15;
			client.level.addParticle(
				ParticleTypes.CRIT,
				px,
				py,
				pz,
				0.0,
				0.02,
				0.0
			);
		}
	}
}
