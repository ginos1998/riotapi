package riot.riotapi.externals.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import riot.riotapi.exceptions.DiscordException;

@Configuration
public class BotConfiguration {

    @Value("${discord.token}")
    private String token;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        try {
            return DiscordClientBuilder.create(token)
                    .build()
                    .login()
                    .block();
        } catch (BeanCreationException ex) {
            throw new DiscordException("ERROR al inicializar discord.\n".concat(ex.getMessage()), ex);
        }
    }
}
