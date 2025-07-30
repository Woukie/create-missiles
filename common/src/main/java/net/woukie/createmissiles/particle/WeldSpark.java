package net.woukie.createmissiles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.Random;

public class WeldSpark extends TextureSheetParticle {
    private final static double spread = 0.25D;
    private final static float initialVelocity = 0.25f;
    private final SpriteSet sprites;

    public WeldSpark(ClientLevel arg, double x, double y, double z, double vX, double vY, double vZ, SpriteSet arg2) {
        super(arg, x, y, z);

        var rand = new Random((long) ((x + y + z) * 32D));
        Vector3f velocity = new Vector3f(0, initialVelocity, 0);
        velocity.rotateX(rand.nextFloat((float) (0.25 * Math.PI), (float) (0.4 * Math.PI)));
        velocity.rotateY((float)(rand.nextFloat() * 2 * Math.PI));

        velocity.rotateX((float)(-this.random.nextFloat() * spread * Math.PI));
        velocity.rotateY((float)(-this.random.nextFloat() * spread * Math.PI));
        velocity.rotateZ((float)(-this.random.nextFloat() * spread * Math.PI));

        this.bbHeight = 1/16f;
        this.bbWidth = 1/16f;
        this.gravity = 1F;
        this.friction = 0.99F;
        this.sprites = arg2;
        this.xd = vX + velocity.x;
        this.yd = vY + velocity.y;
        this.zd = vZ + velocity.z;
        this.quadSize = this.random.nextFloat() / 16f + 1/32f;
        this.lifetime = (int)(100 + (double)this.random.nextFloat() * 10);
        this.setSpriteFromAge(arg2);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        Vector3d velocity = new Vector3d(xd, yd, zd);
        this.roll = (float)velocity.angle(new Vector3d(0D, 1D, 0D)) + (float) Math.PI / 2;
        this.oRoll = roll;
        float lifePercent = 1 - (float) (age + 1) / lifetime;
        this.alpha = Math.min(lifePercent * 4f, 1);

        setColor(1.0f, lifePercent / 2.0f + 0.5f, lifePercent);
        quadSize -= (float) (quadSize * 0.1);

        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet arg) {
            this.sprites = arg;
        }

        public Particle createParticle(@NotNull SimpleParticleType arg, @NotNull ClientLevel arg2, double d, double e, double f, double g, double h, double i) {
            return new WeldSpark(arg2, d, e, f, g, h, i, this.sprites);
        }
    }
}
