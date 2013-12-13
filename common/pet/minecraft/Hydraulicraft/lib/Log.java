package pet.minecraft.Hydraulicraft.lib;

import java.util.logging.Level;
import java.util.logging.Logger;

import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import cpw.mods.fml.common.FMLLog;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * @brief Class to log.
 */
public class Log {
	private static Logger logger = Logger.getLogger(ModInfo.ID);
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Initializes the log class
	 */
	public static void init(){
		logger.setParent(FMLLog.getLogger());
		logger.log(Level.INFO, ModInfo.NAME + " Starting!");
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Logs an info message to the console
	 */
	public static void info(String message){
		logger.log(Level.INFO, message);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Logs an error message to the console
	 */
	public static void error(String message){
		logger.log(Level.SEVERE, message);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Logs a warning message to the console
	 */
	public static void warning(String message){
		logger.log(Level.WARNING, message);
	}
}
