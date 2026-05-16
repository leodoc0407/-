package plastfps.bust.client.hud;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import plastfps.bust.client.config.ClientConfig;

public final class PlastCreatorBadge {
	public static final String CREATOR_USERNAME = "plastfps";
	private static final String BADGE_TEXT = "Mod creator";

	private PlastCreatorBadge() {
	}

	public static boolean isEnabled() {
		return ClientConfig.isClientHud() && isMultiplayerServer();
	}

	public static boolean isMultiplayerServer() {
		Minecraft mc = Minecraft.getInstance();
		return mc.player != null && !mc.isLocalServer();
	}

	public static boolean isPlastfps(String name) {
		return name != null && CREATOR_USERNAME.equalsIgnoreCase(name);
	}

	public static MutableComponent badge() {
		return Component.literal(BADGE_TEXT).withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD);
	}

	/**
	 * Appends the creator badge after the server-formatted tab name so donate prefixes stay intact.
	 */
	public static Component appendToTabName(Component serverDisplayName) {
		if (serverDisplayName == null || serverDisplayName.getString().contains(BADGE_TEXT)) {
			return serverDisplayName;
		}
		return Component.empty()
			.append(serverDisplayName)
			.append(Component.literal(" "))
			.append(badge());
	}

	/**
	 * Inserts the badge immediately before the creator username in chat text.
	 */
	public static Component decorateChatMessage(Component message) {
		if (!isEnabled() || message == null || message.getString().contains(BADGE_TEXT)) {
			return message;
		}
		MutableComponent rebuilt = Component.empty();
		message.visit((style, text) -> {
			appendTextWithBadge(rebuilt, style, text);
			return java.util.Optional.empty();
		}, Style.EMPTY);
		return rebuilt;
	}

	private static void appendTextWithBadge(MutableComponent target, Style style, String text) {
		String lower = text.toLowerCase();
		String needle = CREATOR_USERNAME.toLowerCase();
		int searchFrom = 0;
		while (true) {
			int idx = lower.indexOf(needle, searchFrom);
			if (idx < 0) {
				if (searchFrom < text.length()) {
					target.append(Component.literal(text.substring(searchFrom)).withStyle(style));
				}
				break;
			}
			if (idx > searchFrom) {
				target.append(Component.literal(text.substring(searchFrom, idx)).withStyle(style));
			}
			target.append(badge());
			target.append(Component.literal(" ").withStyle(style));
			searchFrom = idx + needle.length();
		}
	}
}
