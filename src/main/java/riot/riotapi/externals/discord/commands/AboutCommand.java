package riot.riotapi.externals.discord.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import riot.riotapi.externals.discord.utils.URLs;

@Component
public class AboutCommand implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(AboutCommand.class);
    private final String EMOJI_LOT = "lot";

    @Override
    public String getName() {
        return "about";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.getInteraction()
                .getGuild()
                .flatMap(guild -> {
                    Snowflake guildId = guild.getId();
                    return event.getClient()
                                .getGuildById(Snowflake.of(guildId.asString()))
                                .flatMap(g -> g.getEmojis()
                                                .collectList()
                                                .flatMap(emojis -> {
                                                    if (!emojis.isEmpty()) {
                                                        GuildEmoji emoji = emojis.stream()
                                                                .filter(e -> e.getName().equals(EMOJI_LOT))
                                                                .findFirst()
                                                                .orElse(null);
                                                        assert emoji != null;
                                                        return event.reply()
                                                                .withEphemeral(true)
                                                                .withEmbeds(aboutEmbed(emoji));
                                                    } else {
                                                        logger.info("No emojis found.");
                                                        return event.reply("No emojis found.");
                                                    }
                                                })
                                );
                });
    }

    private EmbedCreateSpec aboutEmbed(GuildEmoji emoji) {
        String emojiToDs = String.format("<:%s:%s>", emoji.getName(), emoji.getId().asString());
        return EmbedCreateSpec.builder()
                .color(Color.GRAY)
                .author("Author: ginos1998", URLs.URL_LoT_REPO, URLs.ICON_LoT_BOT)
                .title("About League of Trolls")
                .description("> This BOT is about League Of Legends matches, stats, and more. " + emojiToDs)
                .footer("Beta version.", URLs.ICON_LoT_BOT)
                .build();
    }
}
