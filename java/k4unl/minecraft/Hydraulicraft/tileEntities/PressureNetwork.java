package k4unl.minecraft.Hydraulicraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.k4lib.lib.Location;
import mcmultipart.block.TileMultipart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PressureNetwork {
    private float pressure = 0;
    private List<networkEntry> machines;
    private int randomNumber = 0;
    private IBlockAccess world;
    private int          fluidInNetwork = 0;
    private int          fluidCapacity  = 0;
    private boolean      isOilStored    = false;
    private PressureTier lowestTier     = PressureTier.INVALID;
    public PressureNetwork(IHydraulicMachine machine, float beginPressure, EnumFacing from) {

        randomNumber = new Random().nextInt();
        machines = new ArrayList<networkEntry>();
        machines.add(new networkEntry(((TileHydraulicBase) machine.getHandler()).getBlockLocation(), from));
        pressure = beginPressure;
        world = ((TileHydraulicBase) machine.getHandler()).getWorldObj();
        isOilStored = machine.getHandler().isOilStored();

        lowestTier = machine.getHandler().getPressureTier();
    }

    public PressureNetwork(NBTTagCompound compoundTag) {
        randomNumber = compoundTag.getInteger("randomNumber");
        machines = new ArrayList<networkEntry>();
        for(int i = 0; i <= compoundTag.getTagList("machines", 10).tagCount(); i++){
            machines.add(new networkEntry(compoundTag.getTagList("machines", 10).getCompoundTagAt(i)));
        }
        pressure = compoundTag.getFloat("pressure");
        isOilStored = compoundTag.getBoolean("isOilStored");
        lowestTier = PressureTier.fromOrdinal(compoundTag.getInteger("lowestTier"));
    }

    public void setWorld(IBlockAccess world) {
        this.world = world;
    }

    public static PressureNetwork getNetworkInDir(IBlockAccess iba, BlockPos pos, EnumFacing dir) {
        TileEntity t = iba.getTileEntity(pos);
        if (t instanceof IHydraulicMachine || t instanceof TileMultipart) {
            IHydraulicMachine mEnt;
            boolean isMultipart = false;
            if (t instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) t).getPartContainer())) {
                mEnt = MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer());
                isMultipart = true;
            } else {
                mEnt = (IHydraulicMachine) t;
            }

            //List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
            PressureNetwork foundNetwork = null;
            if (isMultipart) {
                if (mEnt instanceof IHydraulicTransporter) {
                    if (((IHydraulicTransporter) mEnt).isConnectedTo(dir)) {
                        TileEntity tn = iba.getTileEntity(pos.offset(dir));

                        if (tn instanceof IHydraulicMachine) {
                            if (((IHydraulicMachine) tn).canConnectTo(dir.getOpposite())) {
                                foundNetwork = ((TileHydraulicBase) ((IHydraulicMachine) tn).getHandler()).getNetwork(dir.getOpposite());
                            }
                        } else if (tn instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) tn).getPartContainer())) {
                            if (MultipartHandler.getTransporter(((TileMultipart) tn).getPartContainer()).isConnectedTo(dir.getOpposite())) {
                                foundNetwork = ((TileHydraulicBase) (MultipartHandler.getTransporter(((TileMultipart) tn).getPartContainer())).getHandler()).getNetwork(dir.getOpposite());
                            }
                        }
                    }
                } else {
                    TileEntity tn = iba.getTileEntity(pos.offset(dir));

                    if (tn instanceof IHydraulicTransporter) {
                        if (((IHydraulicMachine) tn).canConnectTo(dir.getOpposite())) {
                            foundNetwork = ((TileHydraulicBase) ((IHydraulicMachine) tn).getHandler()).getNetwork(dir.getOpposite());
                        }
                    }
                }
            } else {
                BlockPos offset = pos.offset(dir);
                TileEntity tn = iba.getTileEntity(offset);
                if (tn instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) tn).getPartContainer())) {
                    if (MultipartHandler.getTransporter(((TileMultipart) tn).getPartContainer()).isConnectedTo(dir.getOpposite())) {
                        foundNetwork = ((TileHydraulicBase) (MultipartHandler.getTransporter(((TileMultipart) tn).getPartContainer())).getHandler()).getNetwork(dir.getOpposite());
                    }
                }
            }
            return foundNetwork;
        } else {
            return null;
        }
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public PressureTier getLowestTier() {
        return lowestTier;
    }

    private int contains(IHydraulicMachine machine) {
        int i;
        for (i = 0; i < machines.size(); i++) {
            if(machines.get(i) != null) {
                if (machines.get(i).getLocation().equals(((TileHydraulicBase) machine.getHandler()).getBlockLocation())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void addMachine(IHydraulicMachine machine, float pressureToAdd, EnumFacing from) {
        if (contains(machine) == -1) {
            //float oPressure = pressure * machines.size();
            //oPressure += pressureToAdd;
            machines.add(new networkEntry(((TileHydraulicBase) machine.getHandler()).getBlockLocation(), from));
            //pressure = oPressure / machines.size();
            machine.getHandler().updateFluidOnNextTick();
            if (world == null) {
                world = ((TileHydraulicBase) machine.getHandler()).getWorldObj();
            }

            PressureTier newestTier = machine.getHandler().getPressureTier();
            isOilStored = machine.getHandler().isOilStored();

            if (newestTier.ordinal() < lowestTier.ordinal()) {
                lowestTier = newestTier;
            }

        }
    }

    public void removeMachine(IHydraulicMachine machineToRemove) {
        int machineIndex = contains(machineToRemove);
        if (machineIndex != -1) {
            ((TileHydraulicBase) machineToRemove.getHandler()).setNetwork(machines.get(machineIndex).getFrom(), null);
            machines.remove(machineIndex);
        }
        //And tell every machine in the block to recheck it's network! :D
        //Note, this might cost a bit of time..
        //There should be a better way to do this..
        for (networkEntry entry : machines) {
            Location loc = entry.getLocation();
            TileEntity ent = world.getTileEntity(loc.toBlockPos());
            if (ent instanceof IHydraulicMachine) {
                IHydraulicMachine machine = (IHydraulicMachine) ent;
                ((TileHydraulicBase) machineToRemove.getHandler()).setNetwork(entry.getFrom(), null);
                machine.getHandler().updateNetworkOnNextTick(getPressure());
            } else if (ent instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) ent).getPartContainer())) {
                IHydraulicMachine machine = MultipartHandler.getTransporter(((TileMultipart) ent).getPartContainer());
                ((TileHydraulicBase) machine.getHandler()).setNetwork(entry.getFrom(), null);
                machine.getHandler().updateNetworkOnNextTick(getPressure());
            }
        }

    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float newPressure) {
        pressure = newPressure;
    }

    public boolean getIsOilStored() {
        return isOilStored;
    }

    public List<networkEntry> getMachines() {
        return machines;
    }

    public void mergeNetwork(PressureNetwork toMerge) {
        //Log.info("Trying to merge network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
        if (toMerge.equals(this)) return;

        float newPressure = (pressure + toMerge.getPressure()) / 2;
        setPressure(newPressure);

        List<networkEntry> otherList = toMerge.getMachines();

        for (networkEntry entry : otherList) {
            Location loc = entry.getLocation();
            TileEntity ent = world.getTileEntity(loc.toBlockPos());
            if (ent instanceof IHydraulicMachine) {
                IHydraulicMachine machine = (IHydraulicMachine) ent;
                ((TileHydraulicBase) machine.getHandler()).setNetwork(entry.getFrom(), this);
                this.addMachine(machine, newPressure, entry.getFrom());
            } else if (ent instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) ent).getPartContainer())) {
                IHydraulicMachine machine = MultipartHandler.getTransporter(((TileMultipart) ent).getPartContainer());
                ((TileHydraulicBase) machine.getHandler()).setNetwork(entry.getFrom(), this);
                this.addMachine(machine, newPressure, entry.getFrom());
            }
        }

        //Log.info("Merged network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
    }

    public void writeToNBT(NBTTagCompound tagCompound) {

        tagCompound.setInteger("randomNumber", randomNumber);

        NBTTagList machinesList = new NBTTagList();
        for(int i = 0; i < machines.size(); i++){
            machinesList.appendTag(machines.get(i).saveEntry());
        }
        tagCompound.setFloat("pressure", pressure);

        tagCompound.setBoolean("isOilStored", isOilStored);
        tagCompound.setInteger("lowestTier", lowestTier.ordinal());
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        randomNumber = tagCompound.getInteger("randomNumber");
        machines = new ArrayList<networkEntry>();
        for(int i = 0; i <= tagCompound.getTagList("machines", 10).tagCount(); i++){
            machines.add(new networkEntry(tagCompound.getTagList("machines", 10).getCompoundTagAt(i)));
        }
        pressure = tagCompound.getFloat("pressure");
        isOilStored = tagCompound.getBoolean("isOilStored");
        lowestTier = PressureTier.fromOrdinal(tagCompound.getInteger("lowestTier"));
    }

    public int getFluidCapacity() {
        return fluidCapacity;
    }

    public int getFluidInNetwork() {
        return fluidInNetwork;
    }

    public void updateFluid(IHydraulicMachine mEnt) {

        boolean canOilBeStored = false;
        isOilStored = mEnt.getHandler().isOilStored();

        if (!isOilStored && mEnt.getHandler().getStored() == 0) {
            canOilBeStored = true;
        }
        fluidInNetwork = 0;
        fluidCapacity = 0;

        List<IHydraulicMachine> mainList = new ArrayList<IHydraulicMachine>();

        for (networkEntry entry : machines) {
            Location loc = entry.getLocation();
            TileEntity ent = world.getTileEntity(loc.toBlockPos());
            IHydraulicMachine machine = null;
            if (ent instanceof IHydraulicMachine) {
                machine = (IHydraulicMachine) ent;
            } else if (ent instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) ent).getPartContainer())) {
                machine = MultipartHandler.getTransporter(((TileMultipart) ent).getPartContainer());
            }

            if (machine != null) {
                if (((getIsOilStored() && machine.getHandler().isOilStored()) || canOilBeStored) || (!getIsOilStored() && !machine.getHandler().isOilStored()) || machine.getHandler().getStored() == 0) { //Otherwise we would be turning water into oil

                    fluidInNetwork = fluidInNetwork + machine.getHandler().getStored();
                    fluidCapacity = fluidCapacity + machine.getHandler().getMaxStorage();
                    if (canOilBeStored && !isOilStored && machine.getHandler().isOilStored()) {
                        isOilStored = true;
                    }
                    machine.getHandler().setStored(0, isOilStored, false);
                    mainList.add(machine);
                }
            }
        }
        disperseFluid(mainList);
    }

    @SuppressWarnings("cast")
    private void disperseFluid(List<IHydraulicMachine> mainList) {
        List<IHydraulicMachine> remainingBlocks = new ArrayList<IHydraulicMachine>();
        float newFluidInSystem = 0;
        float fluidInSystem = fluidInNetwork;
        //Log.info("Before iteration: FIS = " + fluidInSystem + " M = " + mainList.size());
        while (fluidInSystem > 0) {
            if (mainList.size() == 0) {
                //Error!
                Log.error("Too much fluid in the system!");
                break;
            }
            float toSet = fluidInSystem / (float) mainList.size();

            for (IHydraulicMachine machineEntity : mainList) {
                if (machineEntity.getHandler().getMaxStorage() < (toSet + machineEntity.getHandler().getStored())) {
                    //This machine can't store this much!
                    newFluidInSystem = newFluidInSystem + ((toSet + machineEntity.getHandler().getStored()) - machineEntity.getHandler().getMaxStorage());
                    machineEntity.getHandler().setStored(machineEntity.getHandler().getMaxStorage(), isOilStored, false);
                } else {
                    remainingBlocks.add(machineEntity);
                    machineEntity.getHandler().setStored((int) toSet + machineEntity.getHandler().getStored(), isOilStored, false);
                }
            }

            //Log.info("Iteration done. Fluid remaining: " + newFluidInSystem);
            fluidInSystem = newFluidInSystem;
            newFluidInSystem = 0;

            mainList.clear();
            for (IHydraulicMachine machineEntity : remainingBlocks) {
                mainList.add(machineEntity);
            }

            remainingBlocks.clear();
        }
    }

    public static class networkEntry {
        private Location       blockLocation;
        private EnumFacing from;

        public networkEntry(Location nLocation, EnumFacing nFrom) {

            blockLocation = nLocation;
            from = nFrom;
        }

        public networkEntry(NBTTagCompound compoundTag) {

            blockLocation = new Location(compoundTag.getIntArray("blockLocation"));
            from = EnumFacing.byName(compoundTag.getString("from"));
        }

        public NBTTagCompound saveEntry(){
            NBTTagCompound ret = new NBTTagCompound();

            ret.setIntArray("blockLocation", blockLocation.getIntArray());
            if(from != null)
                ret.setString("from", from.toString());

            return ret;
        }

        public Location getLocation() {

            return blockLocation;
        }

        public EnumFacing getFrom() {

            return from;
        }
    }

}
