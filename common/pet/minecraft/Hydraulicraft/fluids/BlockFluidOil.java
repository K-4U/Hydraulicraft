package pet.minecraft.Hydraulicraft.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.BlockFluidClassic;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockFluidOil extends BlockFluidClassic {

	public BlockFluidOil() {
		super(Ids.blockFluidOil.act, Fluids.fluidOil, Material.water);
	}
	
	
	@Override
	public void registerIcons(IconRegister icon){
		blockIcon = icon.registerIcon(ModInfo.LID + ":" + Names.fluidOil.unlocalized);
	}

	@Override
	public Icon getIcon(int side, int meta){
		return blockIcon;
	}
	
}
