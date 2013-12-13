package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;
import pet.minecraft.Hydraulicraft.proxy.ClientProxy;

public class BlockHydraulicPump extends MachineBlock {

    protected BlockHydraulicPump() {
        super(Ids.blockHydraulicPump, Names.blockHydraulicPump);
        this.hasTopIcon = true;
        this.hasBottomIcon = true;
        this.hasFrontIcon = true;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int getRenderType(){
        return ClientProxy.hydraulicPumpRenderType;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public int getRenderBlockPass(){
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass){
        //Set the static var in the client proxy
    	ClientProxy.renderPass = pass;
        //the block can render in both passes, so return true always
        return true;
    }
}
