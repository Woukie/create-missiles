package net.woukie.createmissiles.client.models.warheads;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;

public class TunnelerWarheadModel extends ExcavatorWarheadModel {
	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/tunneler.png");
	}
}