package plastfps.bust.client.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import plastfps.bust.client.config.ClientConfig;

public final class InventoryHelp {
	private static final int COLOR_MENDING_ARMOR = 0x9055FFAA;
	private static final int COLOR_GOLDEN_APPLE = 0x90FFD700;
	private static final int COLOR_ENCHANTED_GOLDEN_APPLE = 0x90CC66FF;
	private static final int COLOR_SWORD = 0x90FF5555;
	private static final int COLOR_GROUND = 0xB0FFE066;

	private InventoryHelp() {
	}

	public static int getSlotHighlightColor(ItemStack stack, Level level) {
		if (!ClientConfig.isInventoryHelp() || stack.isEmpty()) {
			return 0;
		}
		if (stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
			return COLOR_ENCHANTED_GOLDEN_APPLE;
		}
		if (stack.is(Items.GOLDEN_APPLE)) {
			return COLOR_GOLDEN_APPLE;
		}
		if (stack.is(ItemTags.SWORDS)) {
			return COLOR_SWORD;
		}
		if (isMendingArmor(stack, level)) {
			return COLOR_MENDING_ARMOR;
		}
		return 0;
	}

	public static int getGroundHighlightColor(ItemStack stack, Level level) {
		if (!ClientConfig.isInventoryHelp() || stack.isEmpty()) {
			return 0;
		}
		return getSlotHighlightColor(stack, level) != 0 ? COLOR_GROUND : 0;
	}

	private static boolean isMendingArmor(ItemStack stack, Level level) {
		if (level == null || !(stack.getItem() instanceof ArmorItem)) {
			return false;
		}
		return level.registryAccess()
			.lookup(Registries.ENCHANTMENT)
			.flatMap(registry -> registry.get(Enchantments.MENDING))
			.filter(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0)
			.isPresent();
	}
}
