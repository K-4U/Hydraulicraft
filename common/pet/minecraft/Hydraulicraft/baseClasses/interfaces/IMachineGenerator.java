package pet.minecraft.Hydraulicraft.baseClasses.interfaces;

public interface IMachineGenerator {
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Function that gets called when there's work to be done
	 * Probably every tick or so.
	 */
	public void workFunction();
	
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Returns how much the generator can max output
	 */
	public int getMaxGenerating();
	
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Returns how much the generator is now generating
	 */
	public int getBar();
}
