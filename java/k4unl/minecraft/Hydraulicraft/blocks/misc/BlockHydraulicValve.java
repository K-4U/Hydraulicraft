package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTextureContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicValve extends BlockConnectedTextureContainer {
/*
    @SideOnly(Side.CLIENT)
	public IIcon[] icons;
	@SideOnly(Side.CLIENT)
	public IIcon[] rotatedIcons;
*/

    public BlockHydraulicValve() {

        super(Names.blockValve);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicValve();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

	/*
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {

		return icons[0];
	}*/

	/*
	@SideOnly(Side.CLIENT)
	public boolean connectTo(IConnectTexture it, IBlockAccess world, int x, int y, int z){
		if(world.getBlock(x, y, z) == this){
			return true;
		}else if(it != null && it.connectTextureTo(world.getBlock(x, y, z))){
			return true;
		}

		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null){
			if(te instanceof IConnectTexture){
				return ((IConnectTexture)te).connectTexture();
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

		boolean[] bitMatrix = new boolean[8];
		TileEntity te = world.getTileEntity(x, y, z);
		IConnectTexture it = null;
		if(te instanceof IConnectTexture){
			it = ((IConnectTexture)te);
		}

		if (side == 0 || side == 1) {
			bitMatrix[0] = connectTo(it, world, x - 1, y, z - 1);
			bitMatrix[1] = connectTo(it, world, x, y, z - 1);
			bitMatrix[2] = connectTo(it, world, x + 1, y, z - 1);
			bitMatrix[3] = connectTo(it, world, x - 1, y, z);
			bitMatrix[4] = connectTo(it, world, x + 1, y, z);
			bitMatrix[5] = connectTo(it, world, x - 1, y, z + 1);
			bitMatrix[6] = connectTo(it, world, x, y, z + 1);
			bitMatrix[7] = connectTo(it, world, x + 1, y, z + 1);
		}
		if (side == 2 || side == 3) {
			bitMatrix[0] = connectTo(it, world, x + (side == 2 ? 1 : -1), y + 1, z);
			bitMatrix[1] = connectTo(it, world, x, y + 1, z);
			bitMatrix[2] = connectTo(it, world, x + (side == 3 ? 1 : -1), y + 1, z);
			bitMatrix[3] = connectTo(it, world, x + (side == 2 ? 1 : -1), y, z);
			bitMatrix[4] = connectTo(it, world, x + (side == 3 ? 1 : -1), y, z);
			bitMatrix[5] = connectTo(it, world, x + (side == 2 ? 1 : -1), y - 1, z);
			bitMatrix[6] = connectTo(it, world, x, y - 1, z);
			bitMatrix[7] = connectTo(it, world, x + (side == 3 ? 1 : -1), y - 1, z);
		}
		if (side == 4 || side == 5) {
			bitMatrix[0] = connectTo(it, world, x, y + 1, z + (side == 5 ? 1 : -1));
			bitMatrix[1] = connectTo(it, world, x, y + 1, z);
			bitMatrix[2] = connectTo(it, world, x, y + 1, z + (side == 4 ? 1 : -1));
			bitMatrix[3] = connectTo(it, world, x, y, z + (side == 5 ? 1 : -1));
			bitMatrix[4] = connectTo(it, world, x, y, z + (side == 4 ? 1 : -1));
			bitMatrix[5] = connectTo(it, world, x, y - 1, z + (side == 5 ? 1 : -1));
			bitMatrix[6] = connectTo(it, world, x, y - 1, z);
			bitMatrix[7] = connectTo(it, world, x, y - 1, z + (side == 4 ? 1 : -1));
		}

		int idBuilder = 0;

		for (int i = 0; i <= 7; i++)
			idBuilder = idBuilder
					+ (bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64
					: 128))))))) : 0);


		EnumFacing s = EnumFacing.getOrientation(side);
		if(s.equals(EnumFacing.SOUTH) || s.equals(EnumFacing.WEST)){
			return idBuilder > 255 || idBuilder < 0 ? icons[0] : rotatedIcons[iconRefByID[idBuilder]];
		}

		return idBuilder > 255 || idBuilder < 0 ? icons[0] : icons[iconRefByID[idBuilder]];

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {

		icons = new IIcon[iconRefByID.length];
		rotatedIcons = new IIcon[iconRefByID.length];

		for (int i = 0; i < 47; i++)
			icons[i] = iconRegister.registerIcon(ModInfo.ID + ":" + mName.unlocalized + "/" + mName.unlocalized + "_" + (i + 1));
		for (int i = 0; i < 47; i++)
			rotatedIcons[i] = iconRegister.registerIcon(ModInfo.ID + ":" + mName.unlocalized + "/" + mName.unlocalized + "_rotated_" + (i + 1));
	}
*/
}
