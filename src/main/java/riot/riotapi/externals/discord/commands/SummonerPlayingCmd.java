package riot.riotapi.externals.discord.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import riot.riotapi.controllers.api.MatchApiController;
import riot.riotapi.controllers.discord.GuildEmojiController;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.exceptions.DiscordException;
import riot.riotapi.externals.discord.utils.URLs;

import java.time.Duration;
import java.util.List;

@Component
public class SummonerPlayingCmd implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MatchApiController matchController;
    private final GuildEmojiController guildEmojiController;
    @Autowired
    public SummonerPlayingCmd(MatchApiController matchController, GuildEmojiController guildEmojiController) {
        this.matchController = matchController;
        this.guildEmojiController = guildEmojiController;
    }

    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        try {

            String name = event.getOption("name")
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElseThrow();

            Mono<MatchDTO> liveMatchMono = event.getInteraction()
                    .getGuild()
                    .flatMap(guild -> {
                                Snowflake guildId = guild.getId();
                                return this.matchController.getSummonerLiveMatch(name, guildId.asString());
                            });

            return liveMatchMono.flatMap(match -> {
                        logger.debug("Start creating embed message.");
                        List<ParticipantInfoDTO> redTeamParticipants = match.getParticipants().stream()
                                .filter(participant -> participant.getTeamId() == 200)
                                .toList();

                        List<ParticipantInfoDTO> blueTeamParticipants = match.getParticipants().stream()
                                .filter(participant -> participant.getTeamId() == 100)
                                .toList();

                        String mode = match.getMode();

                        return  event.reply()
                                .withEphemeral(false)
                                .withEmbeds(headerEmbed(name, mode),
                                            createTeamEmbed(redTeamParticipants, Color.RED),
                                            createTeamEmbed(blueTeamParticipants, Color.BLUE))
                                .timeout(Duration.ofSeconds(5))
                                .then(Mono.defer(() -> deleteEmojisAll(event.getInteraction().getGuildId().orElseThrow().asString())))
                                .onErrorResume(err -> {
                                    logger.error("ups:" + err.getMessage());
                                    return Mono.empty();
                                });
                    })
                    .timeout(Duration.ofSeconds(8))
                    .onErrorResume(err -> {
                        logger.error(err.getMessage());
                        return Mono.empty();
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        logger.debug("No live match found. Returning default embed.");
                        return event.reply()
                                                            .withEphemeral(false)
                                                            .withEmbeds(notPlayingEmbed(name))
                                                            .then();
                    }
                    ));
//                    .publishOn(Schedulers.boundedElastic())
//                    .doAfterTerminate(() -> event.getInteraction()
//                            .getGuild().map(g -> deleteEmojisAll(g.getId().asString()))
//                            .block() // the embed message has been returned so blocking don't affect.
//                    );

        } catch (DiscordException de) {
            logger.error("Ha ocurrido un error al ejecutar el comando ".concat(getName()));
            throw new DiscordException("Ha ocurrido un error al ejecutar el comando ".concat(getName()));
        }
    }

    private Mono<Void> deleteEmojisAll(String gid) {
        logger.info("Deleting all emojis on guild " + gid);
        return this.guildEmojiController.deleteEmojiAll(gid)
                .onErrorComplete();
    }

    private EmbedCreateSpec headerEmbed(String sumName, String mode) {

        return EmbedCreateSpec.builder()
                .color(Color.GRAY)
                .title("Live Match of ".concat(sumName))
                .author("More details", URLs.URL_POROFESOR_GG.concat(sumName), URLs.ICON_POROFESOR_GG)
                .thumbnail(URLs.ICON_LoT_BOT)
                .description(String.format("> LAS Match %s", mode))
                .build();
    }

    private EmbedCreateSpec createTeamEmbed(List<ParticipantInfoDTO> teamParticipants, Color teamColor) {
        StringBuilder summoners = new StringBuilder();
        StringBuilder champions = new StringBuilder();
        StringBuilder spells = new StringBuilder();
        int line = 0;
        String startWith = teamColor.equals(Color.RED) ? "```diff\n" : "```fix\n";
        for(ParticipantInfoDTO match: teamParticipants) {
            String quote = ((line) %2 == 0) ? "- " : " ";
            String lineColor = ((line++) %2 == 0) ? startWith : "```md\n";
//            champions.append(lineColor).append(quote).append(match.getChampionName()).append("\n ").append("\n```");
            champions.append(match.getChampionEmoji()).append(" ").append(match.getChampionName()).append("\nMadera").append('\n');
//            summoners.append(lineColor).append(quote).append(match.getSummonerName()).append('\n').append(match.getSummonerLevel()).append('\n').append("```");
            summoners.append(match.getSummonerName()).append("\nLvl ").append(match.getSummonerLevel()).append('\n');
//            spells.append('\n').append(match.getSpell1Emoji()).append("  ").append(match.getSpell2Emoji()).append("\n\n");
            spells.append(match.getSpell1Emoji()).append("  ").append(match.getSpell2Emoji()).append("\n zzz \n");
        }
        return EmbedCreateSpec.builder()
                .color(teamColor)
                .addField("Champion", champions.toString(), true)
                .addField("Summoner", summoners.toString(), true)
                .addField("Spells", spells.toString(), true)
                .build();
    }

    private EmbedCreateSpec notPlayingEmbed(String sumName) {
        return EmbedCreateSpec.builder()
                .color(Color.YELLOW)
                .author("League Of Trolls", URLs.URL_LoT_REPO , URLs.ICON_LoT_BOT)
                .title("UPS! Not found")
                .description("The summoner ".concat(sumName).concat(" is not playing any match :face_with_monocle:"))
                .footer("If you think this is an error, type /about to get support info.", URLs.ICON_THINKING_EMOJI)
                .build();
    }
}
