package plastfps.bust.client.hud;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import plastfps.bust.client.config.ClientConfig;
import plastfps.bust.client.mixin.ItemCooldownsAccessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CooldownHudRenderer {
	private CooldownHudRenderer() {
	}

	public static void render(GuiGraphics graphics, float partialTick) {
		if (!ClientConfig.isCooldownHud()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) {
			return;
		}

		ItemCooldowns cooldowns = mc.player.getCooldowns();
		Object2ObjectMap<Item, ItemCooldowns.CooldownInstance> map =
			((ItemCooldownsAccessor) cooldowns).plastfps$getCooldowns();
		if (map.isEmpty()) {
			return;
		}

		List<Entry> entries = new ArrayList<>();
		int tick = mc.player.tickCount;
		for (Object2ObjectMap.Entry<Item, ItemCooldowns.CooldownInstance> entry : map.object2ObjectEntrySet()) {
			Item item = entry.getKey();
			ItemCooldowns.CooldownInstance instance = entry.getValue();
			int remaining = instance.endTime() - tick;
			if (remaining <= 0) {
				continue;
			}
			entries.add(new Entry(item, remaining, cooldowns.getCooldownPercent(item, partialTick)));
		}
		if (entries.isEmpty()) {
			return;
		}
		entries.sort(Comparator.comparingInt(e -> -e.remainingTicks));

		int x = HudPositions.getX(HudElement.COOLDOWN, graphics);
		int y = HudPositions.getY(HudElement.COOLDOWN, graphics);
		int slotX = x;
		int shown = 0;

		for (Entry entry : entries) {
			if (shown >= 9) {
				break;
			}
			ItemStack stack = new ItemStack(entry.item);
			graphics.renderItem(stack, slotX, y);
			graphics.renderItemDecorations(mc.font, stack, slotX, y);
			String seconds = String.valueOf((entry.remainingTicks + 19) / 20);
			int tw = mc.font.width(seconds);
			graphics.drawString(mc.font, seconds, slotX + 8 - tw / 2, y + 11, 0xFFFFFFFF, true);
			slotX += 20;
			shown++;
		}
	}

	private record Entry(Item item, int remainingTicks, float percent) {
	}
}
