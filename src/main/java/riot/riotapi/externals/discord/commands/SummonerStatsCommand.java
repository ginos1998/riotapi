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
import riot.riotapi.delegators.SummonerDelegador;
import riot.riotapi.dtos.summoner.SummonerChampionMasteryDTO;
import riot.riotapi.dtos.summoner.SummonerStatsDTO;
import riot.riotapi.externals.discord.utils.URLs;
import riot.riotapi.utils.CommonFunctions;

import java.time.Duration;
import java.util.*;

@Component
public class SummonerStatsCommand implements SlashCommand{

  private final SummonerDelegador summonerDelegador;
  private final Logger logger = LoggerFactory.getLogger(SummonerStatsCommand.class);
  private final Map<String, String> queueTypesMap = new HashMap<>();
  private final String space = " ";

  @Autowired
  public SummonerStatsCommand(SummonerDelegador summonerDelegador) {
    this.summonerDelegador = summonerDelegador;
    this.queueTypesMap.put("RANKED_SOLO_5x5", "SOLO/DUO");
    this.queueTypesMap.put("RANKED_FLEX_SR", "FLEX");
  }

  @Override
  public String getName() {
    return "invocador";
  }

  @Override
  public Mono<Void> handle(ChatInputInteractionEvent event) {
    String name = event.getOption("nombre")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElseThrow();
    int timeOutSeconds = 6;
    return summonerDelegador.getSummonerStatsByNameMono(name)
        .flatMap(sumStats -> event.reply()
                                  .withEmbeds(summonerStatsEmbed(sumStats))
                                  .timeout(Duration.ofSeconds(timeOutSeconds))
          )
        .timeout(Duration.ofSeconds(timeOutSeconds))
        .switchIfEmpty(Mono.defer(() -> {
              logger.debug("No live match found. Returning default embed.");
              return event.reply()
                  .withEphemeral(false)
                  .withEmbeds(playerNotFoundEmbed(name))
                  .then();
            }
        ))
        .onErrorResume(err -> {
          logger.warn("Error on summonerDelegador.flatMap: {}",err.getMessage());
          return event.reply()
              .withEphemeral(false)
              .withEmbeds(playerNotFoundEmbed(name))
              .then();
        });
  }

  private EmbedCreateSpec summonerStatsEmbed(SummonerStatsDTO summonerStatsDTO) {
    int championMastersAmount = 3;
    boolean inline = true;
    String whiteSpace = "\u200B";
    String isPlaying = Boolean.TRUE.equals(summonerStatsDTO.getIsPlaying())? "Si" : "No";
    String lastRevision = CommonFunctions.getDateAsString(summonerStatsDTO.getSummonerDTO().getRevisionDate());
    StringBuilder type = new StringBuilder("**Tipo**\n");
    StringBuilder tier = new StringBuilder("**Tier**\n");
    StringBuilder rank = new StringBuilder("**Rango**\n");
    StringBuilder champ = new StringBuilder("**Campeón**\n");
    StringBuilder level = new StringBuilder("**Nivel**\n");
    StringBuilder points = new StringBuilder("**Puntos**\n");
    
    summonerStatsDTO.getSummonerTierDTOList().forEach(sumTier -> {
      type.append(queueTypesMap.get(sumTier.getQueueType())).append('\n');
      tier.append(sumTier.getDsEmoji()).append(space).append(sumTier.getTier()).append('\n');
      rank.append(sumTier.getRank()).append('\n');
    });
    List<SummonerChampionMasteryDTO> summonerChampionMasteryDTOList = new ArrayList<>();

    summonerStatsDTO.getSummonerChampionMasteryDTOS()
        .sort(Comparator.comparingLong(SummonerChampionMasteryDTO::getChampionLevel)
            .reversed());

    if (summonerStatsDTO.getSummonerChampionMasteryDTOS().size() > championMastersAmount) {
      for (int i = 0; i < championMastersAmount; i++) {
        SummonerChampionMasteryDTO sumMastery = summonerStatsDTO.getSummonerChampionMasteryDTOS().get(i);
        summonerChampionMasteryDTOList.add(sumMastery);
        buildSummonerMastersEmbed(sumMastery, champ, level, points);
      }
    } else {
      summonerChampionMasteryDTOList.addAll(summonerStatsDTO.getSummonerChampionMasteryDTOS());
      summonerChampionMasteryDTOList.forEach(sumMastery -> buildSummonerMastersEmbed(sumMastery, champ, level, points));
    }

    return EmbedCreateSpec.builder()
        .color(Color.MEDIUM_SEA_GREEN)
        .title(String.format(":bar_chart: Estadísticas de %s", summonerStatsDTO.getSummonerDTO().getName()))
        .description(String.format("Region: **LAS** \nNivel: **%d** \n¿Está jugando?: **%s** \nÚltima vez: %s", summonerStatsDTO.getSummonerDTO().getSummonerLevel(), isPlaying, lastRevision))
        .addField(whiteSpace, whiteSpace, !inline)
        .addField(whiteSpace, type.toString(), inline)
        .addField("TIERS", tier.toString(), inline)
        .addField(whiteSpace, rank.toString(), inline)
        .addField(whiteSpace, whiteSpace, false)
        .addField(whiteSpace, level.toString(),inline)
        .addField("MAESTRÍAS", champ.toString(), inline)
        .addField(whiteSpace, points.toString(), inline)
        .build();
  }

  private void buildSummonerMastersEmbed(SummonerChampionMasteryDTO sumMastery, StringBuilder champ, StringBuilder level, StringBuilder points) {
    champ.append(sumMastery.getChampEmoji()).append(space).append(sumMastery.getChampionName()).append('\n');
    level.append(sumMastery.getMasteryEmoji()).append(space).append(sumMastery.getChampionLevel()).append('\n');
    points.append(sumMastery.getChampionPoints()).append('\n');
  }

  private EmbedCreateSpec playerNotFoundEmbed(String name) {
    return EmbedCreateSpec.builder()
        .color(Color.YELLOW)
        .author("League Of Trolls", URLs.URL_LoT_REPO , URLs.ICON_LoT_BOT)
        .title("UPS! Algo falló")
        .description("Invocador ".concat(name).concat(" no encontrado :face_with_monocle:. Por favor, intentá de nuevo. Si crees que es un error, usá el comando ***/about*** para obtener ayuda."))
        .build();
  }

}
