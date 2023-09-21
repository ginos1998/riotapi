package riot.riotapi.controllers.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.delegators.GuildEmojiDelegator;
import riot.riotapi.dtos.discord.GuildEmojiDTO;

@RestController
@RequestMapping("${discord.api-version}/guild-emoji")
public class GuildEmojiController {
    private final GuildEmojiDelegator emojiDelegator;

    @Autowired
    public GuildEmojiController(GuildEmojiDelegator emojiDelegator) {
        this.emojiDelegator = emojiDelegator;
    }
    @PostMapping("/create-champion-all")
    public Mono<GuildEmojiDTO> createsChampionEmojiAll(@RequestParam String guildId) {
        return emojiDelegator.createsChampionEmojiAll(guildId);
    }

    @PostMapping("/create-champion")
    public Mono<GuildEmojiDTO> createChampionEmojiByName(@RequestParam String guildId, @RequestParam String champName) {
        return emojiDelegator.createChampionEmojiByName(guildId, champName);
    }

    @GetMapping("/emoji-all")
    public Flux<GuildEmojiDTO> getEmojiGuildAll(@RequestParam String guildId) {
        return this.emojiDelegator.getEmojiAll(guildId);
    }

    @DeleteMapping("/delete-emoji-all")
    public Mono<Void> deleteEmojiAll(@RequestParam String guildId) {
        return this.emojiDelegator.deleteEmojiAll(guildId)
                .onErrorComplete();
    }
}
