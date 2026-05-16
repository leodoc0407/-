package plastfps.bust.client.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import plastfps.bust.client.config.ClientConfig;

public final class GroundItemRenderer {
	private static final double MAX_DISTANCE_SQ = 64.0 * 64.0;

	private GroundItemRenderer() {
	}

	public static void render(WorldRenderContext context) {
		if (!ClientConfig.isInventoryHelp() && !ClientConfig.isItemName()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null) {
			return;
		}

		Vec3 cam = context.camera().getPosition();
		boolean drawHighlights = ClientConfig.isInventoryHelp();
		boolean drawNames = ClientConfig.isItemName();

		PoseStack poseStack = context.matrixStack();
		poseStack.pushPose();
		poseStack.translate(-cam.x, -cam.y, -cam.z);

		if (drawHighlights) {
			renderHighlights(mc, poseStack, cam);
		}

		if (drawNames) {
			renderItemNames(mc, poseStack, cam);
		}

		poseStack.popPose();
	}

	private static void renderHighlights(Minecraft mc, PoseStack poseStack, Vec3 cam) {
		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer consumer = buffers.getBuffer(RenderType.lines());
		Matrix4f matrix = poseStack.last().pose();

		for (Entity entity : mc.level.entitiesForRendering()) {
			if (!(entity instanceof ItemEntity itemEntity)) {
				continue;
			}
			if (entity.distanceToSqr(cam) > MAX_DISTANCE_SQ) {
				continue;
			}
			ItemStack stack = itemEntity.getItem();
			int color = InventoryHelp.getGroundHighlightColor(stack, mc.level);
			if (color == 0) {
				continue;
			}
			float r = ((color >> 16) & 0xFF) / 255.0f;
			float g = ((color >> 8) & 0xFF) / 255.0f;
			float b = (color & 0xFF) / 255.0f;
			float a = ((color >> 24) & 0xFF) / 255.0f;
			drawBox(consumer, matrix, itemEntity.getBoundingBox().inflate(0.08), r, g, b, a);
		}

		buffers.endBatch(RenderType.lines());
	}

	private static void renderItemNames(Minecraft mc, PoseStack poseStack, Vec3 cam) {
		Font font = mc.font;
		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

		for (Entity entity : mc.level.entitiesForRendering()) {
			if (!(entity instanceof ItemEntity itemEntity)) {
				continue;
			}
			if (entity.distanceToSqr(cam) > MAX_DISTANCE_SQ) {
				continue;
			}
			ItemStack stack = itemEntity.getItem();
			if (stack.isEmpty()) {
				continue;
			}

			Component name = stack.getHoverName();
			double labelY = itemEntity.getY() + itemEntity.getBbHeight() + 0.35;
			double dx = itemEntity.getX() - cam.x;
			double dy = labelY - cam.y;
			double dz = itemEntity.getZ() - cam.z;

			poseStack.pushPose();
			poseStack.translate(dx, dy, dz);
			poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
			float scale = 0.025f;
			poseStack.scale(-scale, -scale, scale);

			Matrix4f matrix = poseStack.last().pose();
			float width = -font.width(name) / 2.0f;
			int light = LevelRenderer.getLightColor(mc.level, itemEntity.blockPosition());
			font.drawInBatch(
				name,
				width,
				0.0f,
				0xFFFFFFFF,
				false,
				matrix,
				buffers,
				Font.DisplayMode.SEE_THROUGH,
				0,
				light
			);
			poseStack.popPose();
		}

		buffers.endBatch();
	}

	private static void drawBox(VertexConsumer consumer, Matrix4f matrix, AABB box, float r, float g, float b, float a) {
		float x1 = (float) box.minX;
		float y1 = (float) box.minY;
		float z1 = (float) box.minZ;
		float x2 = (float) box.maxX;
		float y2 = (float) box.maxY;
		float z2 = (float) box.maxZ;

		line(consumer, matrix, x1, y1, z1, x2, y1, z1, r, g, b, a);
		line(consumer, matrix, x2, y1, z1, x2, y1, z2, r, g, b, a);
		line(consumer, matrix, x2, y1, z2, x1, y1, z2, r, g, b, a);
		line(consumer, matrix, x1, y1, z2, x1, y1, z1, r, g, b, a);

		line(consumer, matrix, x1, y2, z1, x2, y2, z1, r, g, b, a);
		line(consumer, matrix, x2, y2, z1, x2, y2, z2, r, g, b, a);
		line(consumer, matrix, x2, y2, z2, x1, y2, z2, r, g, b, a);
		line(consumer, matrix, x1, y2, z2, x1, y2, z1, r, g, b, a);

		line(consumer, matrix, x1, y1, z1, x1, y2, z1, r, g, b, a);
		line(consumer, matrix, x2, y1, z1, x2, y2, z1, r, g, b, a);
		line(consumer, matrix, x2, y1, z2, x2, y2, z2, r, g, b, a);
		line(consumer, matrix, x1, y1, z2, x1, y2, z2, r, g, b, a);
	}

	private static void line(
		VertexConsumer consumer,
		Matrix4f matrix,
		float x1,
		float y1,
		float z1,
		float x2,
		float y2,
		float z2,
		float r,
		float g,
		float b,
		float a
	) {
		consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
		consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a);
	}
}
