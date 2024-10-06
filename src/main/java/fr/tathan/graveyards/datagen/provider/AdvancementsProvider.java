package fr.tathan.graveyards.datagen.provider;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.advancements.ActivateGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UpgradeGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UseAmuletTrigger;
import fr.tathan.graveyards.common.registries.ItemRegistry;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementsProvider extends AdvancementProvider {


    public AdvancementsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new GraveyardAdvancements()));
    }

    private static final class GraveyardAdvancements implements AdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            Advancement.Builder gravestoneActivate = Advancement.Builder.advancement();
            Advancement.Builder goldUpgrade = Advancement.Builder.advancement();
            Advancement.Builder goldActivate = Advancement.Builder.advancement();
            Advancement.Builder amuletOfForgiveness = Advancement.Builder.advancement();
            Advancement.Builder diamondUpgrade = Advancement.Builder.advancement();
            Advancement.Builder diamondActivate = Advancement.Builder.advancement();

            gravestoneActivate
                    .display(
                    new ItemStack(ItemRegistry.GRAVEYARD.get()),
                    Component.translatable("advancements.graveyards.activate_gravestone.title"),
                    Component.translatable("advancements.graveyards.activate_gravestone.description"),
                    ResourceLocation.withDefaultNamespace("textures/block/andesite.png"),
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                ).addCriterion("activate_gravestone", ActivateGravestoneTrigger.Instance.criterion(1))
                .requirements(AdvancementRequirements.allOf(List.of("activate_gravestone")))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "activate_gravestone"), existingFileHelper);

            goldUpgrade
                    .parent(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "activate_gravestone"))
                    .display(
                            new ItemStack(Items.GOLD_INGOT),
                            Component.translatable("advancements.graveyards.upgrade_gold.title"),
                            Component.translatable("advancements.graveyards.upgrade_gold.description"),
                            ResourceLocation.withDefaultNamespace("textures/block/andesite_block"),
                            AdvancementType.TASK,
                            true,
                            false,
                            false
                    ).addCriterion("upgrade_gold", UpgradeGravestoneTrigger.Instance.criterion(2))
                    .requirements(AdvancementRequirements.allOf(List.of("upgrade_gold")))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "upgrade_gold"), existingFileHelper);

            goldActivate
                    .parent(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "upgrade_gold"))
                    .display(
                            new ItemStack(ItemRegistry.GOLD_GRAVESTONE.get()),
                            Component.translatable("advancements.graveyards.activate_gold_gravestone.title"),
                            Component.translatable("advancements.graveyards.activate_gold_gravestone.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    ).addCriterion("activate_gravestone", ActivateGravestoneTrigger.Instance.criterion(2))
                    .requirements(AdvancementRequirements.allOf(List.of("activate_gravestone")))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "activate_gold_gravestone"), existingFileHelper);


            diamondUpgrade
                    .parent(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "upgrade_gold"))
                    .display(
                            new ItemStack(Items.DIAMOND),
                            Component.translatable("advancements.graveyards.upgrade_diamond.title"),
                            Component.translatable("advancements.graveyards.upgrade_diamond.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            false,
                            false
                    ).addCriterion("upgrade_diamond", UpgradeGravestoneTrigger.Instance.criterion(3))
                    .requirements(AdvancementRequirements.allOf(List.of("upgrade_diamond")))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "upgrade_diamond"), existingFileHelper);

            diamondActivate
                    .parent(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "upgrade_diamond"))
                    .display(
                            new ItemStack(ItemRegistry.DIAMOND_GRAVESTONE.get()),
                            Component.translatable("advancements.graveyards.activate_diamond_gravestone.title"),
                            Component.translatable("advancements.graveyards.activate_diamond_gravestone.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    ).addCriterion("activate_gravestone", ActivateGravestoneTrigger.Instance.criterion(3))
                    .requirements(AdvancementRequirements.allOf(List.of("activate_gravestone")))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "activate_diamond_gravestone"), existingFileHelper);


            amuletOfForgiveness
                    .parent(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "activate_gravestone"))
                    .display(
                            new ItemStack(ItemRegistry.AMULET_OF_FORGIVENESS.get()),
                            Component.translatable("advancements.graveyards.amulet_of_forgiveness.title"),
                            Component.translatable("advancements.graveyards.amulet_of_forgiveness.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    ).addCriterion("use_amulet", UseAmuletTrigger.Instance.criterion(ItemRegistry.AMULET_OF_FORGIVENESS.getKey()))
                    .requirements(AdvancementRequirements.allOf(List.of("use_amulet")))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "amulet_of_forgiveness"), existingFileHelper);


        }
    }

}
