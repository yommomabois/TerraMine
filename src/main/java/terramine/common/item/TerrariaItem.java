package terramine.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import terramine.TerraMine;

import java.util.Collections;
import java.util.List;

public abstract class TerrariaItem extends Item {
	private boolean showTooltip = true;

	public TerrariaItem(Properties properties) {
		super(properties.stacksTo(1).tab(TerraMine.ITEM_GROUP).rarity(Rarity.RARE).fireResistant());
	}

	public TerrariaItem() {
		this(new Properties());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
		if (TerraMine.CONFIG.general.showTooltips && showTooltip) {
			appendTooltipDescription(tooltip, this.getDescriptionId() + ".tooltip");
		}
	}

	public void canShowTooltip(boolean canShow) {
		showTooltip = canShow;
	}

	public Component getREITooltip() {
		return Component.literal(Language.getInstance().getOrDefault(this.getDescriptionId() + ".tooltip").replace("\n", " ").replace("%%", "%"));
	}

	protected void appendTooltipDescription(List<Component> tooltip, String translKey) {
		String[] lines = String.format(Language.getInstance().getOrDefault(translKey), getTooltipDescriptionArguments().toArray()).split("\n");

		for (String line : lines) {
			tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
		}
	}

	protected List<String> getTooltipDescriptionArguments() {
		return Collections.emptyList();
	}
}
