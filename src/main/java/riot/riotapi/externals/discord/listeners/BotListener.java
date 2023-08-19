package riot.riotapi.externals.discord.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;

    Message message = event.getMessage();
    String content = message.getContentRaw();
    if (content.equals(".ping")) {
      String arg1 = "boca";
      String arg2 = "riBAR";
      String tableMarkdown = String.format("```\n" +
          "| Encabezado 1   | Encabezado 2 | Encabezado 3 |\n" +
          "|----------------|--------------|--------------|\n" +
          "| %-12s | Celda 1,2    | Celda 1,3    |\n" +
          "| Celda 2,1      | \\uD83D\\uDC99    | Celda 2,3    |\n" +
          "| Celda 3,1      | %-12s | Celda 3,3    |\n" +
          "```" +
          "\n :soccer: boca" +
          "\n <h3 id=\"custom-id\">My Great Heading</h3> " +
          "\n```CSS\n" +
          "Hello\n" +
          "```" +
          "\n```fix\n" +
          "I love linuxhint :smile: \n" +
          "```" +
          "\n", arg1, arg2).concat("[sett](/resources/static/sett.jpeg)");
      MessageChannel channel = event.getChannel();
      channel.sendMessage(tableMarkdown).queue();
    }
  }
}

