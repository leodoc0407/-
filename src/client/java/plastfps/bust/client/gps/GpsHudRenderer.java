package plastfps.bust.client.gps;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import plastfps.bust.client.config.ClientConfig;

public final class GpsHudRenderer {
	private GpsHudRenderer() {
	}

	public static void render(GuiGraphics graphics) {
		if (!GpsManager.hasActiveMarker()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return;
		}

		int width = graphics.guiWidth();
		int height = graphics.guiHeight();
		int centerX = width / 2;
		int centerY = height / 2;
		float angle = GpsManager.arrowDegrees(mc.player);
		int distance = GpsManager.distanceBlocks(mc.player);

		int radius = Math.min(width, height) / 2 - 48;
		double rad = Math.toRadians(angle);
		int arrowX = centerX + (int) (Math.sin(rad) * radius);
		int arrowY = centerY - (int) (Math.cos(rad) * radius);

		var pose = graphics.pose();
		pose.pushPose();
		pose.translate(arrowX, arrowY, 0.0f);
		pose.mulPose(Axis.ZP.rotationDegrees(angle));
		graphics.drawCenteredString(mc.font, Component.literal("\u25b2"), 0, -4, 0xFF55FFFF);
		pose.popPose();

		Component distanceText = Component.translatable("plastfps.gps.distance", distance);
		int textWidth = mc.font.width(distanceText);
		graphics.fill(centerX - textWidth / 2 - 4, 18, centerX + textWidth / 2 + 4, 32, 0x90000000);
		graphics.drawString(mc.font, distanceText, centerX - textWidth / 2, 20, 0xFF55FFFF, true);

		GpsWaypoint waypoint = ClientConfig.getGpsWaypoint();
		Component coords = Component.translatable(
			"plastfps.gps.coords",
			waypoint.x,
			waypoint.y,
			waypoint.z
		);
		int coordsWidth = mc.font.width(coords);
		graphics.drawString(mc.font, coords, centerX - coordsWidth / 2, 34, 0xFFCCCCCC, true);
	}
}
