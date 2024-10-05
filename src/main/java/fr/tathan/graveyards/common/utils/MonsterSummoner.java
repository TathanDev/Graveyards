package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.common.attributes.MonsterFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;


public class MonsterSummoner<T extends Entity> {

    public T entity;
    public BlockPos pos;
    public Player player;
    public Level level;

    public MonsterSummoner(EntityType<T> entityType, Player player, BlockPos pos) {
        this.entity = entityType.create(player.level());
        this.pos = pos;
        this.player = player;
        this.level = player.level();
    }

    public T summon() {

        Random random = new Random();

        this.entity.setData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA.get(), new MonsterFightData(player.getName().getString()));
        this.level.addFreshEntity(this.entity);
        this.entity.setPos(this.pos.getX() + random.nextInt(3), this.pos.getY() + random.nextInt(3), this.pos.getZ() + random.nextInt(3));
        if(this.entity instanceof LivingEntity mob) {
            mob.setLastHurtByMob(this.player);
        }
        return this.entity;

    }

}
