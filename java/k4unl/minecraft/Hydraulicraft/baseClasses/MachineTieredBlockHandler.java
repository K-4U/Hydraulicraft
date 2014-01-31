package k4unl.minecraft.Hydraulicraft.baseClasses;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MachineTieredBlockHandler extends ItemBlock {
	private Name[] tNames;
	
	
	public MachineTieredBlockHandler(int blockId, Name[] names) {
		super(blockId);
		
		tNames = names;
		
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		return tNames[itemStack.getItemDamage()].unlocalized;
	}
	
	@Override
	public int getMetadata(int damage){
		return damage;
	}
	

	/********************
	 * FMP
	 */
	@SideOnly(Side.CLIENT)
    public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3,
            int par4, int par5, EntityPlayer par6EntityPlayer,
            ItemStack par7ItemStack) {

        if (tryPlaceMultiPart(par1World,
                    new BlockCoord(par2, par3, par4).offset(par5), par7ItemStack,
                    par5, false)) { return true; }
        return super.canPlaceItemBlockOnSide(par1World, par2, par3, par4, par5,
                par6EntityPlayer, par7ItemStack);
    }

    public TMultiPart createMultiPart(World world, BlockCoord pos,
            ItemStack item, int side) {

        return null;
    }

    public boolean tryPlaceMultiPart(World world, BlockCoord pos,
            ItemStack item, int side, boolean doPlace) {

        TileMultipart tile = TileMultipart.getOrConvertTile(world, pos);

        if (tile == null) { return false; }
        TMultiPart part = createMultiPart(world, pos, item, side);

        if (part == null) { return false; }
        if (tile.canAddPart(part)) {
            if (doPlace) {
                TileMultipart.addPart(world, pos, part);
            }
            return true;
        }

        return false;
    }

    public boolean onItemUse(ItemStack par1ItemStack,
            EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
            int par6, int par7, float par8, float par9, float par10) {

        if ((par8 != 0.0F) && (par9 != 0.0F) && (par10 != 0.0F)
                && (par8 != 1.0F) && (par9 != 1.0F) && (par10 != 1.0F)) {
            BlockCoord pos = new BlockCoord(par4, par5, par6);
            if (tryPlaceMultiPart(par3World, pos, par1ItemStack, par7,
                        !par3World.isRemote)) {
                Block block = Block.blocksList[getBlockID()];
                par3World.playSoundEffect(pos.x + 0.5F, pos.y + 0.5F,
                        pos.z + 0.5F, block.stepSound.getPlaceSound(),
                        (block.stepSound.getVolume() + 1.0F) / 2.0F,
                        block.stepSound.getPitch() * 0.8F);

                par1ItemStack.stackSize -= 1;
                return true;
            }
                }

        int i1 = par3World.getBlockId(par4, par5, par6);

        if ((i1 != Block.snow.blockID)
                || ((par3World.getBlockMetadata(par4, par5, par6) & 0x7) >= 1)) {
            if ((i1 != Block.vine.blockID)
                    && (i1 != Block.tallGrass.blockID)
                    && (i1 != Block.deadBush.blockID)
                    && ((Block.blocksList[i1] == null) || (!Block.blocksList[i1]
                            .isBlockReplaceable(par3World, par4, par5, par6)))) {
                BlockCoord pos = new BlockCoord(par4, par5, par6).offset(par7);

                if (tryPlaceMultiPart(par3World, pos, par1ItemStack, par7,
                            !par3World.isRemote)) {
                    Block block = Block.blocksList[getBlockID()];
                    par3World.playSoundEffect(pos.x + 0.5F, pos.y + 0.5F,
                            pos.z + 0.5F, block.stepSound.getPlaceSound(),
                            (block.stepSound.getVolume() + 1.0F) / 2.0F,
                            block.stepSound.getPitch() * 0.8F);

                    par1ItemStack.stackSize -= 1;
                    return true;
                }
                            }
                }
        return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World,
                par4, par5, par6, par7, par8, par9, par10);
    }
	
}

