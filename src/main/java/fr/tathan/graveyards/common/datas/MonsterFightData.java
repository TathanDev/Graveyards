package fr.tathan.graveyards.common.datas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public record MonsterFightData(String playerName) {

    public static final Codec<MonsterFightData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("playerName").forGetter(MonsterFightData::playerName)
    ).apply(instance, MonsterFightData::new));

}
