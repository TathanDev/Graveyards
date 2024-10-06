package fr.tathan.graveyards.common.item;

import fr.tathan.graveyards.common.advancements.UpgradeGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UseAmuletTrigger;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class AmuletOfForgivness extends Item {

    public AmuletOfForgivness(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(player.level().isClientSide) return super.use(level, player, usedHand);

        if(Utils.isPlayerFighting(player)) {
            Utils.stopPlayerFighting(player);
            player.getItemInHand(usedHand).shrink(1);

            if(player instanceof ServerPlayer) UseAmuletTrigger.Instance.trigger((ServerPlayer) player, player.getItemInHand(usedHand).getItem().builtInRegistryHolder().key());

            return InteractionResultHolder.success(player.getItemInHand(usedHand));

        }


        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }
}
