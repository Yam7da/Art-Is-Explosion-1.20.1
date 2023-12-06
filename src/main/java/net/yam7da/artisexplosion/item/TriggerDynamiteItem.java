package net.yam7da.artisexplosion.item;

import net.yam7da.artisexplosion.entity.DynamiteEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class TriggerDynamiteItem extends DynamiteItem {
    public TriggerDynamiteItem(Settings settings, EntityType<DynamiteEntity> entityType) {
        super(settings, entityType);
    }

    @Override
    public void playSoundEffects(World world, PlayerEntity playerEntity) {
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.NEUTRAL, 0.8F, 0.5F);
    }
}