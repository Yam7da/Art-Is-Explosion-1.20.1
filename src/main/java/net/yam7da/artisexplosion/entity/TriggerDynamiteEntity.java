package net.yam7da.artisexplosion.entity;

import net.yam7da.artisexplosion.item.ArtIsExplosionItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class TriggerDynamiteEntity extends DynamiteEntity {
    public TriggerDynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World world) {
        super(entityType, world);
    }

    public TriggerDynamiteEntity(EntityType<? extends DynamiteEntity> entityType, World world, LivingEntity livingEntity) {
        super(entityType, world, livingEntity);
    }

    @Override
    protected Item getDefaultItem() {
        return ArtIsExplosionItems.TRIGGER_DYNAMITE;
    }

    @Override
    public DynamiteTriggerType getTriggerType() {
        return DynamiteTriggerType.IMPACT;
    }

    // play the click, although you can barely hear it, but you know, details
    @Override
    protected void onCollision(HitResult hitResult) {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.NEUTRAL, 1F, 0.6F);
        super.onCollision(hitResult);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }

}