package fr.tathan.graveyards.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.tathan.graveyards.common.registries.CriterionsRegistry;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class UseAmuletTrigger extends SimpleCriterionTrigger<UseAmuletTrigger.Instance>
{
    @Override
    public Codec<Instance> codec()
    {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, ResourceKey<Item> item)
    {
        trigger(player, instance -> instance.matches(item));
    }

    public record Instance(Optional<ContextAwarePredicate> player, ResourceKey<Item> item) implements SimpleInstance
    {
        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                ResourceKey.codec(Registries.ITEM).fieldOf("item").forGetter(Instance::item)
        ).apply(instance, Instance::new));


        public static void trigger(ServerPlayer player, ResourceKey<Item> item) {
            CriterionsRegistry.USE_AMULET.get().trigger(player, item);
        }

        public static Criterion<Instance> criterion(ResourceKey<Item> item)
        {
            return CriterionsRegistry.USE_AMULET.get().createCriterion(new Instance(Optional.empty(), item));
        }


        public boolean matches(ResourceKey<Item> item) {
            // Since ItemPredicate matches a stack, we use a stack as the input here.
            return this.item == item;
        }
    }
}