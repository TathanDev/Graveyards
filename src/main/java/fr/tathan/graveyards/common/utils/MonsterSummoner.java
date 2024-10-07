package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.common.attributes.MonsterFightData;
import fr.tathan.graveyards.common.datas.GravestoneData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Random;


public class MonsterSummoner<T extends Entity> {

    public T entity;
    public final BlockPos pos;
    public final Player player;
    public final Level level;
    public final GravestoneData.Monster monster;

    public MonsterSummoner(EntityType<T> entityType, GravestoneData.Monster monster, Player player, BlockPos pos) {
        this.monster = monster;
        this.entity = entityType.create(player.level());
        this.pos = pos;
        this.player = player;
        this.level = player.level();
    }

    public T summon() {

        Random random = new Random();

        this.entity.setData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA.get(), new MonsterFightData(player.getName().getString()));
        this.entity.setPos(this.pos.getX() + random.nextInt(3), this.pos.getY() + random.nextInt(3), this.pos.getZ() + random.nextInt(3));
        if(this.entity instanceof LivingEntity livingEntity) {
            this.setItems(livingEntity);
            this.setAttributes(livingEntity);

            if (livingEntity instanceof Mob mob) {
                mob.setTarget(this.player);
                if (!this.level.isClientSide) mob.finalizeSpawn((ServerLevel) this.level, this.level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null);
            }

            if(livingEntity instanceof Warden warden) {
                warden.setAttackTarget(this.player);
                warden.increaseAngerAt(this.player);
            }
        }
        this.level.addFreshEntity(this.entity);
        return this.entity;

    }

    private void setAttributes(LivingEntity mob) {
        this.monster.attributes().ifPresent(attributes -> {
            attributes.forEach((attribute) -> {

                Holder<Attribute> attributeHolder = this.level.registryAccess().registryOrThrow(Registries.ATTRIBUTE).getHolderOrThrow(attribute.attribute());

                if (mob.getAttributes().hasAttribute(attributeHolder)) {
                    mob.getAttribute(attributeHolder).setBaseValue(attribute.baseValue());

                }
            });
        });
    }

    private void setItems(LivingEntity mob) {
        this.monster.items().ifPresent(equipments -> {
            equipments.forEach(monsterItem -> {
                mob.setItemSlot(monsterItem.slot(), monsterItem.stack());
            });
        });
    }

}
