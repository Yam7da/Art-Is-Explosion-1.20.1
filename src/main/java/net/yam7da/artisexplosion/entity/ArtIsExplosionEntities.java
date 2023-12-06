package net.yam7da.artisexplosion.entity;

import net.yam7da.artisexplosion.ArtIsExplosionMod;
import net.yam7da.artisexplosion.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static net.yam7da.artisexplosion.ArtIsExplosionMod.MOD_ID;

public class ArtIsExplosionEntities {

    public static EntityType<DynamiteEntity> DYNAMITE;
    public static EntityType<DynamiteEntity> TRIGGER_DYNAMITE;

    public static EntityType<IctuDeCaeloEntity> ICTU_DE_CAELO;


    public static void init() {
        // throwable explosives
        DYNAMITE = register("bomb", createDynamiteEntityType(DynamiteEntity::new));
        TRIGGER_DYNAMITE = register("trigger_bomb", createDynamiteEntityType(TriggerDynamiteEntity::new));
        // explosive blocks
        ICTU_DE_CAELO = register("ictu_de_caelo", FabricEntityTypeBuilder.create(SpawnGroup.MISC, IctuDeCaeloEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).forceTrackedVelocityUpdates(true).build());
        // projectiles

        // custom falling block entity (Needed for claims protection)

    }

    private static <T extends Entity> EntityType<T> register(String s, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, MOD_ID + ":" + s, entityType);
    }

    private static <T extends Entity> EntityType<T> createDynamiteEntityType(EntityType.EntityFactory<T> factory) {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.25f, 0.25f)).trackRangeBlocks(64).trackedUpdateRate(1).forceTrackedVelocityUpdates(true).build();
    }

    public static void registerModEntities() {
        ArtIsExplosionMod.LOGGER.info("Registering Mod Entities for " + MOD_ID);
    }
}