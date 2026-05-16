package plastfps.bust.client.gps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import plastfps.bust.client.config.ClientConfig;

public final class GpsManager {
	private GpsManager() {
	}

	public static boolean hasActiveMarker() {
		GpsWaypoint waypoint = ClientConfig.getGpsWaypoint();
		if (!waypoint.active) {
			return false;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return false;
		}
		return isSameDimension(mc.level, waypoint.dimension);
	}

	public static Vec3 markerCenter() {
		GpsWaypoint waypoint = ClientConfig.getGpsWaypoint();
		return new Vec3(waypoint.x + 0.5, waypoint.y + 0.5, waypoint.z + 0.5);
	}

	public static void setMarker(int x, int y, int z) {
		Minecraft mc = Minecraft.getInstance();
		GpsWaypoint waypoint = new GpsWaypoint();
		waypoint.active = true;
		waypoint.x = x;
		waypoint.y = y;
		waypoint.z = z;
		if (mc.level != null) {
			waypoint.dimension = dimensionKey(mc.level);
		}
		ClientConfig.setGpsWaypoint(waypoint);
		ClientConfig.save();
	}

	public static void clearMarker() {
		GpsWaypoint waypoint = new GpsWaypoint();
		ClientConfig.setGpsWaypoint(waypoint);
		ClientConfig.save();
	}

	public static int distanceBlocks(LocalPlayer player) {
		return (int) Math.round(player.position().distanceTo(markerCenter()));
	}

	public static float arrowDegrees(LocalPlayer player) {
		Vec3 target = markerCenter();
		double dx = target.x - player.getX();
		double dz = target.z - player.getZ();
		float targetYaw = (float) (Mth.atan2(-dx, dz) * (180.0 / Math.PI));
		return Mth.wrapDegrees(targetYaw - player.getYRot());
	}

	private static boolean isSameDimension(Level level, String savedDimension) {
		if (savedDimension == null || savedDimension.isEmpty()) {
			return true;
		}
		return dimensionKey(level).equals(savedDimension);
	}

	private static String dimensionKey(Level level) {
		ResourceKey<Level> key = level.dimension();
		return key.location().toString();
	}
}
