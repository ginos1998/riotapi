package riot.riotapi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordUtils {

    public static String formatDiscordEmoji(String emojiName, String emojiId) {
        if (emojiName == null || emojiId == null) {
            return String.format("<:%s:%s>", "lot", "1145530242090414250");
        }
        return String.format("<:%s:%s>", emojiName, emojiId);
    }

    public static String decodeEmoji(String emoji) {
        Pattern pattern = Pattern.compile("<:(\\w+):(\\d+)>");
        Matcher matcher = pattern.matcher(emoji);
        return matcher.matches() ? matcher.group(1) : emoji;
    }
}
