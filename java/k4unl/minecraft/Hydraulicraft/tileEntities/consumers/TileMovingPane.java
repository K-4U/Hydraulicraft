package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class TileMovingPane extends TileHydraulicBase implements IHydraulicConsumer {
    private Location parent;
    private Location child;
    private EnumFacing facing = EnumFacing.UP;
    private EnumFacing paneFacing = EnumFacing.NORTH;

    private boolean isPane = false;
    private boolean isRotating = false;

    private float movedPercentage = 0.0F;
    private float prevMovedPercentage = 0.0F;
    private float movingSpeed = 0.01F;
    private float target = 1.0F;

    public TileMovingPane() {

        super(5);
        super.init(this);
    }

    public boolean getIsRotating() {

        return isRotating;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        isPane = tagCompound.getBoolean("isPane");
        if (isPane) {
            parent = new Location(tagCompound.getIntArray("parent"));
        } else {
            child = new Location(tagCompound.getIntArray("child"));
        }
        facing = EnumFacing.byName(tagCompound.getString("facing"));
        paneFacing = EnumFacing.byName(tagCompound.getString("paneFacing"));

        if (isPane) {
            isRotating = tagCompound.getBoolean("isRotating");
            movedPercentage = tagCompound.getFloat("movedPercentage");
            movingSpeed = tagCompound.getFloat("movingSpeed");
            target = tagCompound.getFloat("target");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (isPane) {
            if (parent != null) {
                tagCompound.setIntArray("parent", parent.getLocation());
                ;
            }
        } else {
            if (child != null) {
                tagCompound.setIntArray("child", child.getLocation());
                ;
            }
        }
        tagCompound.setBoolean("isPane", isPane);
        tagCompound.setString("facing", facing.getName());
        tagCompound.setString("paneFacing", paneFacing.getName());

        tagCompound.setBoolean("isRotating", isRotating);
        tagCompound.setFloat("movedPercentage", movedPercentage);
        tagCompound.setFloat("movingSpeed", movingSpeed);
        tagCompound.setFloat("target", target);
    }

    @Override
    public void update() {
        super.update();

        if (isRotating && isPane) {
            if (getParent() == null) {
                //This happens when the block is broken.
                return;
            }
            if (getParent().hasEnoughPressure()) {
                prevMovedPercentage = movedPercentage;
                movedPercentage += (movingSpeed * getParent().getPressureFactor());
                if (Float.compare(movedPercentage, target) >= 0 && movingSpeed > 0.0F) {
                    isRotating = false;
                    prevMovedPercentage = target;
                    movedPercentage = target;
                    getHandler().updateBlock();
                } else if (Float.compare(movedPercentage, target) <= 0 && movingSpeed <= 0.0F) {
                    isRotating = false;
                    movingSpeed = 0.0F;
                    prevMovedPercentage = target;
                    movedPercentage = target;
                    getHandler().updateBlock();
                }
            }
        }
        if (!worldObj.isRemote && !isPane) {
            if (getChild() == null) {
                return;
            }
            if (getRedstonePowered(this)) {
                if (Float.compare(target, 1.0F) != 0 && Float.compare(movingSpeed, 0.0F) < 0) {
                    movingSpeed = 0.1F;
                    target = 1.0F;
                    isRotating = true;
                    getChild().setSpeed(movingSpeed);
                    getChild().setTarget(target);
                    getHandler().updateBlock();
                }
            } else if (Float.compare(target, 0.0F) != 0 && Float.compare(movingSpeed, 0.0F) >= 0) {
                movingSpeed = -0.1F;
                target = 0.0F;
                isRotating = true;
                getChild().setSpeed(movingSpeed);
                getChild().setTarget(target);
                getHandler().updateBlock();
            }
        }
    }

    private boolean hasEnoughPressure() {
        return getPressure(EnumFacing.UP) > Constants.MIN_PRESSURE_PANE;
    }

    private float getPressureFactor() {
        return getPressure(EnumFacing.UP) / getMaxPressure(isOilStored(), EnumFacing.UP);
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        if (isPane) {
            return false;
        }
        return !side.equals(getFacing());
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing n) {
        facing = n;
        getHandler().updateBlock();
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {
        if (!isPane) {
            if (getChild() != null) {
                if (getChild().getIsRotating()) {
                    return Constants.PRESSURE_PANE_PER_TICK * (10 * getPressureFactor());
                }
            }
        }
        return 0f;
    }

    @Override
    public boolean canWork(EnumFacing dir) {
        return true;
    }

    public void setIsPane(boolean isIt) {
        isPane = isIt;
        getHandler().updateBlock();
    }

    public boolean getIsPane() {
        return isPane;
    }

    public void setParentLocation(Location p) {
        parent = p;
    }

    public Location getParentLocation() {
        return parent;
    }

    public void setChildLocation(Location c) {
        child = c;
    }

    public Location getChildLocation() {
        return child;
    }

    public float getMovedPercentage() {
        return movedPercentage;
    }

    public float getMovedPercentageForRender(float f) {
        if (isRotating) {
            return getMovedPercentage() + (getMovedPercentage() - prevMovedPercentage) * f;
        } else {
            return getMovedPercentage();
        }
    }

    public void setPaneFacing(EnumFacing n) {
        paneFacing = n;
    }

    public EnumFacing getPaneFacing() {
        return paneFacing;
    }

    public void setTarget(float nTarget) {
        target = nTarget;
        isRotating = true;
        getHandler().updateBlock();
    }

    public void setSpeed(float nSpeed) {
        movingSpeed = nSpeed;
        getHandler().updateBlock();
    }

    public TileMovingPane getChild() {
        return (TileMovingPane) worldObj.getTileEntity(child.toBlockPos());
    }

    public TileMovingPane getParent() {
        return (TileMovingPane) worldObj.getTileEntity(parent.toBlockPos());
    }

    @Override
    public boolean getRedstonePowered() {
        return super.getRedstonePowered();
    }

    public boolean getRedstonePowered(Object caller) {
        List<Object> called = new ArrayList<Object>();
        return getRedstonePowered(called);
    }

    public boolean getRedstonePowered(List<Object> called) {
        called.add(this);

        boolean allFalse = true;
        boolean oneTrue = false;
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity te = new Location(getPos()).getTE(getWorld(), dir);
            if (te instanceof TileMovingPane) {
                if (!called.contains(te)) {
                    if (((TileMovingPane) te).getRedstonePowered(called)) {
                        oneTrue = true;
                        allFalse = false;
                    }
                }
            }
        }
        if (oneTrue) {
            return true;
        }
        if (allFalse) {
            return super.getRedstonePowered();
        }
        return super.getRedstonePowered();
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }
}
