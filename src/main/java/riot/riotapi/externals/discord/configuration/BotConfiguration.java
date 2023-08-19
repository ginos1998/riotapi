package riot.riotapi.externals.discord.configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import riot.riotapi.exceptions.DiscordException;
import riot.riotapi.externals.discord.listeners.BotListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class BotConfiguration {

    @Value("${discord.token}")
    private String token;
    private JDA jda;
    Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

    @Bean
    public JDA jda() {
        try {
            return JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new BotListener())
                .build();
        } catch (BeanCreationException ex) {
            throw new DiscordException("ERROR al inicializar discord.\n".concat(ex.getMessage()), ex);
        } finally {
            logger.info("Discord BOT has started successfully!");
        }
    }
//    @Bean
//    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
//        GatewayDiscordClient client;
//        try {
//             client = DiscordClientBuilder.create(token)
//                    .build()
//                    .login()
//                    .block();
//
//            for(EventListener<T> listener : eventListeners) {
//                assert client != null;
//                client.on(listener.getEventType())
//                        .flatMap(listener::execute)
//                        .onErrorResume(listener::handleError)
//                        .subscribe();
//            }
//        } catch (BeanCreationException ex) {
//            throw new DiscordException("ERROR al inicializar discord.\n".concat(ex.getMessage()), ex);
//        }
//
//        return client;
//    }
}
