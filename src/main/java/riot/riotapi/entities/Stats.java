package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stats")
public class Stats {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_stats")
  private Long idStats;
  @OneToOne
  @JoinColumn(name = "key")
  private Champion champion;
  @Column(name = "hp")
  private int hp;
  @Column(name = "hp_per_level")
  private int hpperlevel;
  @Column(name = "mp")
  private int mp;
  @Column(name = "mp_per_level")
  private int mpperlevel;
  @Column(name = "move_speed")
  private int movespeed;
  @Column(name = "armor")
  private int armor;
  @Column(name = "armor_per_level")
  private double armorperlevel;
  @Column(name = "spell_block")
  private int spellblock;
  @Column(name = "spell_block_per_level")
  private double spellblockperlevel;
  @Column(name = "attack_range")
  private int attackrange;
  @Column(name = "hp_regen")
  private int hpregen;
  @Column(name = "hp_regen_per_level")
  private int hpregenperlevel;
  @Column(name = "mp_regen")
  private int mpregen;
  @Column(name = "mp_regen_per_level")
  private int mpregenperlevel;
  @Column(name = "crit")
  private int crit;
  @Column(name = "crit_per_level")
  private int critperlevel;
  @Column(name = "attack_damage")
  private int attackdamage;
  @Column(name = "attack_damage_per_level")
  private int attackdamageperlevel;
  @Column(name = "attack_speed_per_level")
  private double attackspeedperlevel;
  @Column(name = "attack_speed")
  private double attackspeed;
}

