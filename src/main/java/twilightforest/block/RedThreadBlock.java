package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.RedThreadBlockEntity;

import javax.annotation.Nullable;

public class RedThreadBlock extends MultifaceBlock implements EntityBlock {
	public RedThreadBlock(Properties properties) {
		super(properties);
	}

	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
		return !ctx.getItemInHand().is(TFBlocks.RED_THREAD.get().asItem()) || super.canBeReplaced(state, ctx);
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedThreadBlockEntity(pos, state);
	}
}
