package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "info")
public class Info {
  @Id
  private Long key;

  @Column(name = "attack")
  private int attack;
  @Column(name = "defense")
  private int defense;
  @Column(name = "magic")
  private int magic;
  @Column(name = "difficulty")
  private int difficulty;
}
