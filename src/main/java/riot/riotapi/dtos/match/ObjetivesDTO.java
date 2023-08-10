package riot.riotapi.dtos.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjetivesDTO {
    private BaronDTO baron;
    private ChampionDTO champion;
    private DragonDTO dragon;
    private InhibitorDTO inhibitor;
    private RiftHeraldDTO riftHerald;
    private TowerDTO tower;
}
