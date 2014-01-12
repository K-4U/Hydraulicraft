package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidOil extends BlockFluidClassic {

	public BlockFluidOil() {
		super(Ids.blockFluidOil.act, Fluids.fluidOil, Material.water);
		this.setUnlocalizedName(Names.fluidOil.unlocalized);
	}
	
	
	@Override
	public void registerIcons(IconRegister icon){
		blockIcon = icon.registerIcon(ModInfo.LID + ":" + Names.fluidOil.unlocalized);
		this.getFluid().setIcons(blockIcon);
	}

	@Override
	public Icon getIcon(int side, int meta){
		return blockIcon;
	}
	
}
