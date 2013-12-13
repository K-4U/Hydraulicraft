package pet.minecraft.Hydraulicraft.lib.helperClasses;

import net.minecraftforge.common.Configuration;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * Class that holds an ID.
 */
public class Id {
	public int def = 0;
	public int act = 0;
	
	public Id(int _def){
		def = _def;
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loads a block ID from the config file for given name n
	 */
	public void loadBlock(Configuration config, Name n){
		this.act = config.getBlock(n.unlocalized, def).getInt();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loads a item ID from the config file for given name n
	 */
	public void loadItem(Configuration config, Name n){
		this.act = config.getItem(n.unlocalized, def).getInt();
	}
	
}
