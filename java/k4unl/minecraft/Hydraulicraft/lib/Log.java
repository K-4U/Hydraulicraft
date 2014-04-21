package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * @brief Class to log.
 */
public class Log {
	private static Logger logger = LogManager.getLogger(ModInfo.ID);
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Initializes the log class
	 */
	public static void init(){
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
		logger.log(Level.ERROR, message);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * @brief Logs a warning message to the console
	 */
	public static void warning(String message){
		logger.log(Level.WARN, message);
	}
}
