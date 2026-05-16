package plastfps.bust.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import plastfps.bust.client.config.ClientConfig;

public final class InventoryHudRenderer {
	private InventoryHudRenderer() {
	}

	public static void render(GuiGraphics graphics) {
		if (!ClientConfig.isInventoryHud()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) {
			return;
		}

		int x = HudPositions.getX(HudElement.INVENTORY, graphics);
		int y = HudPositions.getY(HudElement.INVENTORY, graphics);
		int panelW = 9 * 18 + 8;
		int panelH = 3 * 18 + 8 + 18 + 6;
		graphics.fill(x - 4, y - 4, x + panelW - 4, y + panelH - 4, 0x90000000);

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int slot = col + (row + 1) * 9;
				ItemStack stack = player.getInventory().getItem(slot);
				int sx = x + col * 18;
				int sy = y + row * 18;
				graphics.renderItem(stack, sx, sy);
				graphics.renderItemDecorations(mc.font, stack, sx, sy);
			}
		}

		int hotbarY = y + 3 * 18 + 4;
		for (int col = 0; col < 9; col++) {
			ItemStack stack = player.getInventory().getItem(col);
			int sx = x + col * 18;
			graphics.renderItem(stack, sx, hotbarY);
			graphics.renderItemDecorations(mc.font, stack, sx, hotbarY);
		}
	}
}
