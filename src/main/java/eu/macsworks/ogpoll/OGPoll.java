package eu.macsworks.ogpoll;

import co.aikar.commands.BukkitCommandManager;
import eu.macsworks.ogpoll.commands.PollCommands;
import eu.macsworks.ogpoll.io.DataHandler;
import eu.macsworks.ogpoll.objects.PollManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OGPoll extends JavaPlugin {

	@Getter
	private static OGPoll instance = null;

	@Getter
	private PollManager pollManager;

	@Getter
	private DataHandler dataHandler;

	private static void setInstance(OGPoll instance) {
		OGPoll.instance = instance;
	}

	@Override
	public void onEnable() {
		setInstance(this);

		pollManager = new PollManager();
		dataHandler = new DataHandler();

		dataHandler.loadLang();

		initCommands();
		initData();

		Bukkit.getLogger().info("OGPoll by Alice has been initialized successfully!");
	}

	private void initData() {
		//Load data from db
		Bukkit.getScheduler().runTaskAsynchronously(OGPoll.getInstance(), () -> {
			dataHandler.loadFromMongoDB();
		});

		//Save data every 10s as a backup
		Bukkit.getScheduler().runTaskTimerAsynchronously(OGPoll.getInstance(), () -> {
			dataHandler.saveToMongoDB();
		}, 0L, 200L);
	}

	private void initCommands() {
		BukkitCommandManager manager = new BukkitCommandManager(OGPoll.getInstance());
		manager.registerCommand(new PollCommands());
	}

	@Override
	public void onDisable() {
		//Blocking operation which should take no longer than 20ms (provided DB is reachable) - not long enough to be a concern
		dataHandler.saveToMongoDB();

		Bukkit.getLogger().info("OGPoll by Alice has been disabled successfully!");
	}
}
