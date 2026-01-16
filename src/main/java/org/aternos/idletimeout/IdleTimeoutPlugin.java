package org.aternos.idletimeout;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IdleTimeoutPlugin extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Map<UUID, PlayerInfo> playerInfoMap = new HashMap<>();
    private IdleTimeoutConfig config;
    private boolean loaded = false;

    public IdleTimeoutPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // Prevent registering executor and event listeners multiple times on reload
        if (loaded) return;
        loaded = true;

        LOGGER.atInfo().log("Setting up plugin " + this.getName());


        config = IdleTimeoutConfig.load();

        if (config.getTimeoutMinutes() != null) {
            HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(this::updatePlayers, 1000, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);
            this.getEventRegistry().register(PlayerDisconnectEvent.class, event -> playerInfoMap.remove(event.getPlayerRef().getUuid()));
        }
    }

    protected PlayerInfo getPlayerInfo(PlayerRef playerRef) {
        return playerInfoMap.computeIfAbsent(playerRef.getUuid(), _ -> new PlayerInfo(playerRef));
    }

    protected void updatePlayers() {
        Universe universe = Universe.get();
        for (var player : universe.getPlayers()) {
            PlayerInfo info = getPlayerInfo(player);
            info.update(player);

            var timeout = config.getTimeoutMinutes();
            assert timeout != null;
            var kickAfter = info.getLastActiveTime().plus(timeout, ChronoUnit.MINUTES);
            if (Instant.now().isAfter(kickAfter)) {
                LOGGER.atInfo().log("Kicking idle player " + info.getUuid());
                player.getPacketHandler().disconnect("You have been kicked for being idle too long.");
            }
        }
    }
}
