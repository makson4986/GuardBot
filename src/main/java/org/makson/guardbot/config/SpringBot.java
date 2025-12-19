package org.makson.guardbot.config;

import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.config.JDAConfiguration;
import io.github.freya022.botcommands.api.core.events.BReadyEvent;
import io.github.freya022.botcommands.api.core.service.annotations.BService;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@BService
public class SpringBot extends JDAService {
    private final JDAConfiguration jdaConfiguration;
    private final String token;

    public SpringBot(JDAConfiguration jdaConfiguration, @Value("${discord.token}") String token) {
        this.jdaConfiguration = jdaConfiguration;
        this.token = token;
    }

    @NotNull
    @Override
    public Set<CacheFlag> getCacheFlags() {
        return jdaConfiguration.getCacheFlags();
    }

    @NotNull
    @Override
    public Set<GatewayIntent> getIntents() {
        return jdaConfiguration.getIntents();
    }

    @Override
    public void createJDA(@NotNull BReadyEvent event, @NotNull IEventManager eventManager) {
        createLight(token)
                .build();
    }
}
