package plastfps.bust.client.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import plastfps.bust.client.config.ClientConfig;

public final class InventoryHelpHudOverlay {
	private InventoryHelpHudOverlay() {
	}

	public static void render(GuiGraphics graphics) {
		if (!ClientConfig.isInventoryHelp()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || mc.level == null) {
			return;
		}
		if (mc.screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<?>) {
			return;
		}

		int width = graphics.guiWidth();
		int height = graphics.guiHeight();
		int left = width / 2 - 91;
		int top = height - 22;

		for (int slot = 0; slot < 9; slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			int color = InventoryHelp.getSlotHighlightColor(stack, mc.level);
			if (color == 0) {
				continue;
			}
			int x = left + slot * 20;
			int y = top;
			graphics.fill(x, y, x + 16, y + 16, color);
		}
	}
}
