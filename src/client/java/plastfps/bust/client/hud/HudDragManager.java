package plastfps.bust.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import plastfps.bust.client.config.ClientConfig;

public final class HudDragManager {
	private static HudElement dragging;
	private static int dragOffsetX;
	private static int dragOffsetY;

	private HudDragManager() {
	}

	public static boolean isEditMode() {
		Minecraft mc = Minecraft.getInstance();
		return mc.screen instanceof ChatScreen;
	}

	public static boolean handleClick(double mouseX, double mouseY, int button) {
		if (!isEditMode() || button != 0) {
			return false;
		}
		HudElement hit = hitTest((int) mouseX, (int) mouseY);
		if (hit == null) {
			dragging = null;
			return false;
		}
		dragging = hit;
		var window = Minecraft.getInstance().getWindow();
		int sw = window.getGuiScaledWidth();
		int sh = window.getGuiScaledHeight();
		dragOffsetX = (int) mouseX - HudPositions.getX(hit, sw, sh);
		dragOffsetY = (int) mouseY - HudPositions.getY(hit, sw, sh);
		return true;
	}

	public static void handleDrag(double mouseX, double mouseY) {
		if (dragging == null) {
			return;
		}
		var window = Minecraft.getInstance().getWindow();
		int x = (int) mouseX - dragOffsetX;
		int y = (int) mouseY - dragOffsetY;
		HudPositions.clampToScreen(dragging, x, y, window.getGuiScaledWidth(), window.getGuiScaledHeight());
	}

	public static void handleRelease() {
		dragging = null;
	}

	public static HudElement getDragging() {
		return dragging;
	}

	public static void renderEditOverlay(net.minecraft.client.gui.GuiGraphics graphics) {
		if (!isEditMode()) {
			return;
		}
		for (HudElement element : enabledElements()) {
			int x = HudPositions.getX(element, graphics);
			int y = HudPositions.getY(element, graphics);
			int w = element.defaultWidth();
			int h = element.defaultHeight();
			boolean active = element == dragging;
			int border = active ? 0xFFFFD060 : 0x80FFFFFF;
			graphics.fill(x - 1, y - 1, x + w + 1, y, border);
			graphics.fill(x - 1, y + h, x + w + 1, y + h + 1, border);
			graphics.fill(x - 1, y, x, y + h, border);
			graphics.fill(x + w, y, x + w + 1, y + h, border);
		}
	}

	private static HudElement[] enabledElements() {
		java.util.ArrayList<HudElement> list = new java.util.ArrayList<>();
		if (ClientConfig.isArmourHud()) {
			list.add(HudElement.ARMOUR);
		}
		if (ClientConfig.isPotionHud()) {
			list.add(HudElement.POTION);
		}
		if (ClientConfig.isInventoryHud()) {
			list.add(HudElement.INVENTORY);
		}
		if (ClientConfig.isCooldownHud()) {
			list.add(HudElement.COOLDOWN);
		}
		return list.toArray(HudElement[]::new);
	}

	private static HudElement hitTest(int mouseX, int mouseY) {
		var window = Minecraft.getInstance().getWindow();
		int sw = window.getGuiScaledWidth();
		int sh = window.getGuiScaledHeight();
		for (HudElement element : enabledElements()) {
			int x = HudPositions.getX(element, sw, sh);
			int y = HudPositions.getY(element, sw, sh);
			int w = element.defaultWidth();
			int h = element.defaultHeight();
			if (mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h) {
				return element;
			}
		}
		return null;
	}
}
