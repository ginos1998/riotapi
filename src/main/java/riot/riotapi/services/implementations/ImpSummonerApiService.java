package riot.riotapi.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.SummonerDTO;
import riot.riotapi.dtos.summoner.SummonerTierDTO;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntSummonerApiService;
import riot.riotapi.utils.CommonFunctions;
import riot.riotapi.utils.ConstantsExceptions;
import riot.riotapi.utils.URIs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ImpSummonerApiService implements IntSummonerApiService {

  private final WebClient webClient;

  @Value("${riot.apikey}")
  private String apiKey;
  private final Logger logger = LoggerFactory.getLogger(ImpSummonerApiService.class);

  public ImpSummonerApiService() {
    this.webClient = WebClient.create();
  }

  private SummonerDTO getQueryParamResult(String url, String param) {
    return webClient.get()
        .uri(url, param, apiKey)
        .retrieve()
        .bodyToMono(SummonerDTO.class)
        .block();
  }

  private String getApiKeyURLFormat() {
    return "?api_key=" + apiKey;
  }

  @Override
  public List<SummonerDTO> getSummonerByName(String name) {
    List<SummonerDTO> dtoList;

    try {
      validateInput(name);
      String url = URIs.URI_SUMMONER_ACCOUNT_NAME + name + getApiKeyURLFormat();
      dtoList = new ArrayList<>(Collections.singletonList(getQueryParamResult(url, name)));
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(name), ex);
    }

    return dtoList;
  }

  @Override
  public List<SummonerDTO> getSummonerByAccountId(String accountId) {
    List<SummonerDTO> dtoList;

    try {
      validateInput(accountId);
      String url = URIs.URI_SUMMONER_ACCOUNT_ID + accountId + getApiKeyURLFormat();
      dtoList = new ArrayList<>(Collections.singletonList(getQueryParamResult(url, accountId)));
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(accountId), ex);
    }

    return dtoList;
  }

  @Override
  public List<SummonerDTO> getSummonerByPuuid(String puuid) {

    List<SummonerDTO> dtoList;

    try {
      validateInput(puuid);
      String url = URIs.URI_SUMMONER_ACCOUNT_PUUID + puuid + getApiKeyURLFormat();
      dtoList = new ArrayList<>(Collections.singletonList(getQueryParamResult(url, puuid)));
    } catch (Exception ex) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(puuid), ex);
    }

    return dtoList;
  }

  /**
   * Searches a summoner given his name. This is produces a reactive result (no blocking).
   * @param sumName summoner's name
   * @return Mono
   */
  public Mono<SummonerDTO> getSummonerByNameMono(String sumName) {
    String url = URIs.URI_SUMMONER_ACCOUNT_NAME.concat(sumName);
    logger.info("Start champion api request with {}", sumName);
    return webClient.get()
            .uri(url)
            .header("X-Riot-Token", this.apiKey)
            .retrieve()
            .bodyToMono(SummonerDTO.class)
            .onErrorResume(err -> {
              logger.error("An error has occurred getting summoner by name (Mono): " + err.getMessage());
              return Mono.empty();
            });
  }

  @Override
  public Flux<SummonerTierDTO> getSummonerTierFlux(String summonerId) {
    logger.info("Start getting summoner tier with summonerId: {}", summonerId);
    return webClient.get()
        .uri(URIs.URI_LOL_SUMMONER_TIER.concat(summonerId))
        .header("X-Riot-Token", this.apiKey)
        .retrieve()
        .bodyToFlux(SummonerTierDTO.class)
        .onErrorResume(err -> {
          logger.error("An error has occurred getting summoner tier with summonerId="+summonerId+": " + err.getMessage());
          return Mono.empty();
        });
  }

  /**
   * Calls riot's API for each 'summonerName' in the list.
   * They're processed sequentially using flatMapSequential, so in this way
   * the result is in the same order as the given list, and it's done concurrently.
   * On error, log the message and returns an empty Mono.
   * @param summonersNames names to search
   * @return FLux data
   */
  @Override
  public Flux<SummonerDTO> findMatchSummonersByName(List<String> summonersNames) {
    Flux<String> sumNamesFlux = Flux.fromIterable(summonersNames);

    return sumNamesFlux.flatMapSequential(
            sumName -> webClient.get()
            .uri(URIs.URI_SUMMONER_ACCOUNT_NAME + sumName + getApiKeyURLFormat(), sumName, apiKey)
            .retrieve()
            .bodyToMono(SummonerDTO.class)
                    .onErrorResume(err -> {
                      logger.error("Error getting summoners: " + err.getMessage());
                      return Mono.empty();
                    })
    );

  }

  private void validateInput(String input) {
    if (!CommonFunctions.isNotNullOrEmpty(input)) {
      throw new ServiceException(ConstantsExceptions.ERROR_SEARCHING_SUMMONER.concat(input));
    }
  }

}
