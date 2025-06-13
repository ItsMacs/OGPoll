package eu.macsworks.ogpoll.gui;

import eu.macsworks.ogpoll.OGPoll;
import eu.macsworks.ogpoll.gui.items.BackItem;
import eu.macsworks.ogpoll.gui.items.ForwardItem;
import eu.macsworks.ogpoll.gui.items.LiveItem;
import eu.macsworks.ogpoll.gui.items.LoredItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PollListGUI {

	public static void open(Player player){
		Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));

		int[] i = new int[]{0};
		List<Item> items = OGPoll.getInstance().getPollManager().getAllPolls()
				.stream()
				.map(poll -> {
					i[0] = i[0] + 1;
					if(i[0] == 2) i[0] = 0;

					Material mat = i[0] == 0 ? Material.GREEN_CONCRETE_POWDER : Material.LIME_CONCRETE_POWDER;
					if(poll.hasPlayerVoted(player)){
						mat = Material.GRAY_CONCRETE_POWDER;
					}
					return new LiveItem(mat, //Cycle between those two materials to create a checkerboard
							poll.getQuestion(), //Name
							p -> {
								if(poll.hasPlayerVoted(player)){
									player.sendMessage(OGPoll.getInstance().getDataHandler().getLang("already-voted"));
									return;
								}

								PollGUI.show(poll, player);
							}, //Click handler
							poll::getLore); //Live lore supplier
				})
				.collect(Collectors.toList());


		Gui gui = PagedGui.items()
				.setStructure(
						"# # # # # # # # #",
						"# x x x x x x x #",
						"# x x x x x x x #",
						"# # # < # > # # #")
				.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
				.addIngredient('#', border)
				.addIngredient('<', new BackItem())
				.addIngredient('>', new ForwardItem())
				.setContent(items)
				.build();

		Window.single()
				.setTitle(OGPoll.getInstance().getDataHandler().getLang("poll-list-title"))
				.setGui(gui)
				.open(player);
	}

}
