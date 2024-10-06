package fr.tathan.graveyards.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.tathan.graveyards.common.registries.CriterionsRegistry;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class ActivateGravestoneTrigger extends SimpleCriterionTrigger<ActivateGravestoneTrigger.Instance>
{
    @Override
    public Codec<Instance> codec()
    {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, int level)
    {
        trigger(player, instance -> instance.matches(level));
    }

    public record Instance(Optional<ContextAwarePredicate> player, int level) implements SimpleCriterionTrigger.SimpleInstance
    {
        private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                Codec.INT.fieldOf("gravestone_level").forGetter(Instance::level)
        ).apply(instance, Instance::new));

        public static void trigger(ServerPlayer player, int level) {
            // Run code to perform action here
            CriterionsRegistry.ACTIVATE_GRAVESTONE.get().trigger(player, level);
        }

        public static Criterion<Instance> criterion(int level)
        {
            return CriterionsRegistry.ACTIVATE_GRAVESTONE.get().createCriterion(new Instance(Optional.empty(), level));
        }


        public boolean matches(int level) {
            // Since ItemPredicate matches a stack, we use a stack as the input here.
            return this.level == level;
        }
    }
}