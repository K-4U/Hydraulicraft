package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IConnectTexture;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileHydraulicValve extends TileHydraulicBase implements IHydraulicMachine, IConnectTexture {
    private BlockPos targetPos;
    private boolean targetHasChanged = true;
    private IHydraulicConsumer target;
    private boolean clientNeedsToResetTarget = false;
    private boolean clientNeedsToSetTarget = false;


    public TileHydraulicValve() {
        super(1);
        super.init(this);
    }

    public void resetTarget() {
        target = null;
        targetPos = getPos();
        targetHasChanged = true;
        if (pNetwork != null) {
            pNetwork.removeMachine(this);
        }
        if (!worldObj.isRemote) {
            clientNeedsToResetTarget = true;
        }
        getHandler().updateBlock();
        getHandler().updateNetworkOnNextTick(0);
    }

    public void setTarget(BlockPos pos) {
        targetPos = pos;
        targetHasChanged = true;
        if (pNetwork != null) {
            pNetwork.removeMachine(this);
        }
        if (!worldObj.isRemote) {
            clientNeedsToSetTarget = true;
        }
        //getHandler().updateBlock();
    }

    public IHydraulicConsumer getTarget() {
        if(targetPos != null) {
            if (targetHasChanged && !targetPos.equals(getPos())) {
                TileEntity t = worldObj.getTileEntity(targetPos);
                if (t instanceof IHydraulicConsumer) {
                    target = (IHydraulicConsumer) t;
                    targetHasChanged = false;
                }
            } else if (targetHasChanged && targetPos.equals(getPos())) {
                target = null;
                //targetHasChanged = false;
            }
        }
        return target;
    }

    @Override
    public int getMaxStorage() {
        if (getTarget() == null) {
            return 0;
        } else {
            return getTarget().getHandler().getMaxStorage();
        }
    }

    @Override
    public float getMaxPressure(boolean isOil, EnumFacing from) {
        if (getTarget() == null) {
            return 0F;
        } else {
            return getTarget().getHandler().getMaxPressure(isOil, from);
        }
    }

    @Override
    public void onBlockBreaks() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        targetPos = BlockPos.fromLong(tagCompound.getLong("targetPos"));
        if (tagCompound.getBoolean("isTargetNull")) {
            target = null;
        }
        if (worldObj != null && worldObj.isRemote) {
            if (tagCompound.getBoolean("clientNeedsToResetTarget")) {
                resetTarget();
            }
            if (tagCompound.getBoolean("clientNeedsToSetTarget")) {
                targetHasChanged = true;
                getTarget();
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if(targetPos != null) {
            tagCompound.setLong("targetPos", targetPos.toLong());
        }
        tagCompound.setBoolean("isTargetNull", (target == null));
        if (worldObj != null && !worldObj.isRemote) {
            tagCompound.setBoolean("clientNeedsToResetTarget", clientNeedsToResetTarget);
            tagCompound.setBoolean("clientNeedsToSetTarget", clientNeedsToSetTarget);
            clientNeedsToResetTarget = false;
            clientNeedsToSetTarget = false;
        }
        tagCompound.setBoolean("targetHasChanged", targetHasChanged);
    }

    @Override
    public void onFluidLevelChanged(int old) {
        if (getTarget() != null) {
            getTarget().getHandler().setStored(getHandler().getStored(), getHandler().isOilStored(), false);
        }
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return getTarget() != null;
    }

    @Override
    public void updateNetwork(float oldPressure) {
        if (getTarget() == null) {
            pNetwork = null;
            getHandler().updateBlock();
            return;
        }
        PressureNetwork newNetwork = null;
        PressureNetwork foundNetwork;
        PressureNetwork endNetwork = null;
        //This block can merge networks!
        for (EnumFacing dir : EnumFacing.VALUES) {
            foundNetwork = PressureNetwork.getNetworkInDir(worldObj, getPos(), dir);
            if (foundNetwork != null) {
                if (endNetwork == null) {
                    endNetwork = foundNetwork;
                } else {
                    newNetwork = foundNetwork;
                }
                connectedSides.add(dir);
            }

            if (newNetwork != null) {
                //Hmm.. More networks!? What's this!?
                endNetwork.mergeNetwork(newNetwork);
                newNetwork = null;
            }
        }

        if (endNetwork != null) {
            pNetwork = endNetwork;
            pNetwork.addMachine(this, oldPressure, EnumFacing.UP);
            if (getTarget() != null) {
                ((TileHydraulicBase) getTarget().getHandler()).setNetwork(EnumFacing.UP, pNetwork);
                pNetwork.addMachine(getTarget(), oldPressure, EnumFacing.UP);
            }
            for (EnumFacing dir : connectedSides) {
                Location hoseLocation = new Location(getPos(), dir);
                TileEntity ent = getWorldObj().getTileEntity(hoseLocation.toBlockPos());
                //((TilePressureHose)ent).checkConnectedSides(this);

                /*if (ent instanceof TileMultipart && Multipart.hasTransporter((TileMultipart) ent)) {
                    IHydraulicTransporter hose = Multipart.getTransporter((TileMultipart) ent);
                    hose.checkConnectedSides();
                } else */if (ent instanceof IHydraulicTransporter) {
                    ((IHydraulicTransporter) ent).checkConnectedSides();
                }
            }
            //Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            pNetwork = new PressureNetwork(this, oldPressure, EnumFacing.UP);
            if (getTarget() != null) {
                ((TileHydraulicBase) getTarget().getHandler()).setNetwork(EnumFacing.UP, pNetwork);
                pNetwork.addMachine(getTarget(), oldPressure, EnumFacing.UP);
            }
            //Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        }
    }

    @Override
    public boolean connectTexture() {
        return getTarget() != null;
    }

    @Override
    public boolean connectTextureTo(Block type) {
        return connectTexture() && (type instanceof BlockHydraulicPressureWall || type instanceof BlockInterfaceValve);
    }
}
