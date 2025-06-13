package eu.macsworks.ogpoll.utils;

import eu.macsworks.ogpoll.OGPoll;
import eu.macsworks.ogpoll.objects.Poll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

	private ServerMock server;
	private OGPoll plugin;

	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock();
		plugin = MockBukkit.load(OGPoll.class);
	}

	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}

	@Test
	public void crudPollOperations() throws InterruptedException {
		Poll poll = new Poll("test", 600, System.currentTimeMillis(), "Is this test working?", Arrays.asList("Yes", "Oh yes!"), new HashMap<>(Map.of(UUID.randomUUID(), 0)));

		plugin.getPollManager().addPoll(poll);
		plugin.getDataHandler().saveToMongoDB();

		Thread.sleep(2000); //Wait for IO

		plugin.getDataHandler().loadFromMongoDB();

		Thread.sleep(2000); //Wait for IO

		Optional<Poll> retrievedPoll = plugin.getPollManager().getPoll(poll.getId());
		if(retrievedPoll.isEmpty()){
			fail("Poll was mishandled");
			return;
		}

		assertEquals(poll, retrievedPoll.get());
	}

	@Test
	public void pollCommand(){
		PlayerMock player = server.addPlayer();
		player.performCommand("/poll create asd 100"); //Should fail because of no perm

		player.setOp(true);
		player.performCommand("/poll create asd 100"); //Should now create a poll
	}

	@Test
	void parseTimeStr() {
		long parsedTimeImproper = Utils.parseTimeStr("10:30");
		long parsedTimeProper = Utils.parseTimeStr("10m30s");
		long parsedTimeNoTrailingS = Utils.parseTimeStr("10m30");
		assertEquals(parsedTimeProper, parsedTimeNoTrailingS);
		System.out.println(parsedTimeImproper);
	}


}