package me.ikurenkov.game.logic.config;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.github.siahsang.redutils.RedUtilsLock;
import org.github.siahsang.redutils.RedUtilsLockImpl;
import org.github.siahsang.redutils.common.RedUtilsConfig;

import java.net.URI;

@Dependent
public class LockConfig {

    @Produces
    public RedUtilsLock getLockService(
            @ConfigProperty(name = "quarkus.redis.hosts") String redisUri) {
        URI uri = URI.create(redisUri);
        RedUtilsConfig config = new RedUtilsConfig.RedUtilsConfigBuilder()
                .hostAddress(uri.getHost())
                .port(uri.getPort())
                .leaseTimeMillis(30000)
                .build();
        return new RedUtilsLockImpl(config);
    }
}
