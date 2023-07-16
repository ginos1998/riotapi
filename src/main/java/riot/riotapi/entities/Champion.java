package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;
import riot.riotapi.dtos.*;

import java.util.ArrayList;

@Data
@Entity
@Table(name = "champion")
public class Champion {
  // Champion's fields
  @Id
  @Column(name = "key")
  private Long key;
  @Column(name = "id")
  private String id;
  @OneToOne
  @JoinColumn(name = "id_champ_data")
  private ChampionData champData;
  @Column(name = "name")
  private String name;
  @Column(name = "title")
  private String title;
}
