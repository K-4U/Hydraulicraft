package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.baseClasses.interfaces.IMachineGenerator;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPump extends MachineBlock implements IMachineGenerator {

    protected BlockHydraulicPump() {
        super(Ids.blockHydraulicPump, Names.blockHydraulicPump);
        this.hasFrontIcon = true;
    }

	@Override
	public void workFunction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxGenerating() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBar() {
		// TODO Auto-generated method stub
		return 0;
	}
}
