package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;


public class BlockBaseFluid extends BlockFluidClassic {
/*
    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;
*/
    private Name mName;

    public BlockBaseFluid(Fluid fluid, Name name) {

        super(fluid, Material.water);

        setUnlocalizedName(name.unlocalized);
        mName = name;
        setCreativeTab(CustomTabs.tabHydraulicraft);
    }
/*
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister icon) {
        stillIcon = icon.registerIcon(ModInfo.LID + ":"
                + mName.unlocalized + "_still");
        flowingIcon = icon.registerIcon(ModInfo.LID + ":"
                + mName.unlocalized + "_flowing");

        getFluid().setIcons(stillIcon, flowingIcon);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? stillIcon : flowingIcon;
    }*/

    @Override public Fluid getFluid(){
        return FluidRegistry.getFluid(fluidName);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock().getMaterial().isLiquid())
            return false;
        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock().getMaterial().isLiquid())
            return false;
        return super.displaceIfPossible(world, pos);
    }
}
