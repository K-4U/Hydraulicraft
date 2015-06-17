package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.api.ITrolleyRegistrar;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Koen Beckers (K-4U)
 */
public class TrolleyRegistrar implements ITrolleyRegistrar {
    private Map<String, IHarvesterTrolley> blocks;

    public TrolleyRegistrar() {

        blocks = new HashMap<String, IHarvesterTrolley>();
    }

    @Override
    public void registerTrolley(IHarvesterTrolley toRegister) {

        blocks.put(toRegister.getName(), toRegister);
    }

    public IHarvesterTrolley getTrolley(String name) {

        return blocks.get(name);
    }

    @Override
    public ItemStack getTrolleyItem(String trolleyName) {

        if (!blocks.containsKey(trolleyName)) throw new IllegalArgumentException("Trolley with the name " + trolleyName + " isn't registered!");

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
