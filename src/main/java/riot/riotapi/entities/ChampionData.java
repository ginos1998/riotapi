package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "champ_data")
public class ChampionData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_champ_data")
  private Long idChampData;
  @Column(name = "type")
  private String type;
  @Column(name = "format")
  private String format;
  @Column(name = "version")
  private String version;
  @Column(name = "last_update")
  private Date lastUpdate;
}
