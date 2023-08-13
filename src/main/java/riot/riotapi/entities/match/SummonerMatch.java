package riot.riotapi.entities.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import riot.riotapi.entities.Champion;
import riot.riotapi.entities.Summoner;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "summoner_match")
public class SummonerMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summoner_match_id")
    private Long summonerMatchId;
    @OneToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @OneToOne
    @JoinColumn(name = "summoner_id")
    private Summoner summoner;
    @Column(name = "lane")
    private String lane;
    @Column(name = "win")
    private Boolean win;
    @OneToOne
    @JoinColumn(name = "champion_key", referencedColumnName = "key")
    private Champion champion;
    @Column(name = "team_id")
    private Integer teamId;

    public SummonerMatch(Match match, Summoner summoner, String lane, Boolean win, Champion champion, Integer teamId) {
        this.match = match;
        this.summoner = summoner;
        this.lane = lane;
        this.win = win;
        this.champion = champion;
        this.teamId = teamId;
    }
}
