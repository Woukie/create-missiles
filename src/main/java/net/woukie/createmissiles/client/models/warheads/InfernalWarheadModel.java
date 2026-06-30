package net.woukie.createmissiles.client.models.warheads;

import net.minecraft.resources.ResourceLocation;
import net.woukie.createmissiles.CreateMissiles;

public class InfernalWarheadModel extends FlamingWarheadModel {
	@Override
	public ResourceLocation getTexture(int stage) {
		return ResourceLocation.fromNamespaceAndPath(CreateMissiles.MOD_ID,"textures/entity/infernal_warhead.png");
	}
}