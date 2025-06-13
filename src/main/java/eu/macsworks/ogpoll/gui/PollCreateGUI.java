package eu.macsworks.ogpoll.gui;

import eu.macsworks.ogpoll.OGPoll;
import eu.macsworks.ogpoll.gui.items.LoredItem;
import eu.macsworks.ogpoll.objects.Poll;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PollCreateGUI {

	public static void open(Player sender, Poll poll){
		Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));

		List<Item> items = poll.getAnswers().stream().map(s -> new LoredItem(Material.PAPER, s, OGPoll.getInstance().getDataHandler().getLang("edit-answer"), click -> {
			if(click.getClickType() == ClickType.LEFT){
				new AnvilGUI.Builder()
						.plugin(OGPoll.getInstance())
						.title("Enter the new option")
						.itemOutput(new ItemStack(Material.PAPER))
						.onClick((i, state) -> {
							int prevIndex = poll.getAnswers().indexOf(s);
							poll.getAnswers().remove(s);
							poll.getAnswers().add(prevIndex, state.getText());
							PollCreateGUI.open(sender, poll);
							return Collections.singletonList(AnvilGUI.ResponseAction.close());
						})
						.open(sender);
				return;
			}

			poll.getAnswers().remove(s);
			PollCreateGUI.open(sender, poll);
		})).collect(Collectors.toUnmodifiableList());

		Item addQuestion = new LoredItem(Material.LIME_DYE, OGPoll.getInstance().getDataHandler().getLang("add-question"), "", click -> {
			new AnvilGUI.Builder()
					.plugin(OGPoll.getInstance())
					.title("Enter the new option")
					.itemOutput(new ItemStack(Material.PAPER))
					.onClick((i, state) -> {
						poll.getAnswers().add(state.getText());
						PollCreateGUI.open(sender, poll);
						return Collections.singletonList(AnvilGUI.ResponseAction.close());
					})
					.open(sender);
			return;
		});

		Gui gui = PagedGui.items()
				.setStructure(
						"# # # # # # # # #",
						"# . . . . . . . #",
						"# . . . . . . . #",
						"# # # # # # # # N")
				.addIngredient('#', border)
				.addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
				.addIngredient('N', addQuestion)
				.setContent(items)
				.build();

		Window.single()
				.setGui(gui)
				.open(sender);
	}

}
