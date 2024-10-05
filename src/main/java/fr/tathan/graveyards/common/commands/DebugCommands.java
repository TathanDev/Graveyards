package fr.tathan.graveyards.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.tathan.graveyards.common.attributes.PlayerFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

public class DebugCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("debug")
                .then(Commands.literal("isDueling")
                        .executes((CommandContext<CommandSourceStack> context) -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
                            player.sendSystemMessage(Component.literal("Is dueling : " + data.isFighting()  + ". He need to kill " + data.monsters()));
                            return 0;
                        }))
                        .then(Commands.literal("loadedGraveyards")
                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                        .executes((CommandContext<CommandSourceStack> context) -> {
                                            ServerPlayer player = context.getSource().getPlayer();

                                            int level = context.getArgument("level", Integer.class);
                                            player.sendSystemMessage(Component.literal("Loaded graveyards : " + Utils.randomGraveyard(level)));
                                            return 0;
                                        })))

                .then(Commands.literal("setDueling")
                        .then(Commands.argument("dueling", IntegerArgumentType.integer())
                                .executes((CommandContext<CommandSourceStack> context) -> {
                                    ServerPlayer player = context.getSource().getPlayer();

                                    int isDueling = context.getArgument("dueling", Integer.class);

                                    if(isDueling <= 0) {
                                        Utils.finishDuel(player);
                                    } else {
                                        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(true, context.getArgument("level", Integer.class), 0, new BlockPos(-64, -64, -64 ), Utils.randomGraveyard(Mth.clamp(isDueling, 1, 3)).id()));
                                    }
                                    return 0;
                                })))
                        );

    }
}
