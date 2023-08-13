package riot.riotapi.entities.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity()
@Table(name = "match")
public class Match {
    @Id
    @Column(name = "match_id")
    private Long matchId;
    @Column(name = "creation")
    private Long creation;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "end_time")
    private Long endTime;
    @Column(name = "mode")
    private String mode;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "version")
    private String version;
    @Column(name = "map_id")
    private Integer mapId;

    public Match(Long matchId) {
        this.matchId = matchId;
    }
}
