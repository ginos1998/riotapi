package riot.riotapi.delegators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.discord.GuildEmojiDTO;
import riot.riotapi.services.interfaces.IntGuildEmojiService;

@Service
public class GuildEmojiDelegator {

    private final IntGuildEmojiService guildEmojiService;

    @Autowired
    public GuildEmojiDelegator(IntGuildEmojiService guildEmojiService) {
        this.guildEmojiService = guildEmojiService;
    }

    public Mono<GuildEmojiDTO> createChampionEmojiByName(String guildId, String champName) {
        return guildEmojiService.createChampionEmojiByName(guildId, champName);
    }

    public Flux<GuildEmojiDTO> getEmojiAll(String guildId) {
        return guildEmojiService.getEmojiAll(guildId);
    }

    public Mono<Void> deleteEmojiAll(String guildId) {
        return guildEmojiService.deleteEmojiAll(guildId);
    }

    public Mono<GuildEmojiDTO> createsChampionEmojiAll(String guildId) {
        return guildEmojiService.createsChampionEmojiAll(guildId);
    }


}
