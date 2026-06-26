package io.github.orlisan.mapwasher.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public class CauldronMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void injectMapWashing(final ItemStack itemStack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (itemStack.getItem() == Items.FILLED_MAP && state.is(Blocks.WATER_CAULDRON)) {
            if (!level.isClientSide()) {
                ItemStack newStack = new ItemStack(Items.MAP, itemStack.getCount());
                player.setItemInHand(hand, newStack);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
