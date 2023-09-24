package riot.riotapi.services.interfaces;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.discord.GuildEmojiDTO;

public interface IntGuildEmojiService {
    Mono<GuildEmojiDTO> createsChampionEmojiAll(String guildId);
    Flux<GuildEmojiDTO> createChampionEmojiByName(String guildId, Flux<String> champsNames);

    Flux<GuildEmojiDTO> getEmojiAll(String guildId);
    Mono<Void> deleteEmojiAll(String guildId);
}
