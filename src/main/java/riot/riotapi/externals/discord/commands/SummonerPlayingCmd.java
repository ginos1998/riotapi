package riot.riotapi.externals.discord.commands;

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
import riot.riotapi.controllers.api.MatchApiController;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.exceptions.DiscordException;
import riot.riotapi.externals.discord.utils.URLs;

import java.util.List;

@Component
public class SummonerPlayingCmd implements SlashCommand {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MatchApiController matchController;
    @Autowired
    public SummonerPlayingCmd(MatchApiController matchController) {
        this.matchController = matchController;
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

            Mono<MatchDTO> liveMatch = this.matchController.getSummonerLiveMatch(name);

            return liveMatch.flatMap(match -> {
                List<ParticipantInfoDTO> redTeamParticipants = match.getParticipants().stream()
                        .filter(participant -> participant.getTeamId() == 200)
                        .toList();

                List<ParticipantInfoDTO> blueTeamParticipants = match.getParticipants().stream()
                        .filter(participant -> participant.getTeamId() == 100)
                        .toList();

                return  event.reply()
                        .withEphemeral(false)
                        .withEmbeds(headerEmbed(name),
                                    createTeamEmbed(redTeamParticipants, "RED TEAM", Color.RED),
                                    createTeamEmbed(blueTeamParticipants, "BLUE TEAM", Color.BLUE));
            });

        } catch (DiscordException de) {
            logger.error("Ha ocurrido un error al ejecutar el comando ".concat(getName()));
            throw new DiscordException("Ha ocurrido un error al ejecutar el comando ".concat(getName()));
        }
    }

    private EmbedCreateSpec headerEmbed(String sumName) {

        return EmbedCreateSpec.builder()
                .color(Color.GRAY)
                .title("Live Match of ".concat(sumName))
                .author("More details", URLs.URL_POROFESOR_GG.concat(sumName), URLs.ICON_POROFESOR_GG)
                .description("LAS Match")
                .build();
    }

    private EmbedCreateSpec createTeamEmbed(List<ParticipantInfoDTO> teamParticipants, String teamName, Color teamColor) {
        StringBuilder summoners = new StringBuilder();
        StringBuilder champions = new StringBuilder();
        StringBuilder spells = new StringBuilder();

        for(ParticipantInfoDTO match: teamParticipants) {
            summoners.append(match.getSummonerName()).append('\n');
            champions.append(match.getChampionName()).append('\n');
            spells.append(match.getSpellName1()).append(" | ").append(match.getSpellName2()).append('\n');
        }
        return EmbedCreateSpec.builder()
                .color(teamColor)
                .title(teamName)
                .addField("Summoner", summoners.toString(), true)
                .addField("Champion", champions.toString(), true)
                .addField("Spells", spells.toString(), true)
                .build();
    }
}
