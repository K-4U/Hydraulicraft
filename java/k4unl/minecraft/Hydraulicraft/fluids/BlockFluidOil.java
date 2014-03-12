package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidOil extends BlockFluidClassic {
    @SideOnly(Side.CLIENT)
    protected Icon stillIcon;
    @SideOnly(Side.CLIENT)
    protected Icon flowingIcon;
	
	
	public BlockFluidOil() {
		super(Ids.blockFluidOil.act, Fluids.fluidOil, Material.water);
		this.setUnlocalizedName(Names.fluidOil.unlocalized);
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister icon){
		stillIcon = icon.registerIcon(ModInfo.LID + ":" + Names.fluidOil.unlocalized + "_still");
		flowingIcon = icon.registerIcon(ModInfo.LID + ":" + Names.fluidOil.unlocalized + "_flowing");
		
		this.getFluid().setIcons(stillIcon, flowingIcon);
	}

	@Override
	public Icon getIcon(int side, int meta){
		return (side == 0 || side == 1)? stillIcon : flowingIcon;
	}
	
	 @Override
     public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
             if (world.getBlockMaterial(x,  y,  z).isLiquid()) return false;
             return super.canDisplace(world, x, y, z);
     }
    
     @Override
     public boolean displaceIfPossible(World world, int x, int y, int z) {
             if (world.getBlockMaterial(x,  y,  z).isLiquid()) return false;
             return super.displaceIfPossible(world, x, y, z);
     }
	
}
