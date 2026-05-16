package plastfps.bust.client.hud;

import net.minecraft.client.gui.GuiGraphics;

public final class HudRenderer {
	private HudRenderer() {
	}

	public static void render(GuiGraphics graphics, float partialTick) {
		ClientHudRenderer.render(graphics);
		ArmourHudRenderer.render(graphics);
		PotionHudRenderer.render(graphics);
		InventoryHudRenderer.render(graphics);
		CooldownHudRenderer.render(graphics, partialTick);
		HudDragManager.renderEditOverlay(graphics);
	}
}
