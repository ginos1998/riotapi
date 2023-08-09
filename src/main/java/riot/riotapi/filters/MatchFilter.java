package riot.riotapi.filters;

import lombok.Data;

@Data
public class MatchFilter {
  private Long startTime;
  private Long endTime;
  private Integer queue;
  private String type;
  private Integer start;
  private Integer count;
  private boolean save;
}
