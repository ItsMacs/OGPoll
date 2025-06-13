package eu.macsworks.ogpoll.objects;

import eu.macsworks.ogpoll.OGPoll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Poll {

	private final String id;
	private final long duration;
	private final long askDate;
	private final String question;

	private List<String> answers = new ArrayList<>();

	private HashMap<UUID, Integer> voters = new HashMap<>();

	public boolean hasPlayerVoted(Player player){
		return voters.containsKey(player.getUniqueId());
	}

	public String getPlayerAnswer(Player player){
		return answers.get(voters.get(player.getUniqueId()));
	}

	public void playerVoted(Player player, int answer){
		voters.put(player.getUniqueId(), answer);
	}

	public String getLore(){
		StringBuilder sb = new StringBuilder();
		sb.append(OGPoll.getInstance().getDataHandler().getLang("ask-date") + new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(new Date(askDate)));
		sb.append(OGPoll.getInstance().getDataHandler().getLang("ends-in") + new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(new Date((askDate + duration) - System.currentTimeMillis())));
		for(String answer : answers){
			int votesForThisAnswer = (int) voters.values().stream().filter(i -> i == answers.indexOf(answer)).count();

			double percentage = ((double)votesForThisAnswer / (double)voters.size()) * 100D;
			sb.append("\n").append("&e" + answer + "&f: &c" + String.format("%.2f", percentage) + "%");
		}
		return sb.toString();
	}

}
