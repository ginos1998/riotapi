package riot.riotapi.dtos.summoner;

import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.dtos.SummonerDTO;

import java.util.List;

@Data
@NoArgsConstructor
public class SummonerStatsDTO {
  private SummonerDTO summonerDTO;
  private List<SummonerTierDTO> summonerTierDTOList;
  private List<SummonerChampionMasteryDTO> summonerChampionMasteryDTOS;
  private Boolean isPlaying;

  public SummonerStatsDTO(SummonerDTO summonerDTO, List<SummonerTierDTO> summonerTierDTOList, List<SummonerChampionMasteryDTO> summonerChampionMasteryDTOS) {
    this.summonerDTO = summonerDTO;
    this.summonerTierDTOList = summonerTierDTOList;
    this.summonerChampionMasteryDTOS = summonerChampionMasteryDTOS;
    this.isPlaying = false;
  }
}
