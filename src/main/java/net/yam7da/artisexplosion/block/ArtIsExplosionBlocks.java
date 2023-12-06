package net.yam7da.artisexplosion.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.yam7da.artisexplosion.ArtIsExplosionMod;
import net.yam7da.artisexplosion.entity.ArtIsExplosionEntities;
import net.yam7da.artisexplosion.item.ArtIsExplosionItems;
import org.jetbrains.annotations.Nullable;

import static net.yam7da.artisexplosion.ArtIsExplosionMod.MOD_ID;

public class ArtIsExplosionBlocks {
    public static Block ICTU_DE_CAELO;

    public static void init() {
        ICTU_DE_CAELO = registerBlock(new IctuDeCaeloBlock(FabricBlockSettings.create().strength(500.0f, 1200.0f).sounds(BlockSoundGroup.LANTERN)), "ictu_de_caelo", ItemGroups.REDSTONE);
    }

    private static Block registerBlock(Block block, String name, @Nullable RegistryKey<ItemGroup> itemGroupKey) {
        return registerBlock(block, name, itemGroupKey, true);
    }

    private static Block registerBlock(Block block, String name, @Nullable RegistryKey<ItemGroup> itemGroupKey, boolean registerBlockItem) {
        Registry.register(Registries.BLOCK, MOD_ID + ":" + name, block);

        if (registerBlockItem) {
            var blockItem = new BlockItem(block, new Item.Settings());
            blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem);
            ArtIsExplosionItems.registerItem(blockItem, name, itemGroupKey);
        }
        return block;
    }

    public static void registerModBlocks() {
        ArtIsExplosionMod.LOGGER.info("Registering Mod Blocks for " + ArtIsExplosionMod.MOD_ID);
    }
}