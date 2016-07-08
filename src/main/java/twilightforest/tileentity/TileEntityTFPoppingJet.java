package twilightforest.tileentity;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.FireJetVariant;

public class TileEntityTFPoppingJet extends TileEntity implements ITickable {

	private int counter = 0;
	private FireJetVariant nextVariant;

    public TileEntityTFPoppingJet(FireJetVariant variant) {
		this.nextVariant = variant;
	}

    @Override
	public void update()
    {
		if (++counter >= 80)
		{
			counter = 0;
	    	// turn to flame
			if (!worldObj.isRemote && worldObj.getBlockState(pos).getBlock() == TFBlocks.fireJet)
			{
				worldObj.setBlockState(pos, TFBlocks.fireJet.getDefaultState().withProperty(BlockTFFireJet.VARIANT, nextVariant), 3);
			}
			this.invalidate();
		}
		else
		{
			if (counter % 20 == 0) {
				worldObj.spawnParticle(EnumParticleTypes.LAVA, this.pos.getX() + 0.5, this.pos.getY() + 1.5, this.pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
				worldObj.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + worldObj.rand.nextFloat() * 0.2F, 0.9F + worldObj.rand.nextFloat() * 0.15F);
			}

		}

    }
    
    @Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        this.nextVariant = FireJetVariant.values()[par1NBTTagCompound.getInteger("NextMeta")];
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	NBTTagCompound ret = super.writeToNBT(par1NBTTagCompound);
        ret.setInteger("NextMeta", nextVariant.ordinal());
		return ret;
    }
}
