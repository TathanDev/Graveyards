package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.common.attributes.MonsterFightData;
import fr.tathan.graveyards.common.datas.GravestoneData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        this.level.addFreshEntity(this.entity);
        this.entity.setPos(this.pos.getX() + random.nextInt(3), this.pos.getY() + random.nextInt(3), this.pos.getZ() + random.nextInt(3));
        if(this.entity instanceof LivingEntity mob) {
            mob.setLastHurtByMob(this.player);
            this.setItems(mob);
            this.setAttributes(mob);
        }
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
