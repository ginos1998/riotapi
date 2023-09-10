package riot.riotapi.dtos.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuildEmojiDTO {
    private String id;
    private String name;
    private String image;
    private DiscordUserDTO user;
    private String[] roles;
    @JsonProperty("require_colons")
    private Boolean requireColons;
    private Boolean managed;
    private Boolean animated;
    private Boolean available;
}
