package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.util.EnumFacing;

public interface IHydraulicGenerator extends IHydraulicMachine {

    /**
     * Function that gets called when there's work to be done
     * You need to take care of adding pressure to the network!
     *
     * @param from TODO
     * @author Koen Beckers
     * @date 14-12-2013
     */
    void workFunction(EnumFacing from);
    
    /**
     * Returns whether or not this block can do work on this side.
     * DO NOT JUST RETURN TRUE!
     * If it doesn't matter which direction this block can do it's work from,
     * Only return true on EnumFacing.UP!
     * If you return true on all directions, the block will be doing way too much work!
     *
     * @param dir
     * @return Whether or not this block can do work from this side.
     */
    boolean canWork(EnumFacing dir);
    
    /**
     * @param from TODO
     * @return How much the generator can max output
     * @author Koen Beckers
     * @date 14-12-2013
     */
    int getMaxGenerating(EnumFacing from);
    
    
    /**
     * @param from TODO
     * @return How much the generator is currently generating
     * @author Koen Beckers
     * @date 14-12-2013
     */
    float getGenerating(EnumFacing from);
}
