package net.yam7da.artisexplosion.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.yam7da.artisexplosion.block.ArtIsExplosionBlocks;
import net.yam7da.artisexplosion.world.CustomExplosion;

public class IctuDeCaeloEntity extends DynamiteEntity{
    public IctuDeCaeloEntity(EntityType<? extends DynamiteEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(80);
        this.setExplosionRadius(2f);
    }


    @Override
    protected CustomExplosion getExplosion() {
        return new CustomExplosion(this.getWorld(), this, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), CustomExplosion.BlockBreakEffect.FIERY, Explosion.DestructionType.DESTROY);
    }

    @Override
    protected Item getDefaultItem() {
        return ArtIsExplosionBlocks.ICTU_DE_CAELO.asItem();
    }

    @Override
    public void tick() {
        super.tick();
        this.setOnGround(true);
    }

    @Override
    public boolean disableInLiquid() {
        return false;
    }
}
