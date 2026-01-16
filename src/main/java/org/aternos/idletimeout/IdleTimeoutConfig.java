package org.aternos.idletimeout;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.HytaleServer;

import javax.annotation.Nullable;

public class IdleTimeoutConfig {
    private static final String MODULE_NAME = "IdleTimeout";

    public static final BuilderCodec<IdleTimeoutConfig> CODEC = BuilderCodec.builder(
                    IdleTimeoutConfig.class,
                    IdleTimeoutConfig::new
            )
            .append(new KeyedCodec<>("TimeoutMinutes", Codec.INTEGER),
                    (o, s) -> o.timeoutMinutes = s,
                    o -> o.timeoutMinutes
            )
            .add()
            .build();

    private Integer timeoutMinutes = null;

    public static IdleTimeoutConfig load() {
        var serverConfig = HytaleServer.get().getConfig();
        var module = serverConfig.getModule(MODULE_NAME);
        var config = module.decode(CODEC);
        if (config == null) {
            config = new IdleTimeoutConfig();
        }
        return config;
    }

    @Nullable
    public Integer getTimeoutMinutes() {
        return timeoutMinutes;
    }
}
