package plastfps.bust.client.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import plastfps.bust.client.hud.PlastCreatorBadge;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
	@Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
	private void plastfps$creatorTabBadge(PlayerInfo playerInfo, CallbackInfoReturnable<Component> cir) {
		if (!PlastCreatorBadge.isEnabled()) {
			return;
		}
		if (!PlastCreatorBadge.isPlastfps(playerInfo.getProfile().getName())) {
			return;
		}
		cir.setReturnValue(PlastCreatorBadge.appendToTabName(cir.getReturnValue()));
	}
}
