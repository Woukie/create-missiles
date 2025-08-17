package net.woukie.createmissiles.block.navigationpanel.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.function.Supplier;

public class UpdateMapDataMessage {
    public final int mapId;
    public final CompoundTag data;

    public UpdateMapDataMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readNbt());
    }

    public UpdateMapDataMessage(int mapId, CompoundTag data) {
        this.mapId = mapId;
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(mapId);
        buf.writeNbt(data);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        level.overrideMapData("map_" + mapId, MapItemSavedData.load(data));
    }
}
