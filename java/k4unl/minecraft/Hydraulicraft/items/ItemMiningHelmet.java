package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.Functions;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;

public class ItemMiningHelmet extends ItemArmor {

    private final String         textureLocation;
    private       Location       prevPlayerLocation;
    private       List<Location> prevPlacedBlocks;

    public ItemMiningHelmet() {

        super(ArmorMaterial.IRON, 0, HEAD);

        setMaxStackSize(1);
        setUnlocalizedName(Names.itemMiningHelmet.unlocalized);

        setCreativeTab(CustomTabs.tabHydraulicraft);
        textureLocation = ModInfo.LID + ":textures/armor/hydraulicArmor";

        prevPlayerLocation = new Location(0, 0, 0);
        prevPlacedBlocks = new ArrayList<>();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return textureLocation + "_1.png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

        if (!world.isRemote) {
            return;
        }
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        if (itemStack.getTagCompound().getBoolean("powered")) {
            if (world.getTotalWorldTime() % 10 == 0) {
                RayTraceResult blockLookedAt = Functions.getEntityLookedObject(player, 12);
                Location blockLocation;
                if (blockLookedAt != null) {
                    EnumFacing dir = blockLookedAt.sideHit;
                    blockLocation = new Location(blockLookedAt.getBlockPos(), dir);
                } else {
                    blockLocation = new Location((int) Math.floor(player.posX), (int) Math.floor(player.posY) + 1, (int) Math.floor(player.posZ));
                }

                if (!prevPlayerLocation.equals(blockLocation)) {
                    Location playerLocation = new Location((int) Math.floor(player.posX), (int) Math.floor(player.posY) + 1, (int) Math.floor(player.posZ));
                    if (world.getBlockState(blockLocation.toBlockPos()).getBlock() == Blocks.AIR) {
                        world.setBlockState(blockLocation.toBlockPos(), HCBlocks.blockLight.getDefaultState().withProperty(Properties.LIGHTVALUE, Math.max(0, 15 - (playerLocation.getDifference(blockLocation) + 4))), 3);
                        world.scheduleBlockUpdate(blockLocation.toBlockPos(), HCBlocks.blockLight, 1, 1);
                        prevPlacedBlocks.add(blockLocation);
                    }
                    prevPlayerLocation = blockLocation;
                }
            }
            if (world.getTotalWorldTime() % 20 == 0) {
                cleanBlocks(world, prevPlacedBlocks);
            }
        } else {
            while (prevPlacedBlocks.size() > 0) {
                if (world.getBlockState(prevPlacedBlocks.get(0).toBlockPos()).getBlock() == HCBlocks.blockLight) {
                    world.setBlockToAir(prevPlacedBlocks.get(0).toBlockPos());
                }
                prevPlacedBlocks.remove(0);
            }
        }
    }

    private void cleanBlocks(World world, List<Location> prevPlacedBlocks) {

        while (prevPlacedBlocks.size() > 1) {
            if (world.getBlockState(prevPlacedBlocks.get(0).toBlockPos()).getBlock() == HCBlocks.blockLight) {
                world.setBlockToAir(prevPlacedBlocks.get(0).toBlockPos());
            }
            prevPlacedBlocks.remove(0);
        }
    }

    public static boolean isPoweredOn(ItemStack currentArmor) {

        if (currentArmor.getTagCompound() == null) {
            currentArmor.setTagCompound(new NBTTagCompound());
        }
        return currentArmor.getTagCompound().getBoolean("powered");
    }

    public static void togglePower(ItemStack currentArmor) {

        if (currentArmor.getTagCompound() == null) {
            currentArmor.setTagCompound(new NBTTagCompound());
        }
        currentArmor.getTagCompound().setBoolean("powered", !currentArmor.getTagCompound().getBoolean("powered"));
    }
}
