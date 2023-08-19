package riot.riotapi.externals.discord.configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import riot.riotapi.exceptions.DiscordException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Configuration
public class BotConfiguration {

    @Value("${discord.token}")
    private String token;
    Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client;
        try {
             client = DiscordClientBuilder.create(token)
                    .build()
                    .login()
                    .block();

            for(EventListener<T> listener : eventListeners) {
                assert client != null;
                client.on(listener.getEventType())
                        .flatMap(listener::execute)
                        .onErrorResume(listener::handleError)
                        .subscribe();
            }
            logger.info("Discord BOT has started successfully!");
        } catch (BeanCreationException ex) {
            logger.error("ERROR al inicializar discord.\n".concat(ex.getMessage()));
            throw new DiscordException("ERROR al inicializar discord.\n".concat(ex.getMessage()), ex);
        }

        return client;
    }
}
