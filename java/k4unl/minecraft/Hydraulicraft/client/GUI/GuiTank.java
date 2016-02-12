package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.containers.ContainerTank;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.k4lib.lib.Functions;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiTank extends HydraulicGUIBase {

    private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/tank.png");

    private TileInterfaceValve valve;


    public GuiTank(InventoryPlayer invPlayer, TileInterfaceValve tank) {

        super(new ContainerTank(invPlayer, tank), resLoc);
        valve = tank;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        drawRightAlignedString(7, 3, xSize - 14, Localization.getString(Localization.GUI_MULTIBLOCK_TANK), true);

        FluidTankInfo tankInfo = valve.getTankInfo(EnumFacing.UP)[0];
        if (tankInfo != null && tankInfo.fluid != null && tankInfo.fluid.getFluid() != null) {
            //Draw the fluid!
            int x = 11;
            int y = 12;
            int w = 69;
            int h = 56;
            TextureAtlasSprite fluidIcon = Functions.getFluidIcon(tankInfo.fluid.getFluid());
            String fluidName = tankInfo.fluid.getLocalizedName();

            drawVerticalProgressBarWithTexture(x, y, h, w, tankInfo.fluid.amount, tankInfo.capacity, fluidIcon, fluidName, "mB");
        }

        checkTooltips(mouseX, mouseY);
    }

}
