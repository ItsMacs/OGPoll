package eu.macsworks.ogpoll.gui.items;

import eu.macsworks.ogpoll.utils.ColorTranslator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Arrays;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class LoredItem extends AbstractItem {

	private final Material mat;
	private final String name, lore;
	private final Consumer<Click> clickHandler;


	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(mat).setDisplayName(ColorTranslator.translate(name)).setLegacyLore(Arrays.asList(ColorTranslator.translate(lore).split("\n")));
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		Click c = new Click(inventoryClickEvent);
		clickHandler.accept(c);
	}
}
