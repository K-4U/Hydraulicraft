package k4unl.minecraft.Hydraulicraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicraftRegistrar;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiHandler;
import k4unl.minecraft.Hydraulicraft.events.EventHelper;
import k4unl.minecraft.Hydraulicraft.events.TickHandler;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.*;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.network.PacketPipeline;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import k4unl.minecraft.Hydraulicraft.proxy.CommonProxy;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdPartyManager;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileEntities;
import k4unl.minecraft.Hydraulicraft.world.OreGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import thirdParty.truetyper.TrueTypeFont;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	dependencies = "required-after:ForgeMultipart@1.1.0.275"
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
	public static TrueTypeFont mediumGuiFont;
	
	public static HydraulicraftRegistrar harvesterTrolleyRegistrar = new HydraulicraftRegistrar();
	public static IPs ipList = new IPs();
    public static Tanks tankList = new Tanks();
	
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
		
		ThirdPartyManager.instance().preInit();
		
		Multipart.init();
		TickHandler.init();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loading the mod!
	 */
	@EventHandler
	public void load(FMLInitializationEvent event){
		ThirdPartyManager.instance().init();
		
		GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new GuiHandler());
		PacketPipeline.init();

		UpdateChecker.checkUpdateAvailable();
		
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
		
		ThirdPartyManager.instance().postInit();
        ConfigHandler.loadTank();

		Log.info("Hydraulicraft ready for use!");
	}
	
	
	@EventHandler
    public void processIMCRequests(FMLInterModComms.IMCEvent event){
        List<FMLInterModComms.IMCMessage> messages = event.getMessages();
        for(FMLInterModComms.IMCMessage message : messages) {
            try {
                if(message.key.equals("registerCrushingRecipe")){
                    NBTTagCompound toRegister = message.getNBTValue();

                    ItemStack from = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemFrom"));
                    ItemStack to = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemTo"));
                    float pressureRatio = toRegister.getFloat("pressureRatio");
                    CrushingRecipes.addCrushingRecipe(new CrushingRecipes
                            .CrushingRecipe(from, pressureRatio,
                            to));
                }else{
                    Class clazz = Class.forName(message.key);
                    try {
                        Method method = clazz.getMethod(message.getStringValue(), IHydraulicraftRegistrar.class);
                        try {
                            method.invoke(null, Hydraulicraft.harvesterTrolleyRegistrar);
                            Log.info("Successfully gave " + message.getSender() + " a nudge! Happy to be doing business!");
                        } catch(IllegalAccessException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be accessed: " + message.getStringValue());
                        } catch(IllegalArgumentException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method has no single IHydraulicraftRegistrar argument or it isn't static: " + message.getStringValue());
                        } catch(InvocationTargetException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method threw an exception: " + message.getStringValue());
                            e.printStackTrace();
                        }
                    } catch(NoSuchMethodException e) {
                        Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be found: " + message.getStringValue());
                    } catch(SecurityException e) {
                        Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be accessed: " + message.getStringValue());
                    }
                }
            } catch(ClassNotFoundException e) {
                Log.error(message.getSender() + " tried to register to HydCraft. Failed because the class can NOT be found: " + message.key);
            }

        }
    }
	

	@EventHandler
	public void serverStart(FMLServerStartingEvent event){
		ipList.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
        tankList.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
	}
	
	@EventHandler
	public void serverStop(FMLServerStoppingEvent event){
		ipList.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
        tankList.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
	}
	
}


