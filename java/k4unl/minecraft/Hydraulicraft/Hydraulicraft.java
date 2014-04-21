package k4unl.minecraft.Hydraulicraft;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileEntities;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiHandler;
import k4unl.minecraft.Hydraulicraft.events.EventHelper;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.ConfigHandler;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.Recipes;
import k4unl.minecraft.Hydraulicraft.lib.UpdateChecker;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import k4unl.minecraft.Hydraulicraft.proxy.CommonProxy;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdParty;
import k4unl.minecraft.Hydraulicraft.world.OreGenerator;
import thirdParty.truetyper.TrueTypeFont;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

//FIXME: Fluid not being saved in NBT

@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION
	//dependencies = "required-after:ForgeMultipart@1.0.0.250"
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
	public static Multipart mp;
	
	public static TrueTypeFont smallGuiFont;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Before the mod initializes
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Log.init();
		
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		
		CustomTabs.init();
		
		HCBlocks.init();
		Ores.init();
		TileEntities.init();
		Fluids.init();
		
		HCItems.init();
		EventHelper.init();
		
		
		
		mp = new Multipart();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loading the mod!
	 */
	@EventHandler
	public void load(FMLInitializationEvent event){
		//REMOVE ME WHEN RELEASING!
		FMLClientHandler.instance().setDefaultMissingAction(FMLMissingMappingsEvent.Action.IGNORE);
		
		
		ThirdParty.init();
		
		GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new GuiHandler());
		
		
		proxy.init();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * After the mod has been loaded.
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		Recipes.init();
		
		ThirdParty.postInit();
		
		UpdateChecker.checkUpdateAvailable();
		Log.info("Hydraulicraft ready for use!");
	}
	
	
}
