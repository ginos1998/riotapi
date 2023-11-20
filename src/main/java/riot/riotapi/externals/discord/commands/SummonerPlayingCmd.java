package riot.riotapi.externals.discord.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import riot.riotapi.controllers.api.MatchApiController;
import riot.riotapi.dtos.match.MatchDTO;
import riot.riotapi.dtos.match.ParticipantInfoDTO;
import riot.riotapi.externals.discord.utils.URLs;

import java.time.Duration;
import java.util.List;

@Component
@EqualsAndHashCode(callSuper = true)
public class SummonerPlayingCmd extends Command implements SlashCommand {

  private final MatchApiController matchController;

  @Autowired
  public SummonerPlayingCmd(MatchApiController matchController) {
    super();
    this.matchController = matchController;
  }

  @Override
  public String getName() {
    return "partida";
  }

  @Override
  public Mono<Void> handle(ChatInputInteractionEvent event) {
    try {

      String name = event.getOption("invocador")
          .flatMap(ApplicationCommandInteractionOption::getValue)
          .map(ApplicationCommandInteractionOptionValue::asString)
          .orElseThrow();

      Mono<MatchDTO> liveMatchMono = this.matchController.getSummonerLiveMatch(name);

      return liveMatchMono.flatMap(match -> {
            String mode = match.getMode();
            return event.reply()
                .withEphemeral(false)
                .withEmbeds(summonerMatchEmbed(match.getParticipants(), name, mode))
                .timeout(Duration.ofSeconds(8))
                .onErrorResume(err -> {
                  logger.error("An error has occurred replying to /partida command: {}", err.getMessage());
                  return event.reply()
                      .withEphemeral(false)
                      .withEmbeds(defaultEmbed(name))
                      .then();
                });
          })
          .timeout(Duration.ofSeconds(10))
          .onErrorResume(err -> {
            logger.error("Error on liveMatchMono.flatMap: {}", err.getMessage());
            return event.reply()
                .withEphemeral(false)
                .withEmbeds(defaultEmbed(name))
                .then();
          })
          .switchIfEmpty(Mono.defer(() -> {
                logger.debug("No live match found. Returning default embed.");
                return event.reply()
                    .withEphemeral(false)
                    .withEmbeds(defaultEmbed(name))
                    .then();
              }
          ));

    } catch (Exception ce) {
      logger.error("An error has occurred in /partida command handler with name {}. Error: {} ", SummonerPlayingCmd.class.getName(), ce.getMessage());
      return Mono.empty();
    }
  }

  private EmbedCreateSpec summonerMatchEmbed(List<ParticipantInfoDTO> teamParticipants, String sumName, String mode) {

    List<ParticipantInfoDTO> redTeamParticipants = teamParticipants.stream()
        .filter(participant -> participant.getTeamId() == 200)
        .toList();

    List<ParticipantInfoDTO> blueTeamParticipants = teamParticipants.stream()
        .filter(participant -> participant.getTeamId() == 100)
        .toList();

    EmbedCreateFields.Field[] redTeamFields = buildTeamEmbed(redTeamParticipants, "ROJO");
    EmbedCreateFields.Field[] blueTeamFields = buildTeamEmbed(blueTeamParticipants, "AZUL");

    return EmbedCreateSpec.builder()
        .color(Color.VIVID_VIOLET)
        .title("Partida en juego de ".concat(sumName))
        .author("Mas detalles", URLs.URL_POROFESOR_GG.concat(sumName), URLs.ICON_POROFESOR_GG)
        .thumbnail(URLs.ICON_LoT_BOT)
        .description(String.format("Region **LAS** \nPartida: **%s**", mode))
        .addField(whiteSpace, whiteSpace, !inline)
        .addFields(redTeamFields)
        .addField(whiteSpace, whiteSpace, !inline)
        .addFields(blueTeamFields)
        .build();
  }

  private EmbedCreateFields.Field[] buildTeamEmbed(List<ParticipantInfoDTO> teamParticipants, String team) {
    String circle = team.equals("ROJO") ? ":red_circle:" : ":blue_circle:";
    String tableTittle = circle.concat(space).concat("EQUIPO " + team).concat(space).concat(circle);
    StringBuilder summoners = new StringBuilder("**Invocador**".concat(breakLine));
    StringBuilder champions = new StringBuilder("**Campeón**".concat(breakLine));
    StringBuilder spells = new StringBuilder("**Hechizos**".concat(breakLine));

    for (ParticipantInfoDTO match : teamParticipants) {
      champions.append(match.getChampionEmoji()).append(space).append(match.getChampionName()).append(breakLine);
      summoners.append(match.getSummonerName()).append(" - ").append("**").append(match.getSummonerLevel()).append("**").append(breakLine);
      spells.append(match.getSpell1Emoji()).append("  ").append(match.getSpell2Emoji()).append(breakLine);
    }

    EmbedCreateFields.Field championField = EmbedCreateFields.Field.of(whiteSpace, champions.toString(), true);
    EmbedCreateFields.Field summonerField = EmbedCreateFields.Field.of(tableTittle, summoners.toString(), true);
    EmbedCreateFields.Field spellField = EmbedCreateFields.Field.of(whiteSpace, spells.toString(), true);

    return new EmbedCreateFields.Field[]{championField, summonerField, spellField};
  }

  @Override
  public EmbedCreateSpec defaultEmbed(String sumName) {
    return EmbedCreateSpec.builder()
        .color(Color.YELLOW)
        .author("League Of Trolls", URLs.URL_LoT_REPO, URLs.ICON_LoT_BOT)
        .title("UPS! Algo fallo")
        .description("El invocador ".concat(sumName).concat(" no esta jugando una partida :face_with_monocle:.\n Por favor, intentá de nuevo. Si crees que es un error, usá el comando ***/about*** para obtener ayuda."))
        .build();
  }



}
