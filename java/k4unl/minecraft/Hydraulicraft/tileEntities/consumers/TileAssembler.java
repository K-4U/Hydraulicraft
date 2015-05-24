package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;


import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockAssembler;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAssembler extends TileHydraulicBase implements IHydraulicConsumer, IInventory{


    public TileAssembler() {

        super(10);
        super.init(this);
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {

        return 0;
    }

    @Override
    public boolean canWork(ForgeDirection dir) {

        return false;
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {

        return true;
    }

    public int getScaledAssembleTime() {

        return 0;
    }

    @Override
    public int getSizeInventory() {

        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {

        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {

        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {

    }

    @Override
    public String getInventoryName() {

        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {

        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return false;
    }
}
