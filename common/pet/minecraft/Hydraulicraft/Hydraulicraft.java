package pet.minecraft.Hydraulicraft;

import pet.minecraft.Hydraulicraft.TileEntities.TileEntities;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.fluids.Fluids;
import pet.minecraft.Hydraulicraft.items.Items;
import pet.minecraft.Hydraulicraft.lib.ConfigHandler;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.Log;
import pet.minecraft.Hydraulicraft.lib.Recipes;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION
)

@NetworkMod (
	channels = {ModInfo.CHANNEL},
	clientSideRequired = true,
	serverSideRequired = true
)

public class Hydraulicraft {
	//This is the instance that Forge uses:
	@Instance(value=ModInfo.ID)
	public static Hydraulicraft instance;
	
	@SidedProxy(
		clientSide = ModInfo.PROXY_LOCATION + ".ClientProxy",
		serverSide = ModInfo.PROXY_LOCATION + ".CommonProxy"
	)
	public static CommonProxy proxy;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Before the mod initializes
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Log.init();
		
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		
		proxy.initRenderers();
		proxy.initSounds();
		CustomTabs.init();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loading the mod!
	 */
	@EventHandler
	public void load(FMLInitializationEvent event){
		Blocks.init();
		TileEntities.init();
		Items.init();
		Fluids.init();
		Recipes.init();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * After the mod has been loaded.
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		Log.info("Hydraulicraft ready for use!");
	}
	
	
}
