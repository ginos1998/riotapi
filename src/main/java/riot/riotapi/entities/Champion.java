package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "champion")
public class Champion {
  // Champion's fields
  @Id
  @Column(name = "key")
  private Long key;
  @Column(name = "champion_id")
  private String championId;
  @OneToOne
  @JoinColumn(name = "id_champ_data")
  private ChampionData champData;
  @Column(name = "name")
  private String name;
  @Column(name = "title")
  private String title;

  public Champion(Long key) {
    this.key = key;
  }
}
