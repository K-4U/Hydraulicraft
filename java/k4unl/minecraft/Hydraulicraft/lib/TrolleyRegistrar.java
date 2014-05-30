package k4unl.minecraft.Hydraulicraft.lib;

import java.util.HashMap;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.api.ITrolleyRegistrar;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class TrolleyRegistrar implements ITrolleyRegistrar {

	private Map<String, IHarvesterTrolley> blocks;
	
	public TrolleyRegistrar(){
		blocks = new HashMap<String, IHarvesterTrolley>();
	}
	
	@Override
	public void registerTrolley(IHarvesterTrolley toRegister){
		blocks.put(toRegister.getName(), toRegister);
	}

	public IHarvesterTrolley getTrolley(String name) {
		return blocks.get(name);
	}
}
