package net.yam7da.artisexplosion.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.yam7da.artisexplosion.ArtIsExplosionMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.yam7da.artisexplosion.entity.ArtIsExplosionEntities;
import net.yam7da.artisexplosion.entity.DynamiteEntity;

public class ArtIsExplosionItems {
    public static Item DYNAMITE;
    public static Item TRIGGER_DYNAMITE;

   public static void init() {
       DYNAMITE = registerItem(new DynamiteItem(new Item.Settings().maxCount(16), ArtIsExplosionEntities.DYNAMITE), "dynamite", ItemGroups.TOOLS);
       TRIGGER_DYNAMITE = registerItem(new TriggerDynamiteItem(new Item.Settings().maxCount(16), ArtIsExplosionEntities.TRIGGER_DYNAMITE), "trigger_dynamite", ItemGroups.TOOLS);
   }


    public static Item registerItem(Item item, String name, RegistryKey<ItemGroup> itemGroupKey) {
        if (item instanceof DynamiteItem) {
            registerItem(item, name, itemGroupKey, true);
        } else {
            registerItem(item, name, itemGroupKey, false);
        }
        return item;
    }

    public static Item registerItem(Item item, String name, RegistryKey<ItemGroup> itemGroupKey, boolean registerDispenserBehavior) {
        Registry.register(Registries.ITEM, ArtIsExplosionMod.MOD_ID + ":" + name, item);
        ItemGroupEvents.modifyEntriesEvent(itemGroupKey).register((entries) -> entries.add(item));

        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    DynamiteEntity dynamiteEntity = ((DynamiteItem) itemStack.getItem()).getType().create(world);
                    dynamiteEntity.setPos(position.getX(), position.getY(), position.getZ());
                    itemStack.decrement(1);
                    return dynamiteEntity;
                }
            });
        }

        return item;
    }
    public static void registerModItems() {
       ArtIsExplosionMod.LOGGER.info("Registering Mod Items for " + ArtIsExplosionMod.MOD_ID);
    }
}