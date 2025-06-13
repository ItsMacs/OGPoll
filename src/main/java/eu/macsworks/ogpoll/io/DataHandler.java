package eu.macsworks.ogpoll.io;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.macsworks.ogpoll.OGPoll;
import eu.macsworks.ogpoll.objects.Poll;
import eu.macsworks.ogpoll.utils.ColorTranslator;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHandler {

	private final HashMap<String, String> lang = new HashMap<>();
	private final String mongoUser, mongoPassword, mongoServer;

	public DataHandler(){
		//Remove MongoDB stupid bloat logging
		Logger logger = Logger.getLogger("org.mongodb.driver");
		logger.setLevel(Level.SEVERE);

		OGPoll.getInstance().saveDefaultConfig();
		mongoUser = OGPoll.getInstance().getConfig().getString("mongo-user");
		mongoPassword = OGPoll.getInstance().getConfig().getString("mongo-password");
		mongoServer = OGPoll.getInstance().getConfig().getString("mongo-server");
	}


	//Loads (and creates, if not found) the lang.yml file from the plugin's data folder
	public void loadLang(){
		File file = new File(OGPoll.getInstance().getDataFolder() + "/lang.yml");
		if(!file.exists()){
			OGPoll.getInstance().saveResource("lang.yml", false);
		}

		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
		langConfig.getKeys(false).
				forEach(s -> lang.put(s, ColorTranslator.translate(langConfig.getString(s))));
	}

	public String getLang(String key){
		return lang.get(key);
	}


	public void loadFromMongoDB(){
		String uri = "mongodb+srv://" + mongoUser + ":" + mongoPassword + "@" + mongoServer + "/";
		try(MongoClient mongoClient = MongoClients.create(uri)){
			MongoDatabase db = mongoClient.getDatabase("ogpoll");

			MongoCollection<Document> polls = db.getCollection("polls");
			polls.find().forEach(d -> {
				HashMap<UUID, Integer> votes = new HashMap<>();

				//Parse poll votes as hashmaps have to be serialized
				d.getList("pollVotes", String.class).forEach(s -> {
					try{
						votes.put(UUID.fromString(s.split("%")[0]), Integer.parseInt(s.split("%")[1]));
					}catch (Exception e){
						Bukkit.getLogger().warning("Could not parse poll vote: " + s);
					}
				});

				Poll poll = new Poll(d.getString("pollID"),
						d.getLong("pollDuration"),
						d.getLong("pollAskingDate"),
						d.getString("pollQuestion"),
						d.getList("pollAnswers", String.class),
						votes);

				OGPoll.getInstance().getPollManager().addPoll(poll);
			});
		}catch (MongoException e){
			Bukkit.getLogger().warning("The MongoDB Server isn't reachable at the moment. Saving and Loading are not available.");
		}
	}

	public void saveToMongoDB(){
		String uri = "mongodb+srv://" + mongoUser + ":" + mongoPassword + "@" + mongoServer + "/";
		try(MongoClient mongoClient = MongoClients.create(uri)){
			MongoDatabase db = mongoClient.getDatabase("ogpoll");
			MongoCollection<Document> polls = db.getCollection("polls");
			polls.drop(); //Drop the collection to replace all polls - quickest way to do this instead of replacing
			polls = db.getCollection("polls"); //Re-create the polls collection

			List<Document> serializedPolls = new ArrayList<>();
			OGPoll.getInstance().getPollManager().getAllPolls().forEach(poll -> {
				//Serialize poll votes as a list of strings
				List<String> votes = new ArrayList<>();
				poll.getVoters().forEach((uuid, answerID) -> votes.add(uuid.toString() + "%" + answerID));
				serializedPolls.add(new Document("pollID", poll.getId())
						.append("pollQuestion", poll.getQuestion())
						.append("pollVotes", votes)
						.append("pollAskingDate", poll.getAskDate())
						.append("pollAnswers", poll.getAnswers())
						.append("pollDuration", poll.getDuration()));
			});

			if(serializedPolls.isEmpty()) return;

			polls.insertMany(serializedPolls);
		}catch (MongoException e){
			Bukkit.getLogger().warning("The MongoDB Server isn't reachable at the moment. Saving and Loading are not available.");
		}
	}

}
