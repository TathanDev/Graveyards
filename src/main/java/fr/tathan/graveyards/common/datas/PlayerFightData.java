package fr.tathan.graveyards.common.datas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;


public record PlayerFightData(boolean isFighting, int fightLevel, int monsters, BlockPos graveyardPos, ResourceLocation graveyardId) {

    public static final Codec<PlayerFightData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isFightinh").forGetter(PlayerFightData::isFighting),
            Codec.INT.fieldOf("fightLevel").forGetter(PlayerFightData::fightLevel),
            Codec.INT.fieldOf("monsters").forGetter(PlayerFightData::monsters),
            BlockPos.CODEC.fieldOf("graveyardPos").forGetter(PlayerFightData::graveyardPos),
            ResourceLocation.CODEC.fieldOf("graveyardId").forGetter(PlayerFightData::graveyardId)

    ).apply(instance, PlayerFightData::new));

}
