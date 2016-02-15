package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;


public class BlockBaseFluid extends BlockFluidClassic {

    private Name mName;

    public BlockBaseFluid(Fluid fluid, Name name) {

        super(fluid, Material.water);

        setUnlocalizedName(name.unlocalized);
        mName = name;
        setCreativeTab(CustomTabs.tabHydraulicraft);
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
