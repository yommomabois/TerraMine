package terramine.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import terramine.common.init.ModComponents;
import terramine.common.network.ServerPacketHandler;

import java.util.HashMap;
import java.util.Map;

public class AccessoryVisibilityComponent implements Component, AutoSyncedComponent {
    public Map<Integer, Boolean> slotVisibility = new HashMap<>();
    public int size;
    public Player player;

    public AccessoryVisibilityComponent(Player player) {
        this.player = player;
    }

    public Player getEntity() {
        return this.player;
    }

    public void setSlotVisibility(int slot, boolean visible) {
        slotVisibility.put(slot, visible);
        if (!player.isLocalPlayer()) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeInt(slot);
            buf.writeBoolean(visible);
            buf.writeUUID(player.getUUID());
            for (ServerPlayer otherPlayer : player.getServer().getLevel(player.level().dimension()).players()) {
                ServerPlayNetworking.send(otherPlayer, ServerPacketHandler.UPDATE_ACCESSORY_VISIBILITY_PACKET_ID, buf);
            }
        }
    }

    public boolean getSlotVisibility(int slot) {
        return slotVisibility.getOrDefault(slot, true);
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return player == this.player;
    }

    @Override
    public void readFromNbt(@NotNull CompoundTag tag) {
        for (int i = 0; i < 7; i++) {
            if (tag.contains("slotVisibility/" + i)) {
                setSlotVisibility(i, tag.getBoolean("slotVisibility/" + i));
            }
        }
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        for (int i = 0; i < 7; i++) {
            setSlotVisibility(i, buf.readBoolean());
        }
    }

    @Override
    public void writeToNbt(@NotNull CompoundTag tag) {
        for (int i = 0; i < 7; i++) {
            tag.putBoolean("slotVisibility/" + i, getSlotVisibility(i));
        }
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer recipient) {
        for (int i = 0; i < 7; i++) {
            buf.writeBoolean(getSlotVisibility(i));
        }
    }
}
