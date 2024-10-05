package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.common.datas.MonsterFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;


public class MonsterSummoner<T extends Entity> {

    public T entity;
    public BlockPos pos;
    public String playerName;
    public Level level;

    public MonsterSummoner(EntityType<T> entityType, Level level, BlockPos pos, String playerName) {
        this.entity = entityType.create(level);
        this.pos = pos;
        this.playerName = playerName;
        this.level = level;
    }

    public T summon() {
        this.entity.setData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA.get(), new MonsterFightData(this.playerName));
        this.level.addFreshEntity(this.entity);

        this.entity.setPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());

        return this.entity;

    }

}
