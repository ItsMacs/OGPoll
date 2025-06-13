package eu.macsworks.ogpoll.gui.items;

import eu.macsworks.ogpoll.utils.ColorTranslator;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LiveItem extends LoredItem{

	private final Supplier<String> loreSupplier;

	public LiveItem(Material mat, String name, Consumer<Click> clickHandler, Supplier<String> loreSupplier) {
		super(mat, name, "", clickHandler);

		this.loreSupplier = loreSupplier;
	}

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(super.getMat()).setDisplayName(ColorTranslator.translate(super.getName())).setLegacyLore(Arrays.asList(ColorTranslator.translate(loreSupplier.get()).split("\n")));
	}
}
