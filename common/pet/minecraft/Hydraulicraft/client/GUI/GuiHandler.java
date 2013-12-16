package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicCrusher;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import pet.minecraft.Hydraulicraft.client.containers.ContainerCrusher;
import pet.minecraft.Hydraulicraft.client.containers.ContainerIncinerator;
import pet.minecraft.Hydraulicraft.client.containers.ContainerMixer;
import pet.minecraft.Hydraulicraft.client.containers.ContainerPressureVat;
import pet.minecraft.Hydraulicraft.client.containers.ContainerPump;
import pet.minecraft.Hydraulicraft.client.containers.ContainerWasher;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
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
			}
		}
		
		return null;
	}

}
