package eu.macsworks.ogpoll.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PollManager {

	private final HashMap<String, Poll> polls = new HashMap<>();

	public List<Poll> getAllPolls(){
		return new ArrayList<>(polls.values());
	}

	public void addPoll(Poll poll) {
		polls.put(poll.getId(), poll);
	}

	public Optional<Poll> getPoll(String id) {
		return Optional.ofNullable(polls.get(id));
	}

	public void removePoll(String id) {
		polls.remove(id);
	}

	public void removePoll(Poll poll) {
		polls.remove(poll.getId());
	}

}
