package riot.riotapi.delegators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.discord.GuildEmojiDTO;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.services.interfaces.IntGuildEmojiService;

@Service
public class GuildEmojiDelegator {

    private final Logger logger = LoggerFactory.getLogger(GuildEmojiDelegator.class);
    private final IntGuildEmojiService guildEmojiService;

    @Autowired
    public GuildEmojiDelegator(IntGuildEmojiService guildEmojiService) {
        this.guildEmojiService = guildEmojiService;
    }

    public Flux<GuildEmojiDTO> createChampionEmojiByName(String guildId, String champName) {
        Flux<String> champsNamesFlux = Flux.just(champName, "zed", "sett", "vex", "ziggs", "teemo", "yasuo", "yone");
        return guildEmojiService.createChampionEmojiByName(guildId, champsNamesFlux);
    }

    public Flux<GuildEmojiDTO> getEmojiAll(String guildId) {
        return guildEmojiService.getEmojiAll(guildId);
    }

    public Mono<Void> deleteEmojiAll(String guildId) {
        try {
            return guildEmojiService.deleteEmojiAll(guildId);
        } catch (Exception e) {
            logger.error("An error has occurred deleting all emojis on guild " + guildId + ": " + e.getMessage());
            throw new ServiceException("An error has occurred deleting all emojis on guild " + guildId + ": " + e.getMessage(), e);
        }

    }

    public Mono<GuildEmojiDTO> createsChampionEmojiAll(String guildId) {
        return guildEmojiService.createsChampionEmojiAll(guildId);
    }


}
