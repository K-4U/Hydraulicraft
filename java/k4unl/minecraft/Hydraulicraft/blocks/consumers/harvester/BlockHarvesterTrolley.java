package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;


import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockHarvesterTrolley extends HydraulicBlockContainerBase {

    private List<Integer> enabledHarvesters = new ArrayList<Integer>();

    public BlockHarvesterTrolley() {

        super(Names.blockHarvesterTrolley, true);

        mName = Names.blockHarvesterTrolley;
        hasTextures = false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return -1;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    /*@Override
    public boolean renderAsNormalBlock(){
        return false;
    }
	*/
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileHarvesterTrolley();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
    
    @Override
    public int quantityDropped(Random p_149745_1_) {

        return 0;
    }

    @Override
    public void breakBlock(World w, BlockPos pos, IBlockState state) {
        //Call TileEntity's onBlockBreaks function
        TileEntity tile = w.getTileEntity(pos);
        if (tile instanceof TileHarvesterTrolley) {
            ((TileHarvesterTrolley) tile).onBlockBreaks();
        }

        super.breakBlock(w, pos, state);
        w.removeTileEntity(pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        NBTTagCompound tag = stack.getTagCompound();
        TileHarvesterTrolley teTrolley = (TileHarvesterTrolley) world.getTileEntity(pos);
        teTrolley.setTrolley(Hydraulicraft.trolleyRegistrar.getTrolley(tag.getString("name")));
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }


    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {

        TileHarvesterTrolley teTrolley = (TileHarvesterTrolley) world.getTileEntity(pos);
        return Hydraulicraft.trolleyRegistrar.getTrolleyItem(teTrolley.getTrolley().getName());
    }


    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileHarvesterTrolley) {
            TileHarvesterTrolley ht = ((TileHarvesterTrolley) tileEntity);
            float extendedLength = ht.getExtendedLength();
            float sidewaysMovement = ht.getSideLength();

            //Get rotation:
            
            int dir = ht.getFacing().ordinal();
            float minX = 0.0F;
            float minY = 0.8F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;
            int dirXMin = (dir == 1 ? -1 : 0);
            int dirZMin = (dir == 2 ? -1 : 0);
            int dirXMax = (dir == 0 ? 1 : 0);
            int dirZMax = (dir == 3 ? 1 : 0);
            
            
            minX += sidewaysMovement * dirXMin;
            minY -= extendedLength;
            minZ += sidewaysMovement * dirZMin;
            
            maxX += sidewaysMovement * dirXMax;
            //maxY += extendedLength;
            maxZ += sidewaysMovement * dirZMax;
            
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }


}
