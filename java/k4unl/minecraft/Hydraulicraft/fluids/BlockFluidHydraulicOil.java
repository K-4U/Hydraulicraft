package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidHydraulicOil extends BlockFluidClassic {
	@SideOnly(Side.CLIENT)
	protected IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon flowingIcon;

	public BlockFluidHydraulicOil() {
		super(Fluids.fluidHydraulicOil, Material.water);
		this.setBlockName(Names.fluidHydraulicOil.unlocalized);
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister icon) {
		stillIcon = icon.registerIcon(ModInfo.LID + ":"
				+ Names.fluidHydraulicOil.unlocalized + "_still");
		flowingIcon = icon.registerIcon(ModInfo.LID + ":"
				+ Names.fluidHydraulicOil.unlocalized + "_flowing");

		//TODO: Fix me!
		getFluid().setIcons(stillIcon, flowingIcon);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}
	@Override public Fluid getFluid(){ 
		return FluidRegistry.getFluid(fluidName); 
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z).getMaterial().isLiquid())
			return false;
		return super.displaceIfPossible(world, x, y, z);
	}

}
