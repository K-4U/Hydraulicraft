package k4unl.minecraft.Hydraulicraft.blocks.gow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.lib.TeleportHelper;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.network.PacketPipeline;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSpawnParticle;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockPortalTeleporter extends GOWBlockRendering {
    private static Vector3fMax blockBounds = new Vector3fMax(0.499F, 0.499F, 0.499F, 0.501F, 0.501F, 0.501F);


    public BlockPortalTeleporter() {

        super(Names.portalTeleporter.unlocalized);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInPass(int pass) {

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass() {

        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {

        TileEntity ent = iba.getTileEntity(x, y, z);

        if (ent instanceof TilePortalTeleporter) {
            TilePortalTeleporter teleporter = (TilePortalTeleporter) ent;
            Vector3fMax vector = blockBounds.copy();
            if (teleporter.getBaseDir() != null) {
                if (teleporter.getBaseDir().equals(ForgeDirection.NORTH) | teleporter.getPortalDir().equals(ForgeDirection.NORTH)) {
                    vector.setZMin(0.0F);
                    vector.setZMax(1.0F);
                }
                if (teleporter.getPortalDir().equals(ForgeDirection.UP)) {
                    vector.setYMin(0.0F);
					vector.setYMax(1.0F);
				}
				if(teleporter.getBaseDir().equals(ForgeDirection.EAST) || teleporter.getPortalDir().equals(ForgeDirection.EAST)){
					vector.setXMin(0.0F);
					vector.setXMax(1.0F);
				}
			}
			
			this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB axigAlignedBB, List arrayList, Entity entity){
		
	}
	
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity){
		if(!world.isRemote){
			NBTTagCompound entCompound = entity.getEntityData();
			Location teLocation = new Location(x,y,z);
			TilePortalTeleporter teleporter = (TilePortalTeleporter)teLocation.getTE(world);
            if(teleporter == null){
                return;
            }
            if(teleporter.getPortalBase() == null){
                return;
            }
			Location teleportLocation = teleporter.getPortalBase().getTarget();
			long lastInPortal = entCompound.getLong("lastInPortal" + teleporter.getPortalBase().getIPLong());
			if(world.getTotalWorldTime() - lastInPortal > (HCConfig.INSTANCE.getInt("portalTimeoutInSeconds") * 20)){
				if(teleportLocation != null){
					teleporter.usePressure();
					TeleportHelper.teleportEntity(entity, teleportLocation);
					Random rnd = new Random(System.currentTimeMillis()/1000);
					double dx = 0.0D;
		            double dy = 0.0D;
		            double dz = 0.0D;
		            for(int i = 0; i <= 5; i++){
			            dx = (rnd.nextFloat() - 0.6D) * 0.1D;
			            dy = (rnd.nextFloat() - 0.6D) * 0.1D;
			            dz = (rnd.nextFloat() - 0.6D) * 0.1D;
			            
			            //world.spawnParticle("cloud", x, y, z, d3, d4, d5);
			            PacketPipeline.instance.sendToAllAround(new PacketSpawnParticle("cloud", x+.5, y+.5, z+.5, dx, dy, dz), world);
		            }
					/*if(teleportLocation.getDimension() != world.provider.dimensionId){
						
					}else{
						if(entity instanceof EntityPlayer){
							((EntityPlayer)entity).setPositionAndUpdate(teleportLocation.getX()+0.5, teleportLocation.getY()+0.5, teleportLocation.getZ()+0.5);	
						}else{
							entity.setLocationAndAngles(teleportLocation.getX()+0.5, teleportLocation.getY()+0.5, teleportLocation.getZ()+0.5, entity.rotationYaw, entity.rotationPitch);
						}
					}*/
					
					entCompound.setLong("lastInPortal" + teleporter.getPortalBase().getIPLong(), world.getTotalWorldTime());
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World w, int x, int y, int z, Random rnd){
		/*for (int l = 0; l < 1; ++l)
        {*/
		if(rnd.nextInt(100) <= 50){
            double d0 = x + rnd.nextFloat();
            double d1 = y + rnd.nextFloat();
            double d2 = z + rnd.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            d3 = (rnd.nextFloat() - 0.6D) * 0.1D;
            d4 = (rnd.nextFloat() - 0.6D) * 0.1D;
            d5 = (rnd.nextFloat() - 0.6D) * 0.1D;

            w.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
		}
        //}
	}
	
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalTeleporter();
    }
}
