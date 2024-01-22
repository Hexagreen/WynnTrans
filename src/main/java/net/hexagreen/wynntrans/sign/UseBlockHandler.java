package net.hexagreen.wynntrans.sign;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class UseBlockHandler implements UseBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if(player == MinecraftClient.getInstance().player && player.isHolding(Items.NETHER_STAR) && hand == Hand.MAIN_HAND && world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
            if(blockEntity instanceof SignBlockEntity) {
                WynnSign.get(((SignBlockEntity) blockEntity).getFrontText()).registerTranslation();
            }
        }

        return ActionResult.PASS;
    }
}
