package riot.riotapi.externals.discord.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import riot.riotapi.externals.discord.services.GuildEmojiService;

@RestController
@RequestMapping("${discord.api-version}/guild-emoji")
public class GuildEmojiController {
    private final GuildEmojiService emojiService;

    @Autowired
    public GuildEmojiController(GuildEmojiService emojiService) {
        this.emojiService = emojiService;
    }
    @PostMapping("/create-champion-all")
    public ResponseEntity<Void> createsChampionEmojiAll(@RequestParam String guildId) {
        emojiService.createsChampionEmojiAll(guildId);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.CREATED.value())).build();
    }
}
