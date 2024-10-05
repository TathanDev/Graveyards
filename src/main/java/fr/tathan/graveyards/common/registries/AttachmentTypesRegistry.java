package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.attributes.MonsterFightData;
import fr.tathan.graveyards.common.attributes.PlayerFightData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentTypesRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Graveyards.MODID);

    public static final PlayerFightData DEFAULT_PLAYER_FIGHT_DATA = new PlayerFightData(false, 0, 0, BlockPos.ZERO, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "null"));

    public static final Supplier<AttachmentType<PlayerFightData>> PLAYER_FIGHT_DATA = ATTACHMENT_TYPES.register(
            "player_fight_ata", () -> AttachmentType.builder(() -> DEFAULT_PLAYER_FIGHT_DATA)
                    .serialize(PlayerFightData.CODEC).build()
    );

    public static final Supplier<AttachmentType<MonsterFightData>> MONSTER_FIGHT_DATA = ATTACHMENT_TYPES.register(
            "monster_fight_data", () -> AttachmentType.builder(() -> new MonsterFightData("Notch")) //I Seriusly Hope that Notch will never player this mod
                    .serialize(MonsterFightData.CODEC).build()
    );


}
