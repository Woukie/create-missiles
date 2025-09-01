package net.woukie.createmissiles.levelgen.structure.pools;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class LiquidlessSinglePoolElement extends SinglePoolElement {
    public static final Codec<LiquidlessSinglePoolElement> CODEC;

    protected LiquidlessSinglePoolElement(Either<ResourceLocation, StructureTemplate> either, Holder<StructureProcessorList> holder, StructureTemplatePool.Projection projection) {
        super(either, holder, projection);
    }

    @Override
    protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox boundingBox, boolean bl) {
        var settings = super.getSettings(rotation, boundingBox, bl);
        settings.setKeepLiquids(false);
        return settings;
    }

    static {
        CODEC = RecordCodecBuilder.create((instance) -> instance.group(templateCodec(), processorsCodec(), projectionCodec()).apply(instance, LiquidlessSinglePoolElement::new));
    }
}
