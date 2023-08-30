package riot.riotapi.externals.discord.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuildEmojiDTO {
    private String name;
    private String image;
    private DiscordUser user;
    private String[] roles;
    @JsonProperty("require_colons")
    private Boolean requireColons;
    private Boolean managed;
    private Boolean animated;
    private Boolean available;
}
