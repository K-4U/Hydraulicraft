package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.containers.ContainerCrusher;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.containers.ContainerHarvester;
import k4unl.minecraft.Hydraulicraft.containers.ContainerIncinerator;
import k4unl.minecraft.Hydraulicraft.containers.ContainerMixer;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPressureVat;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPump;
import k4unl.minecraft.Hydraulicraft.containers.ContainerWasher;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.client.GUI.GuiPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.containers.ContainerPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI.GuiDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		if(ent != null){
			if(ID == Ids.GUIPump.act){
				if(ent instanceof TileHydraulicPump){
					return new ContainerPump(player.inventory, (TileHydraulicPump) ent);
				}
			}else if(ID == Ids.GUICrusher.act){
				if(ent instanceof TileHydraulicCrusher){
					return new ContainerCrusher(player.inventory, (TileHydraulicCrusher)ent);
				}
			}else if(ID == Ids.GUIWasher.act){
				if(ent instanceof TileHydraulicWasher){
					return new ContainerWasher(player.inventory, (TileHydraulicWasher)ent);
				}
			}else if(ID == Ids.GUIMixer.act){
				if(ent instanceof TileHydraulicMixer){
					return new ContainerMixer(player.inventory, (TileHydraulicMixer)ent);
				}
			}else if(ID == Ids.GUIPressureVat.act){
				if(ent instanceof TileHydraulicPressureVat){
					return new ContainerPressureVat(player.inventory, (TileHydraulicPressureVat)ent);
				}
			}else if(ID == Ids.GUIIncinerator.act){
				if(ent instanceof TileHydraulicFrictionIncinerator){
					return new ContainerIncinerator(player.inventory, (TileHydraulicFrictionIncinerator)ent);
				}
			}else if(ID == Ids.GUIPneumaticCompressor.act){
				if(ent instanceof TileHydraulicPneumaticCompressor){
					return new ContainerPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor)ent);
				}
			}else if(ID == Ids.GUIHarvester.act){
				if(ent instanceof TileHydraulicHarvester){
					return new ContainerHarvester(player.inventory, (TileHydraulicHarvester)ent);
				}
			}else if(ID == Ids.GUIDynamo.act){
				if(ent instanceof TileHydraulicDynamo){
					return new ContainerEmpty(player.inventory);
				}
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		TileEntity ent = world.getBlockTileEntity(x, y, z);
		
		if(ent != null){
			if(ID == Ids.GUIPump.act){
				if(ent instanceof TileHydraulicPump){
					return new GuiPump(player.inventory, (TileHydraulicPump) ent);
				}
			}else if(ID == Ids.GUICrusher.act){
				if(ent instanceof TileHydraulicCrusher){
					return new GuiCrusher(player.inventory, (TileHydraulicCrusher) ent);
				}
			}else if(ID == Ids.GUIWasher.act){
				if(ent instanceof TileHydraulicWasher){
					return new GuiWasher(player.inventory, (TileHydraulicWasher)ent);
				}
			}else if(ID == Ids.GUIMixer.act){
				if(ent instanceof TileHydraulicMixer){
					return new GuiMixer(player.inventory, (TileHydraulicMixer)ent);
				}
			}else if(ID == Ids.GUIPressureVat.act){
				if(ent instanceof TileHydraulicPressureVat){
					return new GuiPressureVat(player.inventory, (TileHydraulicPressureVat)ent);
				}
			}else if(ID == Ids.GUIIncinerator.act){
				if(ent instanceof TileHydraulicFrictionIncinerator){
					return new GuiIncinerator(player.inventory, (TileHydraulicFrictionIncinerator)ent);
				}
			}else if(ID == Ids.GUIPneumaticCompressor.act){
				if(ent instanceof TileHydraulicPneumaticCompressor){
					return new GuiPneumaticCompressor(player.inventory, (TileHydraulicPneumaticCompressor)ent);
				}
			}else if(ID == Ids.GUIHarvester.act){
				if(ent instanceof TileHydraulicHarvester){
					return new GuiHarvester(player.inventory, (TileHydraulicHarvester)ent);
				}
			}else if(ID == Ids.GUIDynamo.act){
				if(ent instanceof TileHydraulicDynamo){
					return new GuiDynamo(player.inventory, (TileHydraulicDynamo)ent);
				}
			}
		}
		
		return null;
	}

}
