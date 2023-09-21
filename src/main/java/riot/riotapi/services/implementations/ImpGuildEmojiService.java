package riot.riotapi.services.implementations;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import riot.riotapi.dtos.discord.GuildEmojiDTO;
import riot.riotapi.exceptions.ServiceException;
import riot.riotapi.externals.discord.utils.URLs;
import riot.riotapi.services.interfaces.IntGuildEmojiService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class ImpGuildEmojiService implements IntGuildEmojiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient = WebClient.create();
    @Value("${discord.token}")
    private String token;
    private JSONArray jsonArray;
    private final String EMOJI_LOT = "lot";

    public ImpGuildEmojiService() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/emojis/champion-create.json")));
            this.jsonArray = new JSONArray(jsonContent);
        } catch (Exception e) {
            logger.error("An error has occurred reading champion-create.json: " + e.getMessage());
        }
    }

    public Mono<GuildEmojiDTO> createChampionEmojiByName(String guildId, String champName) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject dictionary = jsonArray.getJSONObject(i);
            if (champName.equalsIgnoreCase(dictionary.get("name").toString())
                || champName.replace(" ", "_").equalsIgnoreCase(dictionary.get("name").toString())) {
                return createEmojiAtGivenGuild(dictionary, guildId);
            }
        }
        return Mono.empty();
    }

    public Flux<GuildEmojiDTO> getEmojiAll(String guildId) {
        String headerName = "Authorization";
        String apiKey = "Bot ".concat(token);
        logger.info("Start getting emojis from guild " + guildId);
        return webClient.get()
                .uri(String.format(URLs.DS_GET_GUILD_EMOJIS, guildId))
                .header(headerName, apiKey)
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToFlux(GuildEmojiDTO.class)
                .onErrorResume(err -> {
                    logger.error("An error has occurred getting emojis from guild " + guildId + ". " + err.getMessage());
                    return Mono.empty();
                });
    }

    public Mono<Void> deleteEmojiAll(String guildId) {
        try {
            String headerName = "Authorization";
            String apiKey = "Bot ".concat(token);
            logger.info("Start getting emojis from guild " + guildId);

            return webClient.get()
                    .uri(String.format(URLs.DS_GET_GUILD_EMOJIS, guildId))
                    .header(headerName, apiKey)
                    .header("Content-Type", "application/json")
                    .retrieve()
                    .bodyToFlux(GuildEmojiDTO.class)
                    .flatMap(emoji -> {
                        if (!emoji.getName().equals(EMOJI_LOT)) {
                            logger.info("Deleting " + emoji.getName() + " " + emoji.getId());
                            return webClient.delete()
                                    .uri(String.format(URLs.DS_DELETE_GUILD_EMOJI, guildId, emoji.getId()))
                                    .header(headerName, apiKey)
                                    .header("Content-Type", "application/json")
                                    .retrieve()
                                    .bodyToMono(Void.class);
                        }
                        return Mono.empty();
                    })
                    .doOnError(e -> logger.error("An error has occurred deleting all emojis on guild " + guildId + ": " + e.getMessage()))
                    .then();
        } catch (Exception e) {
            logger.error("An error has occurred deleting all emojis on guild " + guildId + ": " + e.getMessage());
            throw new ServiceException("An error has occurred deleting all emojis on guild " + guildId + ": " + e.getMessage(), e);
        }

    }

    public Mono<GuildEmojiDTO> createsChampionEmojiAll(String guildId) {
        List<String> champs = new ArrayList<>(Arrays.asList("illaoi", "lux", "sett", "jinx", "jhin"));
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dictionary = jsonArray.getJSONObject(i);
                if (champs.contains(dictionary.get("name").toString())) {
                    return createEmojiAtGivenGuild(dictionary, guildId);
                }
            }
        } catch (Exception e) {
            logger.info("error");
        }
        return Mono.empty();
        // fillChampionJsonFile();
    }

    private void fillChampionJsonFile() {
        File folder = new File("src/main/resources/static/champ-icons");

        if (folder.isDirectory()) {
            File[] imageFiles = folder.listFiles((dir, name) -> (name.toLowerCase().endsWith(".webp") || name.toLowerCase().endsWith(".png")));

            if (imageFiles != null && imageFiles.length > 0) {
                JSONArray jsonArray = new JSONArray();

                for (File imageFile: imageFiles) {

                    String base64Encoded = readAndEncodeImage(imageFile);

                    if (base64Encoded == null) {
                        break;
                    }

                    int indexOfKeyword = imageFile.getName().indexOf("Square");
                    if (indexOfKeyword != -1) {
                        JSONObject jsonObject = createJsonObject(imageFile.getName(), base64Encoded, indexOfKeyword);
                        jsonArray.put(jsonObject);
                    }
                }
                saveJsonArray(jsonArray);
            }
        }
    }

    private String readAndEncodeImage(File imageFile) {
        byte[] imageBytes = new byte[(int) imageFile.length()];

        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            if (fileInputStream.read(imageBytes) > -1) {
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (IOException e) {
            logger.error("Error reading an imageFile: " + imageFile.getName() + ". " + e.getMessage());
        }
        return null;
    }

    private JSONObject createJsonObject(String imageFileName, String base64Encoded, int indexOfKeyword) {
        String extractedName = imageFileName.substring(0, indexOfKeyword);
        String discordImgFormat = "data:image/webp;base64, " + base64Encoded;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", extractedName.toLowerCase());
        jsonObject.put("image", discordImgFormat);
        jsonObject.put("roles", new JSONArray());

        return jsonObject;
    }

    private void saveJsonArray(JSONArray jsonArray) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/emojis/champion-create.json")) {
            fileWriter.write(jsonArray.toString());
            logger.info("JsonArray with champions-emojis format to POST was created successfully!");
        } catch (IOException e) {
            logger.error("Error writing a json file. " + e.getMessage());
        }
    }

    private Mono<GuildEmojiDTO> createEmojiAtGivenGuild(JSONObject jsonObject, String guildId) {
        try {
            String jsonBody = jsonObject.toString();
            String headerName = "Authorization";
            String apiKey = "Bot ".concat(token);
            // logger.info("Creating emoji to guild " + guildId + "with body " + jsonBody);
            return webClient.post()
                    .uri(String.format(URLs.DS_POST_GUILD_EMOJI, guildId))
                    .header(headerName, apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(jsonBody)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(GuildEmojiDTO.class))
                    .doOnNext(e -> logger.info("At guild "+guildId+" created " + e.getName()))
                    .onErrorResume(err -> {
                        logger.error("An error has occurred posting emojis to guild " + guildId);
                        return Mono.empty();
                    });
        } catch (Exception e) {
            logger.error("Error during POST to discord API. " + e.getMessage());
        }

        return Mono.empty();

    }
}
