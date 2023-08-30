package riot.riotapi.externals.discord.models.dtos;

import lombok.Data;

@Data
public class DiscordUser {
    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private int publicFlags;
    private int flags;
    private String banner;
    private String accentColor;
    private String globalName;
    private String avatarDecorationData;
    private String bannerColor;
}
