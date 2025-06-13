package eu.macsworks.ogpoll.utils;

public class Utils {

	public static long parseTimeStr(String timeStr) {
		timeStr = timeStr.replace(" ", "");
		long dur = 0;

		StringBuilder inBuilding = new StringBuilder();
		for(int i = 0; i < timeStr.length(); i++) {
			char c = timeStr.charAt(i);
			if(Character.isDigit(c)) {
				inBuilding.append(c);
				continue;
			}

			if(!Character.isAlphabetic(c)){
				continue; //Junk character, we expect characters to only be alphabetic at this point
			}

			//Character is a letter at this point
			//inBuilding is for sure composed by digits only

			//If we haven't picked up any args, we dont need this letter. Throw it away.
			if(inBuilding.isEmpty()) continue;

			int parsedNum = Integer.parseInt(inBuilding.toString());
			dur += parsedNum * switch (Character.toLowerCase(c)){
				case 'd' -> 86400L;
				case 'h' -> 3600L;
				case 'm' -> 60L;
				default -> 1L;
			};

			inBuilding = new StringBuilder();
		}

		if(inBuilding.isEmpty()) return dur;

		int parsedNum = Integer.parseInt(inBuilding.toString());
		dur += parsedNum;
		return dur;
	}

}
