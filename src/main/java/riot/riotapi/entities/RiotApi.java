package riot.riotapi.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "riot_api")
public class RiotApi {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_riot_api")
  private Long idRiotApi;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "api_key")
  private String apiKey;
  @Column(name = "last_update")
  private Date lastUpdate;
}
