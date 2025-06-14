package eu.macsworks.ogpoll.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class ColorTranslator {

	public static String translate(String str) {
		final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");

		Matcher matcher = hexPattern.matcher(str);
		StringBuilder buffer = new StringBuilder(str.length() + 4 * 8);

		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer,
					COLOR_CHAR + "x"
							+ COLOR_CHAR + group.charAt(0)
							+ COLOR_CHAR + group.charAt(1)
							+ COLOR_CHAR + group.charAt(2)
							+ COLOR_CHAR + group.charAt(3)
							+ COLOR_CHAR + group.charAt(4)
							+ COLOR_CHAR + group.charAt(5)
			);
		}
		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}

}