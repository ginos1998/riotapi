package riot.riotapi.filters;

import lombok.Data;

@Data
public class MatchFilter {
  private String puuid;
  private long startTime;
  private long endTime;
  private int queue;
  private String type;
  private int start;
  private int count;
  private boolean save;
}
