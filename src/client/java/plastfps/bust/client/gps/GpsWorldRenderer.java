package plastfps.bust.client.gps;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class GpsWorldRenderer {
	private static final float R = 0.2f;
	private static final float G = 0.95f;
	private static final float B = 1.0f;
	private static final float A = 0.95f;

	private GpsWorldRenderer() {
	}

	public static void render(WorldRenderContext context) {
		if (!GpsManager.hasActiveMarker()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null) {
			return;
		}

		GpsWaypoint waypoint = plastfps.bust.client.config.ClientConfig.getGpsWaypoint();
		double mx = waypoint.x + 0.5;
		double my = waypoint.y + 0.5;
		double mz = waypoint.z + 0.5;

		PoseStack poseStack = context.matrixStack();
		Vec3 cam = context.camera().getPosition();
		poseStack.pushPose();
		poseStack.translate(-cam.x, -cam.y, -cam.z);

		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer consumer = buffers.getBuffer(RenderType.lines());
		Matrix4f matrix = poseStack.last().pose();

		AABB box = new AABB(waypoint.x, waypoint.y, waypoint.z, waypoint.x + 1.0, waypoint.y + 1.0, waypoint.z + 1.0)
			.inflate(0.02);
		drawBox(consumer, matrix, box);

		int topY = mc.level.getMaxY();
		drawLine(consumer, matrix, mx, my, mz, mx, topY, mz);

		Vec3 player = mc.player.position();
		drawLine(consumer, matrix, player.x, player.y + mc.player.getEyeHeight() * 0.5, player.z, mx, my, mz);

		buffers.endBatch(RenderType.lines());
		poseStack.popPose();
	}

	private static void drawLine(VertexConsumer consumer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2) {
		consumer.addVertex(matrix, (float) x1, (float) y1, (float) z1).setColor(R, G, B, A);
		consumer.addVertex(matrix, (float) x2, (float) y2, (float) z2).setColor(R, G, B, A);
	}

	private static void drawBox(VertexConsumer consumer, Matrix4f matrix, AABB box) {
		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;
		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		drawLine(consumer, matrix, x1, y1, z1, x2, y1, z1);
		drawLine(consumer, matrix, x2, y1, z1, x2, y1, z2);
		drawLine(consumer, matrix, x2, y1, z2, x1, y1, z2);
		drawLine(consumer, matrix, x1, y1, z2, x1, y1, z1);

		drawLine(consumer, matrix, x1, y2, z1, x2, y2, z1);
		drawLine(consumer, matrix, x2, y2, z1, x2, y2, z2);
		drawLine(consumer, matrix, x2, y2, z2, x1, y2, z2);
		drawLine(consumer, matrix, x1, y2, z2, x1, y2, z1);

		drawLine(consumer, matrix, x1, y1, z1, x1, y2, z1);
		drawLine(consumer, matrix, x2, y1, z1, x2, y2, z1);
		drawLine(consumer, matrix, x2, y1, z2, x2, y2, z2);
		drawLine(consumer, matrix, x1, y1, z2, x1, y2, z2);
	}
}
