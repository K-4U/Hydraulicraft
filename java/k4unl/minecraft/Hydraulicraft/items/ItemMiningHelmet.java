package k4unl.minecraft.Hydraulicraft.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemMiningHelmet extends ItemArmor {
	private final String textureLocation;
	private Location prevPlayerLocation;
	private List<Location> prevPlacedBlocks;
	
	public ItemMiningHelmet() {
		super(ArmorMaterial.IRON, 0, 0);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemMiningHelmet.unlocalized);
		setTextureName(ModInfo.LID + ":" + Names.itemMiningHelmet.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
		textureLocation = ModInfo.LID + ":textures/armor/hydraulicArmor";
		prevPlayerLocation = new Location(0, 0, 0);
		prevPlacedBlocks = new ArrayList<Location>();
	}
	
    @Override
    public void registerIcons(IIconRegister register){
        itemIcon = register.registerIcon(getIconString());
    }
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
        return textureLocation + "_1.png";
    }
	
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack){
		if(!world.isRemote){
			return;
		}
		if(itemStack.getTagCompound() == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		if(itemStack.getTagCompound().getBoolean("powered")){
			if(world.getTotalWorldTime() % 10 == 0){
				MovingObjectPosition blockLookedAt = Functions.getEntityLookedObject(player, 12);
				Location blockLocation = null;
				Location playerLocation = new Location((int)Math.floor(player.posX), (int)Math.floor(player.posY)+1, (int)Math.floor(player.posZ));
				if(blockLookedAt != null){
					ForgeDirection dir = ForgeDirection.getOrientation(blockLookedAt.sideHit);
					int x = blockLookedAt.blockX + dir.offsetX;
					int y = blockLookedAt.blockY + dir.offsetY;
					int z = blockLookedAt.blockZ + dir.offsetZ;
					blockLocation = new Location(x, y, z);
				}else{
					blockLocation = new Location((int)Math.floor(player.posX), (int)Math.floor(player.posY)+1, (int)Math.floor(player.posZ));
				}
				
				
				if(!prevPlayerLocation.equals(blockLocation)){
					if(world.getBlock(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()).equals(Blocks.air)){
						world.setBlock(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), HCBlocks.blockLight, playerLocation.getDifference(blockLocation) + 3, 3);
						world.scheduleBlockUpdate(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), HCBlocks.blockLight, 1);
						world.markBlockForUpdate(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
						prevPlacedBlocks.add(blockLocation);
					}
					prevPlayerLocation = blockLocation;
				}
			}
			if(world.getTotalWorldTime() % 20 == 0){
				cleanBlocks(world);
			}
		}else{
			while(prevPlacedBlocks.size() > 0){
				if(world.getBlock(prevPlacedBlocks.get(0).getX(), prevPlacedBlocks.get(0).getY(), prevPlacedBlocks.get(0).getZ()).equals(HCBlocks.blockLight)){
					world.setBlockToAir(prevPlacedBlocks.get(0).getX(), prevPlacedBlocks.get(0).getY(), prevPlacedBlocks.get(0).getZ());
				}
				prevPlacedBlocks.remove(0);
			}
		}
    }
	
	private void cleanBlocks(World world){
		while(prevPlacedBlocks.size() > 1){
			if(world.getBlock(prevPlacedBlocks.get(0).getX(), prevPlacedBlocks.get(0).getY(), prevPlacedBlocks.get(0).getZ()).equals(HCBlocks.blockLight)){
				world.setBlockToAir(prevPlacedBlocks.get(0).getX(), prevPlacedBlocks.get(0).getY(), prevPlacedBlocks.get(0).getZ());
			}
			prevPlacedBlocks.remove(0);
		}
	}

	public static boolean isPoweredOn(ItemStack currentArmor) {
		if(currentArmor.getTagCompound() == null){
			currentArmor.setTagCompound(new NBTTagCompound());
		}
		return currentArmor.getTagCompound().getBoolean("powered");
	}

	public static void togglePower(ItemStack currentArmor) {
		if(currentArmor.getTagCompound() == null){
			currentArmor.setTagCompound(new NBTTagCompound());
		}
		currentArmor.getTagCompound().setBoolean("powered", !currentArmor.getTagCompound().getBoolean("powered"));
	}
}
