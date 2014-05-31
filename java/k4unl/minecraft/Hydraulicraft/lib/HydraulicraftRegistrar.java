package k4unl.minecraft.Hydraulicraft.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicraftRegistrar;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HydraulicraftRegistrar implements IHydraulicraftRegistrar {

	private Map<String, IHarvesterTrolley> blocks;
	
	public HydraulicraftRegistrar(){
		blocks = new HashMap<String, IHarvesterTrolley>();
	}
	
	@Override
	public void registerTrolley(IHarvesterTrolley toRegister){
		blocks.put(toRegister.getName(), toRegister);
	}

	public IHarvesterTrolley getTrolley(String name) {
		return blocks.get(name);
	}

    @Override
    public ItemStack getTrolleyItem(String trolleyName){
        if(!blocks.containsKey(trolleyName)) throw new IllegalArgumentException("Trolley with the name " + trolleyName + " isn't registered!");
        
        ItemStack trolley = new ItemStack(HCBlocks.harvesterTrolley, 1, 0);
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", trolleyName);
        trolley.setTagCompound(tag);
        
        return trolley;
    }
    
    public Set<String> getRegisteredTrolleys(){
        return blocks.keySet();
    }
}
