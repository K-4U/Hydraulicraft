package k4unl.minecraft.Hydraulicraft.api;

public interface IHydraulicGenerator extends IHydraulicMachine {
    /**
     * Function that gets called when there's work to be done
     * Probably every tick or so.
     * @author Koen Beckers
     * @date 14-12-2013
     */
    public abstract void workFunction();
    
    
    /**
     * @author Koen Beckers
     * @date 14-12-2013
     * @return How much the generator can max output
     */
    public abstract int getMaxGenerating();
    
    
    /**
     * @author Koen Beckers
     * @date 14-12-2013
     * @return How much the generator is currently generating
     */
    public abstract float getGenerating();
}
