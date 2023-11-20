package riot.riotapi.externals.discord.commands;

import discord4j.core.spec.EmbedCreateSpec;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
public abstract class Command {
  protected final Logger logger = LoggerFactory.getLogger(Command.class);
  protected final Boolean inline = true;
  protected final String whiteSpace = "\u200B";
  protected final String space = " ";
  protected final String breakLine = "\n";

  public abstract EmbedCreateSpec defaultEmbed(String arg);

}
