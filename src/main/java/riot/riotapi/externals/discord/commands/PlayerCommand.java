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

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlayerCommand implements SlashCommand{

  private final SummonerDelegador summonerDelegador;
  private final Logger logger = LoggerFactory.getLogger(PlayerCommand.class);
  private final Map<String, String> queueTypesMap = new HashMap<>();

  @Autowired
  public PlayerCommand(SummonerDelegador summonerDelegador) {
    this.summonerDelegador = summonerDelegador;
    this.queueTypesMap.put("RANKED_SOLO_5x5", "SOLO/DUO");
    this.queueTypesMap.put("RANKED_FLEX_SR", "FLEX");
  }

  @Override
  public String getName() {
    return "player";
  }

  @Override
  public Mono<Void> handle(ChatInputInteractionEvent event) {
    String name = event.getOption("name")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElseThrow();
    return summonerDelegador.getSummonerStatsByNameMono(name)
        .flatMap(sumStats -> event.reply()
            .withEmbeds(headerEmbed(sumStats),
                                    summonerTierEmbed(sumStats),
                                    summonerTop3Masteries(sumStats)))
        .timeout(Duration.ofSeconds(2))
        .onErrorResume(err -> {
          logger.error("Error on summonerDelegador.flatMap: {}",err.getMessage());
          return Mono.empty();
        })
        .switchIfEmpty(Mono.defer(() -> {
              logger.debug("No live match found. Returning default embed.");
              return event.reply()
                  .withEphemeral(false)
                  .withEmbeds(playerNotFoundEmbed(name))
                  .then();
            }
        ));
  }

  private EmbedCreateSpec headerEmbed(SummonerStatsDTO summonerStatsDTO) {
    String isPlaying = Boolean.TRUE.equals(summonerStatsDTO.getIsPlaying())? "Yes" : "No";
    return EmbedCreateSpec.builder()
        .color(Color.MEDIUM_SEA_GREEN)
        .title(String.format("Stats of %s", summonerStatsDTO.getSummonerDTO().getName()))
        .description(String.format("> **LAS** | Level: **%d** | Is playing: **%s**", summonerStatsDTO.getSummonerDTO().getSummonerLevel(), isPlaying))
        .build();
  }

  private EmbedCreateSpec summonerTierEmbed(SummonerStatsDTO summonerStatsDTO) {
    StringBuilder type = new StringBuilder();
    StringBuilder tier = new StringBuilder();
    StringBuilder rank = new StringBuilder();
    summonerStatsDTO.getSummonerTierDTOList().forEach(sumTier -> {
      type.append(queueTypesMap.get(sumTier.getQueueType())).append('\n');
      tier.append(sumTier.getTier()).append('\n');
      rank.append(sumTier.getRank()).append('\n');
    });
    return EmbedCreateSpec.builder()
        .title("Tier")
        .color(Color.MOON_YELLOW)
        .addField("Type", type.toString(),true)
        .addField("Tier", tier.toString(), true)
        .addField("Rank", rank.toString(), true)
        .build();
  }

  private EmbedCreateSpec summonerTop3Masteries(SummonerStatsDTO summonerStatsDTO) {
    List<SummonerChampionMasteryDTO> summonerChampionMasteryDTOList = new ArrayList<>();
    StringBuilder champ = new StringBuilder();
    StringBuilder level = new StringBuilder();
    StringBuilder points = new StringBuilder();
    if (summonerStatsDTO.getSummonerChampionMasteryDTOS().size() > 3) {
      for (int i = 0; i < 3; i++) {
        summonerChampionMasteryDTOList.add(summonerStatsDTO.getSummonerChampionMasteryDTOS().get(i));
      }
    } else {
      summonerChampionMasteryDTOList.addAll(summonerStatsDTO.getSummonerChampionMasteryDTOS());
    }
    summonerChampionMasteryDTOList.forEach(sumMastery -> {
      champ.append(sumMastery.getChampionName()).append('\n');
      level.append(sumMastery.getChampionLevel()).append('\n');
      points.append(sumMastery.getChampionPoints()).append('\n');
    });

    return EmbedCreateSpec.builder()
        .title("Mastery")
        .color(Color.SUMMER_SKY)
        .addField("Champion", champ.toString(),true)
        .addField("Level", level.toString(), true)
        .addField("Points", points.toString(), true)
        .build();
  }

  private EmbedCreateSpec playerNotFoundEmbed(String name) {
    return EmbedCreateSpec.builder()
        .color(Color.YELLOW)
        .author("League Of Trolls", URLs.URL_LoT_REPO , URLs.ICON_LoT_BOT)
        .title("UPS! Not found")
        .description("Summoner ".concat(name).concat(" not found :face_with_monocle:. Please, try again. If you think this is an error, type ***/about*** to get support info."))
        .build();
  }

}
