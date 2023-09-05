package riot.riotapi.externals.discord.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import riot.riotapi.externals.discord.utils.URLs;

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
public class GuildEmojiService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient webClient = WebClient.create();
    @Value("${discord.token}")
    private String token;
    private final int AMOUNT_OF_CHAMPS = 161;

    public void createsChampionEmojiAll(String guildId) {
        List<String> champs = new ArrayList<>(Arrays.asList("illaoi", "lux", "sett", "jinx", "jhin"));
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/main/resources/emojis/champion-create.json")));
            JSONArray jsonArray = new JSONArray(jsonContent);

            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dictionary = jsonArray.getJSONObject(i);
                if (champs.contains(dictionary.get("name").toString())) {
                    jsonObjectList.add(dictionary);
                    createEmojiAtGivenGuild(dictionary, guildId);
                }
            }
            logger.info("size of list: " + jsonObjectList.size());
        } catch (Exception e) {
            logger.info("error");
        }
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
            fileInputStream.read(imageBytes);
        } catch (IOException e) {
            logger.error("Error reading an imageFile: " + imageFile.getName() + ". " + e.getMessage());
        }

        return Base64.getEncoder().encodeToString(imageBytes);
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

    private void createEmojiAtGivenGuild(JSONObject jsonObject, String guildId) {
        try {
            String jsonBody = jsonObject.toString();
            String headerName = "Authorization";
            String apiKey = "Bot ".concat(token);
            String response = webClient.post()
                    .uri(String.format(URLs.DS_POST_GUILD_EMOJI, guildId))
                    .header(headerName, apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(jsonBody)
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                    .block();
            logger.info(response);
        } catch (Exception e) {
            logger.error("Error during POST to discord API. " + e.getMessage());
        }
    }
}
