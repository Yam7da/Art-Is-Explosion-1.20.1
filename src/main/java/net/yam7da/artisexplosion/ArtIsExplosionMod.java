package net.yam7da.artisexplosion;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Direction;
import net.yam7da.artisexplosion.block.ArtIsExplosionBlocks;
import net.yam7da.artisexplosion.entity.ArtIsExplosionEntities;
import net.yam7da.artisexplosion.item.ArtIsExplosionItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArtIsExplosionMod implements ModInitializer {
	public static final String MOD_ID = "art-is-explosion-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TrackedDataHandler<Direction> FACING = new TrackedDataHandler<>() {
		public void write(PacketByteBuf packetByteBuf, Direction direction) {
			packetByteBuf.writeEnumConstant(direction);
		}

		public Direction read(PacketByteBuf packetByteBuf) {
			return packetByteBuf.readEnumConstant(Direction.class);
		}

		public Direction copy(Direction direction) {
			return direction;
		}
	};

	@Override
	public void onInitialize() {
		TrackedDataHandlerRegistry.register(FACING);
		ArtIsExplosionEntities.init();
		ArtIsExplosionItems.init();
		ArtIsExplosionBlocks.init();
	}
}