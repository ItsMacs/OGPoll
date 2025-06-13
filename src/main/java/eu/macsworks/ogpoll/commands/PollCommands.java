package eu.macsworks.ogpoll.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import eu.macsworks.ogpoll.OGPoll;
import eu.macsworks.ogpoll.gui.PollCreateGUI;
import eu.macsworks.ogpoll.gui.PollListGUI;
import eu.macsworks.ogpoll.objects.Poll;
import eu.macsworks.ogpoll.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PollCommands extends BaseCommand {

	@Default
	@CommandAlias("poll")
	public void pollMenu(Player sender){
		PollListGUI.open(sender);
	}

	@CommandAlias("poll create|new|cr")
	@CommandPermission("poll.admin")
	public void pollCreate(Player sender, String duration, String... question){
		//Build the question from varargs
		StringBuilder composedQuestion = new StringBuilder();
		for(String q : question){
			composedQuestion.append(q).append(" ");
		}

		//Parse duration
		long pollDuration = Utils.parseTimeStr(duration);

		//Inserting new poll into map - no duplicate check, although it'd be useless, the probability of two truncaed UUIDs overlapping are tiny.
		Poll poll = new Poll(UUID.randomUUID().toString().substring(0, 4), pollDuration, System.currentTimeMillis(), composedQuestion.toString());
		OGPoll.getInstance().getPollManager().addPoll(poll);

		sender.sendMessage(OGPoll.getInstance().getDataHandler().getLang("poll-created"));
		PollCreateGUI.open(sender, poll);
	}

	@CommandAlias("poll delete|remove|rem|del")
	@CommandPermission("poll.admin")
	public void pollDelete(Player sender, String pollID){
		Optional<Poll> poll = OGPoll.getInstance().getPollManager().getPoll(pollID);
		if(poll.isEmpty()){
			sender.sendMessage(OGPoll.getInstance().getDataHandler().getLang("poll-not-found"));
			return;
		}

		OGPoll.getInstance().getPollManager().removePoll(poll.get());
		sender.sendMessage(OGPoll.getInstance().getDataHandler().getLang("poll-removed"));
	}

}
