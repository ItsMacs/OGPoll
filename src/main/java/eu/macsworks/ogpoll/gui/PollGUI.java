package eu.macsworks.ogpoll.gui;

import eu.macsworks.ogpoll.gui.items.LoredItem;
import eu.macsworks.ogpoll.objects.Poll;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.stream.Collectors;

public class PollGUI {

	public static void show(Poll poll, Player player){
		Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));

		List<Item> items = poll.getAnswers().stream().map(s -> new LoredItem(Material.PAPER, s, "", click -> {
			poll.playerVoted(player, poll.getAnswers().indexOf(s)); //Register vote and close inventory
			player.closeInventory();
		})).collect(Collectors.toUnmodifiableList());

		Gui gui = PagedGui.items()
				.setStructure(
						"# # # # # # # # #",
						"# . . . . . . . #",
						"# . . . . . . . #",
						"# # # # # # # # #")
				.addIngredient('#', border)
				.addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
				.setContent(items)
				.build();

		Window.single()
				.setGui(gui)
				.open(player);
	}

}
