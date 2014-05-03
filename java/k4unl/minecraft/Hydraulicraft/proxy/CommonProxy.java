package k4unl.minecraft.Hydraulicraft.proxy;

import net.minecraft.client.model.ModelBiped;


public class CommonProxy /* FMP implements IPartFactory */{
	
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
/*FMP
	@Override
	public TMultiPart createPart(String id, boolean client) {
		if(id.equals(Names.partHose[0].unlocalized)){
			return new PartHose();
		}
		return null;
	}
	*/
	
	public ModelBiped getArmorModel(int id){
		return null;
	}

}
