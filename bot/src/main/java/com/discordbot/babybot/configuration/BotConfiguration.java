package com.discordbot.babybot.configuration;


import com.discordbot.babybot.events.EventListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.security.auth.login.LoginException;

@Configuration
@PropertySource("bot.properties")
public class BotConfiguration {

    @Value("${token}")
    private String TOKEN;
    private static final Logger log = LoggerFactory.getLogger(BotConfiguration.class);

    @Bean
    public void wakeUp() {
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);
        jdaBuilder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        jdaBuilder.setBulkDeleteSplittingEnabled(false);
        jdaBuilder.setActivity(Activity.competing("Sleeping"));
        jdaBuilder.setStatus(OnlineStatus.IDLE);
        jdaBuilder.addEventListeners(new EventListener());
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        try {
            jdaBuilder.build();
        } catch (LoginException ex) {
            log.error("Login failed", ex);
        }
    }
}
