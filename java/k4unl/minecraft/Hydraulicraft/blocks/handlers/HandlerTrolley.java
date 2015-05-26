package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import java.util.List;
import java.util.Set;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.TrolleyEnderlily;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCrops;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleySugarCane;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HandlerTrolley extends HandlerHydraulicBlock{

    public HandlerTrolley(Block block){
        super(block);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList){
        Set<String> trolleys = Hydraulicraft.harvesterTrolleyRegistrar.getRegisteredTrolleys();
        for(String trolley : trolleys){
            itemList.add(Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem(trolley));
        }
    }
    
    public String getUnlocalizedName(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null){
            return super.getUnlocalizedName(stack) + "." + tag.getString("name");
        }else{
            Log.error("Tag of a trolley itemstack was null??");
            return super.getUnlocalizedName(stack);
        }
    }
}
