package riot.riotapi.externals.discord.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import riot.riotapi.externals.discord.utils.URLs;

@Component
public class AboutCommand implements SlashCommand {
    @Override
    public String getName() {
        return "about";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        return  event.reply()
                .withEphemeral(true)
                .withEmbeds(aboutEmbed());
    }

    private EmbedCreateSpec aboutEmbed() {

        return EmbedCreateSpec.builder()
                .color(Color.GRAY)
                .author("Author: ginos1998", URLs.URL_LoT_REPO, URLs.ICON_LoT_BOT)
                .title("About League of Trolls")
                .description("> This BOT is about League Of Legends matches, stats, and more.")
                .footer("Beta version.", URLs.ICON_LoT_BOT)
                .build();
    }
}
