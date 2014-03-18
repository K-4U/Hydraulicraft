package k4unl.minecraft.Hydraulicraft.proxy;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

public class CommonProxy implements IPartFactory {
	
	public void init(){
		
	}
	
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Initializes renderers for custom models.
	 */
	public void initRenderers(){
		
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Initializes and loads the sounds.
	 */
	public void initSounds(){
		
	}

	@Override
	public TMultiPart createPart(String id, boolean client) {
		if(id.equals(Names.blockHydraulicHose[0].unlocalized)){
			return new PartHose();
		}
		return null;
	}
}
