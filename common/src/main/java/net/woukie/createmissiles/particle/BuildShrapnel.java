package net.woukie.createmissiles.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Random;

public class BuildShrapnel extends TextureSheetParticle {
    private final static double spread = 0.25D;
    private final static float initialVelocity = 0.25f;
    private final double spinSpeed;

    public BuildShrapnel(ClientLevel arg, double x, double y, double z, double vX, double vY, double vZ, SpriteSet arg2) {
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
        this.xd = vX + velocity.x;
        this.yd = vY + velocity.y;
        this.zd = vZ + velocity.z;
        this.quadSize = 0f;
        this.lifetime = (int)(100 + (double)this.random.nextFloat() * 10);
        this.spinSpeed = Math.random() - 0.5;
        this.setSprite(arg2.get((int) (this.random.nextFloat() * 100f), 100));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (onGround) {
            age += 4;
        } else {
            this.roll += (float) spinSpeed;
            this.oRoll = roll;
        }

        float lifePercent = 1 - (float) (age + 1) / lifetime;
        this.alpha = Math.min(lifePercent * 4f, 1);

//        https://easings.net/#easeInExpo
        quadSize = lifePercent == 1 ? 1 : (float) (1 - Math.pow(2, -10 * lifePercent));
        quadSize /= 12;


        super.tick();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet arg) {
            this.sprites = arg;
        }

        public Particle createParticle(@NotNull SimpleParticleType arg, @NotNull ClientLevel arg2, double d, double e, double f, double g, double h, double i) {
            return new BuildShrapnel(arg2, d, e, f, g, h, i, this.sprites);
        }
    }
}
