package plastfps.bust.client;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * Blocks mostly ambient / fluid particles (soul-sand bubbles, splashes, drips, etc.) when FPS Boost is on.
 */
public final class FpsBoostParticles {
	private static final Set<String> EXTRA_PATHS = Set.of(
		"ambient_entity_effect",
		"ash",
		"block_marker",
		"bubble",
		"bubble_column_up",
		"bubble_pop",
		"campfire_cosy_smoke",
		"campfire_signal_smoke",
		"cloud",
		"crimson_spore",
		"dolphin",
		"egg_crack",
		"electric_spark",
		"enchant",
		"end_rod",
		"firefly",
		"firework",
		"fishing",
		"glow",
		"glow_squid_ink",
		"gust",
		"gust_emitter_large",
		"gust_emitter_small",
		"infested",
		"item_cobweb",
		"item_slime",
		"item_snowball",
		"lava",
		"mycelium",
		"nautilus",
		"note",
		"portal",
		"rain",
		"reverse_portal",
		"scrape",
		"small_flame",
		"small_gust",
		"smoke",
		"large_smoke",
		"sneeze",
		"soul",
		"soul_fire_flame",
		"spore_blossom_air",
		"splash",
		"squid_ink",
		"trail",
		"underwater",
		"warped_spore",
		"wax_off",
		"wax_on",
		"white_ash",
		"witch"
	);

	private FpsBoostParticles() {
	}

	public static boolean shouldSuppress(ParticleType<?> type) {
		ResourceLocation id = BuiltInRegistries.PARTICLE_TYPE.getKey(type);
		if (id == null || !ResourceLocation.DEFAULT_NAMESPACE.equals(id.getNamespace())) {
			return false;
		}
		String path = id.getPath();
		if (EXTRA_PATHS.contains(path)) {
			return true;
		}
		return path.startsWith("dripping_") || path.startsWith("falling_") || path.startsWith("landing_");
	}
}
