package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IGUIMultiBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicHarvester extends HydraulicBlockContainerBase implements ITieredBlock, IGUIMultiBlock {

    public BlockHydraulicHarvester() {

        super(Names.blockHydraulicHarvester);

        hasFrontIcon = true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicHarvester();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.HARVESTER;
    }


    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }

    @Override
    public boolean isValid(IBlockAccess world, int x, int y, int z) {

        return ((TileHydraulicHarvester)world.getTileEntity(x, y, z)).getIsMultiblock();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
      EntityPlayer player, int par6, float par7, float par8, float par9) {

        if(world.isRemote) return true;
        if(!isValid(world, x, y, z)){
            TileHydraulicHarvester harvester = ((TileHydraulicHarvester)world.getTileEntity(x, y, z));
            //Tell the harvester to quickly check:
            harvester.doMultiBlockChecking();
            if(!isValid(world, x, y, z)) {
                player.addChatMessage(new ChatComponentTranslation(harvester.getError().getTranslation(), harvester.getExtraErrorInfo()));
                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }
}
