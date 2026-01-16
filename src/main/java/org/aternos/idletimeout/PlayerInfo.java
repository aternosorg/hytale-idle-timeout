package org.aternos.idletimeout;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.time.Instant;
import java.util.UUID;

public class PlayerInfo {
    private final UUID uuid;
    private Instant lastActiveTime;
    private Transform lastTransform;

    public PlayerInfo(PlayerRef player) {
        this.uuid = player.getUuid();
        this.lastActiveTime = Instant.now();
        this.lastTransform = player.getTransform();
    }

    public void update(PlayerRef player) {
        if (!lastTransform.equals(player.getTransform())) {
            updateActiveTime();
        }
        lastTransform = player.getTransform().clone();
    }

    public void updateActiveTime() {
        lastActiveTime = Instant.now();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Instant getLastActiveTime() {
        return lastActiveTime;
    }
}
