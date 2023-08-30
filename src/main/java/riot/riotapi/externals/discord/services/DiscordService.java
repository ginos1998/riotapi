package riot.riotapi.externals.discord.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import riot.riotapi.externals.discord.models.dtos.GuildEmojiDTO;
import riot.riotapi.externals.discord.utils.URLs;

import java.util.List;

@Service
public class DiscordService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient = WebClient.create();
    @Value("${discord.token}")
    private String token;

    public Mono<List<GuildEmojiDTO>> getGuildEmojis(Long guildId) {
        return webClient.get()
                .uri(String.format(URLs.DS_GET_GUILD_EMOJIS, guildId))
                .header("Authorization", "Bot ".concat(token))
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, clientResponse -> {logger.error("401 Unauthorized from GET https://discord.com/api/v10/guilds/1140033130200301663/emojis"); return Mono.empty(); })
                .bodyToFlux(GuildEmojiDTO.class)
                .collectList();
    }
}
