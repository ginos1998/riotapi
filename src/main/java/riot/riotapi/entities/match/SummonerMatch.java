package riot.riotapi.entities.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.entities.Summoner;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "summoner_match")
public class SummonerMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summoner_match_id")
    private Long summonerMatchId;
    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @OneToOne
    @JoinColumn(name = "key")
    private Summoner summoner;
}
