package plastfps.bust.client.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import plastfps.bust.client.config.ClientConfig;

public final class JumpCircleRenderer {
	private static final long CIRCLE_LIFETIME_MS = 900L;
	private static final int SEGMENTS = 56;

	private JumpCircleRenderer() {
	}

	public static void render(WorldRenderContext context) {
		if (!ClientConfig.isJumpCircle() || JumpCircleManager.circles().isEmpty()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return;
		}

		long now = System.currentTimeMillis();
		int rgb = ClientConfig.getJumpCircleColor();
		float r = ((rgb >> 16) & 0xFF) / 255.0f;
		float g = ((rgb >> 8) & 0xFF) / 255.0f;
		float b = (rgb & 0xFF) / 255.0f;

		PoseStack poseStack = context.matrixStack();
		Vec3 cam = context.camera().getPosition();
		poseStack.pushPose();
		poseStack.translate(-cam.x, -cam.y, -cam.z);

		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer consumer = buffers.getBuffer(RenderType.lines());
		Matrix4f matrix = poseStack.last().pose();

		for (JumpCircleManager.JumpCircle circle : JumpCircleManager.circles()) {
			float progress = (now - circle.spawnTimeMs()) / (float) CIRCLE_LIFETIME_MS;
			if (progress < 0.0f || progress > 1.0f) {
				continue;
			}
			float radius = 0.35f + progress * 1.65f;
			float alpha = 1.0f - progress;
			drawRing(consumer, matrix, circle.x(), circle.y() + 0.04, circle.z(), radius, r, g, b, alpha);
		}

		buffers.endBatch(RenderType.lines());
		poseStack.popPose();
	}

	private static void drawRing(
		VertexConsumer consumer,
		Matrix4f matrix,
		double cx,
		double cy,
		double cz,
		float radius,
		float r,
		float g,
		float b,
		float a
	) {
		double prevX = cx + radius;
		double prevZ = cz;
		for (int i = 1; i <= SEGMENTS; i++) {
			double angle = (Math.PI * 2.0 * i) / SEGMENTS;
			double x = cx + Math.cos(angle) * radius;
			double z = cz + Math.sin(angle) * radius;
			consumer.addVertex(matrix, (float) prevX, (float) cy, (float) prevZ).setColor(r, g, b, a);
			consumer.addVertex(matrix, (float) x, (float) cy, (float) z).setColor(r, g, b, a);
			prevX = x;
			prevZ = z;
		}
	}
}
