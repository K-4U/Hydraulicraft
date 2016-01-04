package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerFiller;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFiller;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFiller extends HydraulicGUIBase {
    private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/filler.png");
    TileHydraulicFiller filler;
    NEIWidgetTank       tankFluid;

    public GuiFiller(InventoryPlayer inventoryPlayer, TileHydraulicFiller filler) {
        super(filler, new ContainerFiller(inventoryPlayer, filler), resLoc);

        this.filler = filler;
        tankFluid = new NEIWidgetTank(filler.getTankInfo(EnumFacing.UP)[0], 122, 70, 16, 54, this);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        drawHorizontalAlignedString(7, 3, xSize - 14, HCBlocks.hydraulicFiller.getLocalizedName(), true);

        FluidTankInfo[] tankInfo = filler.getTankInfo(EnumFacing.UP);
        tankFluid.render(tankInfo[0]);

        drawFluidAndPressure();
        checkTooltips(mouseX, mouseY);
    }
}
