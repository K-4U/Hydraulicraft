package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartFluidPipe;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipart;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class PartFluidPipe extends Multipart implements ISlottedPart, ITickable, IOccludingPart {

   public static  AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[7];
   private static float           pixel         = 1.0F / 16F;
   private static int             expandBounds  = -1;
   @SideOnly(Side.CLIENT)
   private static RendererPartFluidPipe renderer;

   static {
      float center = 0.5F;
      double w = pixel * 3;
      boundingBoxes[6] = new AxisAlignedBB(center - w, center - w, center - w, center + w, center + w, center + w);

      int i = 0;
      for (EnumFacing dir : EnumFacing.VALUES) {
         double xMin1 = (dir.getFrontOffsetX() < 0 ? 0.0 : (dir.getFrontOffsetX() == 0 ? center - w : center + w));
         double xMax1 = (dir.getFrontOffsetX() > 0 ? 1.0 : (dir.getFrontOffsetX() == 0 ? center + w : center - w));

         double yMin1 = (dir.getFrontOffsetY() < 0 ? 0.0 : (dir.getFrontOffsetY() == 0 ? center - w : center + w));
         double yMax1 = (dir.getFrontOffsetY() > 0 ? 1.0 : (dir.getFrontOffsetY() == 0 ? center + w : center - w));

         double zMin1 = (dir.getFrontOffsetZ() < 0 ? 0.0 : (dir.getFrontOffsetZ() == 0 ? center - w : center + w));
         double zMax1 = (dir.getFrontOffsetZ() > 0 ? 1.0 : (dir.getFrontOffsetZ() == 0 ? center + w : center - w));

         boundingBoxes[i] = new AxisAlignedBB(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
         i++;
      }
   }

   private byte connectionCache = 0;
   private boolean neighborBlockChanged;
   private Fluid   fluidStored;
   private double fluidAmountStored;

   @Override
   public void readFromNBT(NBTTagCompound tagCompound) {
      super.readFromNBT(tagCompound);
      fluidAmountStored = tagCompound.getDouble("fluidAmountStored");
      fluidStored = FluidRegistry.getFluid(tagCompound.getString("fluidName"));
      connectionCache = tagCompound.getByte("connectionCache");
   }

   @Override
   public void writeToNBT(NBTTagCompound tagCompound) {
      super.writeToNBT(tagCompound);
      tagCompound.setDouble("fluidAmountStored", fluidAmountStored);
      if (fluidStored != null) {
         tagCompound.setString("fluidName", fluidStored.getName());
      }
      tagCompound.setByte("connectionCache", connectionCache);
   }

   @Override
   public void writeUpdatePacket(PacketBuffer buf) {
      super.writeUpdatePacket(buf);

      NBTTagCompound mainCompound = new NBTTagCompound();
      if (getWorld() != null && !getWorld().isRemote) {
         mainCompound.setDouble("fluidAmountStored", fluidAmountStored);
         if (fluidStored != null) {
            mainCompound.setString("fluidName", fluidStored.getName());
         }
         mainCompound.setByte("connectionCache", connectionCache);
      }

      buf.writeNBTTagCompoundToBuffer(mainCompound);
   }

   @Override
   public void readUpdatePacket(PacketBuffer buf) {
      super.readUpdatePacket(buf);
      NBTTagCompound mainCompound = null;
      try {
         mainCompound = buf.readNBTTagCompoundFromBuffer();
         fluidAmountStored = mainCompound.getDouble("fluidAmountStored");
         fluidStored = FluidRegistry.getFluid(mainCompound.getString("fluidName"));
         connectionCache = mainCompound.getByte("connectionCache");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public EnumSet<PartSlot> getSlotMask() {
      return EnumSet.of(PartSlot.CENTER);
   }

   @Override
   public boolean occlusionTest(IMultipart part) {
      return OcclusionHelper.defaultOcclusionTest(this, part);
   }

   @Override
   public void update() {
      if (getWorld() != null) {
         if (!getWorld().isRemote) {
            redistributeFluid();

            if (neighborBlockChanged) {
               updateNeighborInfo(true);
               neighborBlockChanged = false;
            }
         }
      }
   }

   @Override
   public void onRemoved() {
      super.onRemoved();
   }

   @Override
   public float getStrength(EntityPlayer player, PartMOP hit) {
      return 8F;
   }

   private void redistributeFluid() {
        /*if (connectedSides == null) return;
        double avgFluid = getFluidAmountStored();
        int neighbourCount = 1;
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity neighbor = connectedSides.get(dir);
            if (neighbor != null && neighbor instanceof TileMultipart) {
                if (Multipart.hasPartFluidPipe((TileMultipart) neighbor)) {
                    avgFluid += Multipart.getFluidPipe((TileMultipart) neighbor).getFluidAmountStored();
                    neighbourCount++;
                }
            }
        }
        avgFluid = avgFluid / neighbourCount;

        addFluid(avgFluid - getFluidAmountStored());
        for (EnumFacing dir : EnumFacing.values()) {
            TileEntity neighbor = connectedSides.get(dir);

            if (neighbor != null && neighbor instanceof TileMultipart) {

                if (Multipart.hasPartFluidPipe((TileMultipart) neighbor)) {

                    if (Multipart.getFluidPipe((TileMultipart) neighbor).getFluidStored() == null) {
                        Multipart.getFluidPipe((TileMultipart) neighbor).setFluidStored(getFluidStored());
                    } else if (getFluidStored() == null) {
                        setFluidStored(Multipart.getFluidPipe((TileMultipart) neighbor).getFluidStored());
                    }

                    Multipart.getFluidPipe((TileMultipart) neighbor).addFluid(avgFluid - Multipart.getFluidPipe((TileMultipart) neighbor).getFluidAmountStored());
                }
            } else if (neighbor != null && neighbor instanceof IFluidHandler) {
                if (getFluidStored() != null) {
                    int toFill = ((IFluidHandler) neighbor).fill(dir.getOpposite(), new FluidStack(getFluidStored(), (int) getFluidAmountStored()), false);
                    ((IFluidHandler) neighbor).fill(dir.getOpposite(), new FluidStack(getFluidStored(), toFill), true);
                    addFluid(-toFill);
                }
            }
        }*/
   }

   public double getFluidAmountStored() {
      return fluidAmountStored;
   }

   public void setFluidAmountStored(double fluidStored) {
      this.fluidAmountStored = fluidStored;
      if (fluidAmountStored == 0) {
         setFluidStored(null);
      }
   }

   public void addFluid(double fluidStored) {
      this.fluidAmountStored += fluidStored;
      if (fluidAmountStored == 0) {
         setFluidStored(null);
      }
   }

   public Fluid getFluidStored() {
      return fluidStored;
   }

   public void setFluidStored(Fluid fluidStored) {
      this.fluidStored = fluidStored;
   }


   @Override
   public IBlockState getExtendedState(IBlockState state) {
      return state
              .withProperty(Properties.DOWN, connects(EnumFacing.DOWN))
              .withProperty(Properties.UP, connects(EnumFacing.UP))
              .withProperty(Properties.NORTH, connects(EnumFacing.NORTH))
              .withProperty(Properties.SOUTH, connects(EnumFacing.SOUTH))
              .withProperty(Properties.WEST, connects(EnumFacing.WEST))
              .withProperty(Properties.EAST, connects(EnumFacing.EAST));
   }

   @Override
   public BlockState createBlockState() {
      return new BlockState(MCMultiPartMod.multipart,
              Properties.DOWN,
              Properties.UP,
              Properties.NORTH,
              Properties.SOUTH,
              Properties.WEST,
              Properties.EAST);
   }

   @Override
   public AxisAlignedBB getRenderBoundingBox() {
      List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
      addSelectionBoxes(list);
      return list.get(0);
   }

   @Override
   public void addOcclusionBoxes(List<AxisAlignedBB> list) {
      list.add(AxisAlignedBB.fromBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
   }

   @Override
   public void addSelectionBoxes(List<AxisAlignedBB> list) {
      list.add(boundingBoxes[6]);
      for (EnumFacing f : EnumFacing.VALUES) {
         if (connects(f)) {
            list.add(boundingBoxes[f.ordinal()]);
         }
      }
   }

   @Override
   public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
      if (boundingBoxes[6].intersectsWith(mask)) {
         list.add(boundingBoxes[6]);
      }
      for (EnumFacing f : EnumFacing.VALUES) {
         if (connects(f)) {
            if (boundingBoxes[f.ordinal()].intersectsWith(mask)) {
               list.add(boundingBoxes[f.ordinal()]);
            }
         }
      }
   }

   @Override
   public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
      return layer == EnumWorldBlockLayer.CUTOUT;
   }

   // Tile logic

   public TileEntity getNeighbourTile(EnumFacing side) {
      return side != null ? getWorld().getTileEntity(getPos().offset(side)) : null;
   }

   private boolean internalConnects(EnumFacing side) {
      ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(side));
      if (part instanceof IMicroblock.IFaceMicroblock) {
         if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
            return false;
         }
      }

      //Is there a part blocking us?
      if (!OcclusionHelper.occlusionTest(getContainer().getParts(), this, boundingBoxes[side.ordinal()])) {
         return false;
      }

      //Is there a fluidpipe in the other block?
      if (MultipartHandler.getFluidPipe(getWorld(), getPos().offset(side), side.getOpposite()) != null) {
         return true;
      }


      TileEntity tile = getNeighbourTile(side);
      return tile instanceof IFluidHandler;

   }

   private void updateConnections(EnumFacing side) {
      if (side != null) {
         connectionCache &= ~(1 << side.ordinal());

         if (internalConnects(side)) {
            TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
            if (tileEntity instanceof TileMultipart) {
               PartFluidPipe pipe = MultipartHandler.getFluidPipe(((TileMultipart) tileEntity).getPartContainer());
               if (pipe != null && !pipe.internalConnects(side.getOpposite())) {
                  return;
               }
            }
            connectionCache |= 1 << side.ordinal();
         }
      } else {
         for (EnumFacing facing : EnumFacing.VALUES) {
            updateConnections(facing);
         }
      }
   }

   public boolean connects(EnumFacing side) {
      return (connectionCache & (1 << side.ordinal())) != 0;
   }

   private void updateNeighborInfo(boolean sendPacket) {
      if (!getWorld().isRemote) {
         byte oc = connectionCache;

         for (EnumFacing dir : EnumFacing.VALUES) {
            updateConnections(dir);
         }

         if (sendPacket && connectionCache != oc) {
            sendUpdatePacket();
         }
      }
   }


   @Override
   public void onAdded() {
      updateNeighborInfo(false);
   }

   @Override
   public void onLoaded() {
      neighborBlockChanged = true;
   }

   @Override
   public void onNeighborBlockChange(Block block) {
      neighborBlockChanged = true;
   }
}

