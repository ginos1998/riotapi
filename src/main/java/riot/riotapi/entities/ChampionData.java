package riot.riotapi.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;

@lombok.Data
@Entity
@Table(name = "champ_data")
public class ChampionData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String type;
  private String format;
  private String version;
  private Date lastUpdate;

  @Transient // anotacion para decirle a JPA que ignore éste campo durante la persistencia y no lo mapee a ninguna columna
  private Map<String, Champion> data;
}
