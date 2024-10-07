package fr.tathan.graveyards.common.item;

import fr.tathan.graveyards.common.advancements.UseAmuletTrigger;
import fr.tathan.graveyards.common.config.CommonConfig;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;


public class AmuletOfVision extends Item {

    public AmuletOfVision(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(player.level().isClientSide) return super.use(level, player, usedHand);

        if(Utils.isPlayerFighting(player)) {
            AABB area = new AABB(player.blockPosition()).inflate(CommonConfig.amuletOfVisionRadius);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                if(entity.hasData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA.get())) {
                   if(entity.getData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA.get()).playerName().equals(player.getName().getString())) {
                       entity.setGlowingTag(true);
                   }
                }
            }
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.graveyards.amulet_of_vision", CommonConfig.amuletOfVisionRadius).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
