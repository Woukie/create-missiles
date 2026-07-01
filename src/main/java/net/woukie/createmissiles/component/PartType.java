package net.woukie.createmissiles.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PartType(String value) {
    public static final Codec<PartType> CODEC = Codec.STRING
            .fieldOf("PartType")
            .xmap(PartType::new, PartType::value)
            .codec();

    public static final StreamCodec<ByteBuf, PartType> STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(PartType::new, PartType::value);
}