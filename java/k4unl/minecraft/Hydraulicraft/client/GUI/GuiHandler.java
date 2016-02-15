package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.containers.*;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.client.GUI.GuiSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.containers.ContainerSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities.TileHydraulicSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.GUI.GuiElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.GUI.GuiHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.client.GUI.GuiPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.containers.ContainerPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.client.GUI.GuiHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.client.GUI.GuiRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileRFPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.*;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureReservoir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {

        TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));
        if (ent != null) {
            switch (GuiIDs.values()[ID]) {
                case COMPRESSOR:
                    if (ent instanceof TileHydraulicPneumaticCompressor) {
                        return new ContainerPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor) ent);
                    }
                    break;
                case CRUSHER:
                    if (ent instanceof TileHydraulicCrusher) {
                        return new ContainerCrusher(player.inventory, (TileHydraulicCrusher) ent);
                    }
                    break;
                case DYNAMO:
                    if (ent instanceof TileHydraulicDynamo) {
                        return new ContainerEmpty(player.inventory);
                    }
                    break;
                case ELECTRICPUMP:
                    if (ent instanceof TileElectricPump) {
                        return new ContainerEmpty(player.inventory);
                    }
                    break;
                case GENERATOR:
                    if (ent instanceof TileHydraulicGenerator) {
                        return new ContainerEmpty(player.inventory);
                    }
                    break;
                case HARVESTER:
                    if (ent instanceof TileHydraulicHarvester) {
                        return new ContainerHarvester(player.inventory, (TileHydraulicHarvester) ent);
                    }
                    break;
                case INCINERATOR:
                    if (ent instanceof TileHydraulicFrictionIncinerator) {
                        return new ContainerIncinerator(player.inventory, (TileHydraulicFrictionIncinerator) ent);
                    }
                    break;
                case INFINITESOURCE:
                    if (ent instanceof TileInfiniteSource) {
                        return new ContainerInfiniteSource(player.inventory, (TileInfiniteSource) ent);
                    }
                    break;
                case INVALID:
                    break;
                case LAVAPUMP:
                    if (ent instanceof TileHydraulicLavaPump) {
                        return new ContainerEmpty(player.inventory);
                    }
                    break;
                case FILTER:
                    if (ent instanceof TileHydraulicFilter) {
                        return new ContainerFilter(player.inventory, (TileHydraulicFilter) ent);
                    }
                    break;
                case PRESSUREVAT:
                    if (ent instanceof TileHydraulicPressureReservoir) {
                        return new ContainerPressureVat(player.inventory, (TileHydraulicPressureReservoir) ent);
                    }
                    break;
                case PUMP:
                    if (ent instanceof TileHydraulicPump) {
                        return new ContainerPump(player.inventory, (TileHydraulicPump) ent);
                    }
                    break;
                case RFPUMP:
                    if (ent instanceof TileRFPump) {
                        return new ContainerEmpty(player.inventory);
                    }
                    break;
                case SAW:
                    if (ent instanceof TileHydraulicSaw) {
                        return new ContainerSaw(player.inventory, (TileHydraulicSaw) ent);
                    }
                    break;
                case WASHER:
                    if (ent instanceof TileHydraulicWasher) {
                        return new ContainerWasher(player.inventory, (TileHydraulicWasher) ent);
                    }
                    break;
                case PORTALBASE:
                    if (ent instanceof TilePortalBase) {
                        return new ContainerPortalBase(player.inventory, (TilePortalBase) ent);
                    }
                    break;
                case ASSEMBLER:
                    if (ent instanceof TileAssembler) {
                        return new ContainerAssembler(player.inventory, (TileAssembler) ent);
                    }
                    break;
                case FILLER:
                    if (ent instanceof TileHydraulicFiller) {
                        return new ContainerFiller(player.inventory, (TileHydraulicFiller) ent);
                    }
                    break;
                case CHARGER:
                    if (ent instanceof TileHydraulicCharger) {
                        return new ContainerCharger(player.inventory, (TileHydraulicCharger) ent);
                    }
                case TANK:
                    if (ent instanceof TileInterfaceValve) {
                        return new ContainerTank(player.inventory, (TileInterfaceValve) ent);
                    }
                default:
                    break;

            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {

        TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));

        if (ent != null) {
            switch (GuiIDs.values()[ID]) {
                case COMPRESSOR:
                    if (ent instanceof TileHydraulicPneumaticCompressor) {
                        return new GuiPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor) ent);
                    }
                    break;
                case CRUSHER:
                    if (ent instanceof TileHydraulicCrusher) {
                        return new GuiCrusher(player.inventory, (TileHydraulicCrusher) ent);
                    }
                    break;
                case DYNAMO:
                    if (ent instanceof TileHydraulicDynamo) {
                        return new GuiHydraulicDynamo(player.inventory, ent);
                    }
                    break;
                case ELECTRICPUMP:
                    if (ent instanceof TileElectricPump) {
                        return new GuiElectricPump(player.inventory, ent);
                    }
                    break;
                case GENERATOR:
                    if (ent instanceof TileHydraulicGenerator) {
                        return new GuiHydraulicGenerator(player.inventory, ent);
                    }
                    break;
                case HARVESTER:
                    if (ent instanceof TileHydraulicHarvester) {
                        return new GuiHarvester(player.inventory, (TileHydraulicHarvester) ent);
                    }
                    break;
                case INCINERATOR:
                    if (ent instanceof TileHydraulicFrictionIncinerator) {
                        return new GuiIncinerator(player.inventory, (TileHydraulicFrictionIncinerator) ent);
                    }
                    break;
                case INFINITESOURCE:
                    if (ent instanceof TileInfiniteSource) {
                        return new GuiInfiniteSource(player.inventory, (TileInfiniteSource) ent);
                    }
                    break;
                case INVALID:
                    break;
                case LAVAPUMP:
                    if (ent instanceof TileHydraulicLavaPump) {
                        return new GuiLavaPump(player.inventory, (TileHydraulicLavaPump) ent);
                    }
                    break;
                case FILTER:
                    if (ent instanceof TileHydraulicFilter) {
                        return new GuiFilter(player.inventory, (TileHydraulicFilter) ent);
                    }
                    break;
                case PRESSUREVAT:
                    if (ent instanceof TileHydraulicPressureReservoir) {
                        return new GuiPressureVat(player.inventory, (TileHydraulicPressureReservoir) ent);
                    }
                    break;
                case PUMP:
                    if (ent instanceof TileHydraulicPump) {
                        return new GuiPump(player.inventory, (TileHydraulicPump) ent);
                    }
                    break;
                case RFPUMP:
                    if (ent instanceof TileRFPump) {
                        return new GuiRFPump(player.inventory, ent);
                    }
                    break;
                case SAW:
                    if (ent instanceof TileHydraulicSaw) {
                        return new GuiSaw(player.inventory, (TileHydraulicSaw) ent);
                    }
                    break;
                case WASHER:
                    if (ent instanceof TileHydraulicWasher) {
                        return new GuiWasher(player.inventory, (TileHydraulicWasher) ent);
                    }
                    break;
                case PORTALBASE:
                    if (ent instanceof TilePortalBase) {
                        return new GuiPortalBase(player.inventory, (TilePortalBase) ent);
                    }
                    break;
                case ASSEMBLER:
                    if (ent instanceof TileAssembler) {
                        return new GuiAssembler(player.inventory, (TileAssembler) ent);
                    }
                case FILLER:
                    if (ent instanceof TileHydraulicFiller)
                        return new GuiFiller(player.inventory, (TileHydraulicFiller) ent);
                    break;
                case CHARGER:
                    if (ent instanceof TileHydraulicCharger) {
                        return new GuiCharger(player.inventory, (TileHydraulicCharger) ent);
                    }
                case TANK:
                    if (ent instanceof TileInterfaceValve) {
                        return new GuiTank(player.inventory, (TileInterfaceValve) ent);
                    }
                default:
                    break;

            }
        }

        return null;
    }

}
