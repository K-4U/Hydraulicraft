package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.containers.ContainerCrusher;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.containers.ContainerHarvester;
import k4unl.minecraft.Hydraulicraft.containers.ContainerIncinerator;
import k4unl.minecraft.Hydraulicraft.containers.ContainerInfiniteSource;
import k4unl.minecraft.Hydraulicraft.containers.ContainerMixer;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPressureVat;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPump;
import k4unl.minecraft.Hydraulicraft.containers.ContainerWasher;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.GUI.GuiHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.GUI.GuiKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
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
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI.GuiHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI.GuiRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		TileEntity ent = world.getTileEntity(x, y, z);
		if(ent != null){
			if(ID == GuiIDs.GUIPump){
				if(ent instanceof TileHydraulicPump){
					return new ContainerPump(player.inventory, (TileHydraulicPump) ent);
				}
			}else if(ID == GuiIDs.GUICrusher){
				if(ent instanceof TileHydraulicCrusher){
					return new ContainerCrusher(player.inventory, (TileHydraulicCrusher)ent);
				}
			}else if(ID == GuiIDs.GUIWasher){
				if(ent instanceof TileHydraulicWasher){
					return new ContainerWasher(player.inventory, (TileHydraulicWasher)ent);
				}
			}else if(ID == GuiIDs.GUIMixer){
				if(ent instanceof TileHydraulicMixer){
					return new ContainerMixer(player.inventory, (TileHydraulicMixer)ent);
				}
			}else if(ID == GuiIDs.GUIPressureVat){
				if(ent instanceof TileHydraulicPressureVat){
					return new ContainerPressureVat(player.inventory, (TileHydraulicPressureVat)ent);
				}
			}else if(ID == GuiIDs.GUIIncinerator){
				if(ent instanceof TileHydraulicFrictionIncinerator){
					return new ContainerIncinerator(player.inventory, (TileHydraulicFrictionIncinerator)ent);
				}
			}else if(ID == GuiIDs.GUIPneumaticCompressor){
				if(ent instanceof TileHydraulicPneumaticCompressor){
					return new ContainerPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor)ent);
				}
			}else if(ID == GuiIDs.GUIHarvester){
				if(ent instanceof TileHydraulicHarvester){
					return new ContainerHarvester(player.inventory, (TileHydraulicHarvester)ent);
				}
			}else if(ID == GuiIDs.GUIHydraulicDynamo){
				if(ent instanceof TileHydraulicDynamo){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIRFPump){
				if(ent instanceof TileRFPump){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIHydraulicGenerator){
				if(ent instanceof TileHydraulicGenerator){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIElecticPump){
				if(ent instanceof TileElectricPump){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIHydraulicEngine){
				if(ent instanceof TileHydraulicEngine){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIKineticPump){
				if(ent instanceof TileKineticPump){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUISaw){
				if(ent instanceof TileHydraulicSaw){
					return new ContainerSaw(player.inventory, (TileHydraulicSaw)ent);
				}
			}else if(ID == GuiIDs.GUILavaPump){
				if(ent instanceof TileHydraulicLavaPump){
					return new ContainerEmpty(player.inventory);
				}
			}else if(ID == GuiIDs.GUIInfiniteSource){
				if(ent instanceof TileInfiniteSource){
					return new ContainerInfiniteSource(player.inventory, (TileInfiniteSource)ent);
				}
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		TileEntity ent = world.getTileEntity(x, y, z);
		
		if(ent != null){
			if(ID == GuiIDs.GUIPump){
				if(ent instanceof TileHydraulicPump){
					return new GuiPump(player.inventory, (TileHydraulicPump) ent);
				}
			}else if(ID == GuiIDs.GUICrusher){
				if(ent instanceof TileHydraulicCrusher){
					return new GuiCrusher(player.inventory, (TileHydraulicCrusher) ent);
				}
			}else if(ID == GuiIDs.GUIWasher){
				if(ent instanceof TileHydraulicWasher){
					return new GuiWasher(player.inventory, (TileHydraulicWasher)ent);
				}
			}else if(ID == GuiIDs.GUIMixer){
				if(ent instanceof TileHydraulicMixer){
					return new GuiMixer(player.inventory, (TileHydraulicMixer)ent);
				}
			}else if(ID == GuiIDs.GUIPressureVat){
				if(ent instanceof TileHydraulicPressureVat){
					return new GuiPressureVat(player.inventory, (TileHydraulicPressureVat)ent);
				}
			}else if(ID == GuiIDs.GUIIncinerator){
				if(ent instanceof TileHydraulicFrictionIncinerator){
					return new GuiIncinerator(player.inventory, (TileHydraulicFrictionIncinerator)ent);
				}
			}else if(ID == GuiIDs.GUIPneumaticCompressor){
				if(ent instanceof TileHydraulicPneumaticCompressor){
					return new GuiPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor)ent);
				}
			}else if(ID == GuiIDs.GUIHarvester){
				if(ent instanceof TileHydraulicHarvester){
					return new GuiHarvester(player.inventory, (TileHydraulicHarvester)ent);
				}
			}else if(ID == GuiIDs.GUIHydraulicDynamo){
				if(ent instanceof TileHydraulicDynamo){
					return new GuiHydraulicDynamo(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUIRFPump){
				if(ent instanceof TileRFPump){
					return new GuiRFPump(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUIHydraulicGenerator){
				if(ent instanceof TileHydraulicGenerator){
					return new GuiHydraulicGenerator(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUIElecticPump){
				if(ent instanceof TileElectricPump){
					return new GuiElectricPump(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUIHydraulicEngine){
				if(ent instanceof TileHydraulicEngine){
					return new GuiHydraulicEngine(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUIKineticPump){
				if(ent instanceof TileKineticPump){
					return new GuiKineticPump(player.inventory, ent);
				}
			}else if(ID == GuiIDs.GUISaw){
				if(ent instanceof TileHydraulicSaw){
					return new GuiSaw(player.inventory, (TileHydraulicSaw)ent);
				}
			}else if(ID == GuiIDs.GUILavaPump){
				if(ent instanceof TileHydraulicLavaPump){
					return new GuiLavaPump(player.inventory, (TileHydraulicLavaPump) ent);
				}
			}else if(ID == GuiIDs.GUIInfiniteSource){
				if(ent instanceof TileInfiniteSource){
					return new GuiInfiniteSource(player.inventory, (TileInfiniteSource)ent);
				}
			}
		}
		
		return null;
	}

}
