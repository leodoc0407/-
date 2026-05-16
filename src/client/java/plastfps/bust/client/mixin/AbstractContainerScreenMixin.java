package plastfps.bust.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plastfps.bust.client.feature.InventoryHelp;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
	@Shadow
	protected int leftPos;

	@Shadow
	protected int topPos;

	@Inject(method = "renderSlot", at = @At("HEAD"))
	private void plastfps$highlightSlot(GuiGraphics graphics, Slot slot, CallbackInfo ci) {
		ItemStack stack = slot.getItem();
		if (stack.isEmpty()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		int color = InventoryHelp.getSlotHighlightColor(stack, mc.level);
		if (color == 0) {
			return;
		}
		int x = this.leftPos + slot.x;
		int y = this.topPos + slot.y;
		graphics.fill(x, y, x + 16, y + 16, color);
	}
}
