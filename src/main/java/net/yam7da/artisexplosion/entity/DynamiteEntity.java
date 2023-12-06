package net.yam7da.artisexplosion.entity;

import net.minecraft.entity.Entity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.yam7da.artisexplosion.item.ArtIsExplosionItems;
import net.yam7da.artisexplosion.world.CustomExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DynamiteEntity extends ThrownItemEntity {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(DynamiteEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private float explosionRadius = 5f;
    public int ticksUntilRemoval;
    private int fuseTimer;

    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(40);
        this.setExplosionRadius(5f);
        this.ticksUntilRemoval = -1;
    }

    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, livingEntity, world);
        this.setFuse(40);
        this.setExplosionRadius(5f);
        this.ticksUntilRemoval = -1;
    }

    protected Item getDefaultItem() {
        return ArtIsExplosionItems.DYNAMITE;
    }

    protected CustomExplosion getExplosion() {
        return new CustomExplosion(this.getWorld(), this.getOwner(), this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), null, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (this.age > 1) {

            this.setVelocity(0, 0, 0);

            if (this.getTriggerType() == DynamiteTriggerType.IMPACT) {
                this.explode();
            }
        }
    }


    @Override
    public void tick() {
        if (this.ticksUntilRemoval > 0) {
            ticksUntilRemoval--;
            if (ticksUntilRemoval <= 0) {
                this.remove(RemovalReason.DISCARDED);
            }
        } else {
            super.tick();

            if (this.getWorld().getBlockState(this.getBlockPos()).isFullCube(this.getWorld(), this.getBlockPos())) {
                this.setPosition(this.prevX, this.prevY, this.prevZ);
            }

            // drop item if in water
            if (this.isSubmergedInWater() && this.disableInLiquid()) {
                this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(this.getDefaultItem())));
                this.remove(RemovalReason.DISCARDED);
            }

            // tick down the fuse, then blow up
            if (this.getTriggerType() == DynamiteTriggerType.FUSE) {
                // smoke particle for lit fuse
                if (this.getWorld().isClient) {
                    this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.3, this.getZ(), 0, 0, 0);
                }

                // shorten the fuse
                this.setFuse(this.getFuse() - 1);
                if (this.getFuse() <= 0) {
                    this.explode();
                }
            }
        }
    }

    public void explode() {
        if (this.ticksUntilRemoval == -1) {
            this.ticksUntilRemoval = 1;

            CustomExplosion explosion = this.getExplosion();
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(true);

            if (!this.getWorld().isClient()) {
                for (net.minecraft.entity.player.PlayerEntity playerEntity : this.getWorld().getPlayers()) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                    if (serverPlayerEntity.squaredDistanceTo(this.getX(), this.getY(), this.getZ()) < 4096.0D) {
                        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(this.getX(), this.getY(), this.getZ(), explosion.getPower(), explosion.getAffectedBlocks(), (Vec3d) explosion.getAffectedPlayers().get(serverPlayerEntity)));
                    }
                }
            }
        }
    }

    public boolean disableInLiquid() {
        return true;
    }

    public DynamiteTriggerType getTriggerType() {
        return DynamiteTriggerType.FUSE;
    }

    public void onTrackedDataSet(TrackedData<?> trackedData_1) {
        if (FUSE.equals(trackedData_1)) {
            this.fuseTimer = this.getFuse();
        }
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public void setFuse(int int_1) {
        this.dataTracker.set(FUSE, int_1);
        this.fuseTimer = int_1;
    }

    public int getFuseTimer() {
        return this.fuseTimer;
    }

    public float getExplosionRadius() {
        return this.explosionRadius;
    }

    public void setExplosionRadius(float float_1) {
        this.explosionRadius = float_1;
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItem(new ItemStack(item.getItem()));
        if (item.hasNbt() && item.getOrCreateNbt().contains("ExplosionRadius")) {
            this.setExplosionRadius(item.getOrCreateNbt().getFloat("ExplosionRadius"));
        }
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(FUSE, 40);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound NbtCompound_1) {
        NbtCompound_1.putShort("Fuse", (short) this.getFuseTimer());
        NbtCompound_1.putFloat("ExplosionRadius", this.getExplosionRadius());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound NbtCompound_1) {
        this.setFuse(NbtCompound_1.getShort("Fuse"));
        this.setExplosionRadius(NbtCompound_1.getFloat("ExplosionRadius"));
    }

    @Override
    protected ItemStack getItem() {
        return new ItemStack(this.getDefaultItem());
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public enum DynamiteTriggerType {
        FUSE,
        IMPACT
    }
}