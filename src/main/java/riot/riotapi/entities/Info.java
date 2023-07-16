package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "info")
public class Info {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_info")
  private Long idInfo;

  @OneToOne
  @JoinColumn(name = "key")
  private Champion champion;

  @Column(name = "attack")
  private int attack;
  @Column(name = "defense")
  private int defense;
  @Column(name = "magic")
  private int magic;
  @Column(name = "difficulty")
  private int difficulty;
}
