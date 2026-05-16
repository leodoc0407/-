package plastfps.bust.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import plastfps.bust.client.config.ClientConfig;

public final class ArmourHudRenderer {
	private static final EquipmentSlot[] SLOTS = {
		EquipmentSlot.HEAD,
		EquipmentSlot.CHEST,
		EquipmentSlot.LEGS,
		EquipmentSlot.FEET
	};

	private ArmourHudRenderer() {
	}

	public static void render(GuiGraphics graphics) {
		if (!ClientConfig.isArmourHud()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) {
			return;
		}

		int x = HudPositions.getX(HudElement.ARMOUR, graphics);
		int y = HudPositions.getY(HudElement.ARMOUR, graphics);
		int slotX = x;

		for (EquipmentSlot slot : SLOTS) {
			ItemStack stack = player.getItemBySlot(slot);
			graphics.renderItem(stack, slotX, y);
			graphics.renderItemDecorations(mc.font, stack, slotX, y);

			if (stack.isDamageableItem()) {
				int max = stack.getMaxDamage();
				int left = max - stack.getDamageValue();
				int percent = max > 0 ? (left * 100) / max : 100;
				int color = percent > 60 ? 0xFF55FF55 : (percent > 25 ? 0xFFFFFF55 : 0xFFFF5555);
				String text = String.valueOf(left);
				int tw = mc.font.width(text);
				graphics.drawString(mc.font, text, slotX + 8 - tw / 2, y + 17, color, true);
			}
			slotX += 18;
		}
	}
}
