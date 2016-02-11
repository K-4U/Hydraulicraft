package k4unl.minecraft.Hydraulicraft.api;


import net.minecraft.util.EnumFacing;

public interface IHydraulicTransporter extends IHydraulicMachine {

    /**
     * @param dir
     * @return Whether or not the machine is connected. It is different to canConnectTo
     */
    boolean isConnectedTo(EnumFacing dir);
}
