package plastfps.bust.client.mixin;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import plastfps.bust.client.hud.PlastCreatorBadge;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
	@ModifyVariable(
		method = {"addMessage(Lnet/minecraft/network/chat/Component;)V", "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"},
		at = @At("HEAD"),
		argsOnly = true,
		ordinal = 0
	)
	private Component plastfps$creatorChatBadge(Component message) {
		return PlastCreatorBadge.decorateChatMessage(message);
	}
}
