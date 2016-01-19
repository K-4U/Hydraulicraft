package k4unl.minecraft.Hydraulicraft;

import k4unl.minecraft.Hydraulicraft.api.HCApi;
import k4unl.minecraft.Hydraulicraft.api.ITrolleyRegistrar;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiHandler;
import k4unl.minecraft.Hydraulicraft.events.EventHelper;
import k4unl.minecraft.Hydraulicraft.events.TickHandler;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.*;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import k4unl.minecraft.Hydraulicraft.proxy.CommonProxy;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdPartyManager;
import k4unl.minecraft.Hydraulicraft.thirdParty.igwmod.IGWSupportNotifier;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileEntities;
import k4unl.minecraft.Hydraulicraft.world.HCWorldGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thirdParty.truetyper.TrueTypeFont;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Mod(
        modid = ModInfo.ID,
        name = ModInfo.NAME,
        version = ModInfo.VERSION,
        dependencies = /*"required-after:ForgeMultipart@[1.1.0.297,);*/ "required-after:k4lib"
)

public class Hydraulicraft {
    //This is the instance that Forge uses:
    @Mod.Instance(value = ModInfo.ID)
    public static Hydraulicraft instance;

    @SidedProxy(
            clientSide = ModInfo.PROXY_LOCATION + ".ClientProxy",
            serverSide = ModInfo.PROXY_LOCATION + ".CommonProxy"
    )
    public static CommonProxy      proxy;
    public static MultipartHandler mp;

    public static TrueTypeFont smallGuiFont;
    public static TrueTypeFont mediumGuiFont;

    public static TrolleyRegistrar trolleyRegistrar = new TrolleyRegistrar();
    public static IPs              ipList           = new IPs();
    public static Tanks            tankList         = new Tanks();

    public static HCConfigHandler configHandler = new HCConfigHandler();

    public static int pressure = 0;
    public static boolean hasPressureGaugeInInventory = false;

    /*!
     * @author Koen Beckers
	 * @date 13-12-2013
	 * Before the mod initializes
	 */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();

        HCConfig.INSTANCE.init();
        configHandler.init(HCConfig.INSTANCE, event.getSuggestedConfigurationFile());

        HCApi.init(new HydraulicraftAPI());

        CustomTabs.init();

        HCBlocks.init();
        Ores.init();
        TileEntities.init();
        Fluids.init();

        HCItems.init();
        EventHelper.init();

        ThirdPartyManager.instance().preInit();

        MultipartHandler.init();
        TickHandler.init();
    }

    /*!
     * @author Koen Beckers
	 * @date 13-12-2013
	 * Loading the mod!
	 */
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {

        ThirdPartyManager.instance().init();

        GameRegistry.registerWorldGenerator(new HCWorldGenerator(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        NetworkHandler.init();

        //  UPDATE CHECKER DISABLED FOR NOW (2nd June 2015)
        // UpdateChecker.runCheck();

        //ForgeChunkManager.setForcedChunkLoadingCallback(Hydraulicraft
        //  .instance, null);

        proxy.init();
    }

    /*!
     * @author Koen Beckers
	 * @date 13-12-2013
	 * After the mod has been loaded.
	 */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        HydraulicRecipes.init();
        EventHelper.postinit();

        ThirdPartyManager.instance().postInit();
        configHandler.loadTank();
        new IGWSupportNotifier();

        Log.info("Hydraulicraft ready for use!");
    }


    @Mod.EventHandler
    public void processIMCRequests(FMLInterModComms.IMCEvent event) {

        List<FMLInterModComms.IMCMessage> messages = event.getMessages();
        for (FMLInterModComms.IMCMessage message : messages) {
            try {
                if (message.key.equals("registerCrushingRecipe")) {
                    NBTTagCompound toRegister = message.getNBTValue();

                    ItemStack from = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemFrom"));
                    ItemStack to = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemTo"));
                    float pressureRatio = toRegister.getFloat("pressureRatio");
                    if (from != null && to != null) {
                        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(to, from).setPressure(pressureRatio));
                    } else {
                        Log.error("Cannot add crushing recipe from " + message.getSender() + ". One of the item stacks is null");
                    }
                } else if (message.key.equals("registerWashingRecipe")) {
                    NBTTagCompound toRegister = message.getNBTValue();

                    ItemStack from = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemFrom"));
                    ItemStack to = ItemStack.loadItemStackFromNBT
                            (toRegister.getCompoundTag("itemTo"));
                    float pressureRatio = toRegister.getFloat("pressureRatio");
                    if (from != null && to != null) {
                        HydraulicRecipes.INSTANCE.addWasherRecipe(new FluidShapelessOreRecipe(to, from).setPressure(pressureRatio));
                    } else {
                        Log.error("Cannot add washing recipe from " + message.getSender() + ". One of the item stacks is null");
                    }
                } else {
                    Class clazz = Class.forName(message.key);
                    try {
                        Method method = clazz.getMethod(message.getStringValue(), ITrolleyRegistrar.class);
                        try {
                            method.invoke(null, Hydraulicraft.trolleyRegistrar);
                            Log.info("Successfully gave " + message.getSender() + " a nudge! Happy to be doing business!");
                        } catch (IllegalAccessException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be accessed: " + message.getStringValue());
                        } catch (IllegalArgumentException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method has no single IHydraulicraftRegistrar argument or it isn't static: " + message.getStringValue());
                        } catch (InvocationTargetException e) {
                            Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method threw an exception: " + message.getStringValue());
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be found: " + message.getStringValue());
                    } catch (SecurityException e) {
                        Log.error(message.getSender() + " tried to register to HydCraft. Failed because the method can NOT be accessed: " + message.getStringValue());
                    }
                }
            } catch (ClassNotFoundException e) {
                Log.error(message.getSender() + " tried to register to HydCraft. Failed because the class can NOT be found: " + message.key);
            }

        }
    }


    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        ipList.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
        tankList.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

        ipList.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
        tankList.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
    }


    @Mod.EventHandler
    public void convert(FMLMissingMappingsEvent event) {
/*
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
            String name = mapping.name;

            if (name.startsWith("HydCraft:wheatHarvester")) {
                name = name.replaceAll("wheatHarvester", "harvesterTrolley");

                if (mapping.type == GameRegistry.Type.BLOCK) {
                    mapping.remap(GameData.getBlockRegistry().getObject(name));
                } else {
                    mapping.remap(GameData.getItemRegistry().getObject(name));
                }
            }
            if (name.startsWith("HydCraft:hydraulicMixer")) {
                name = name.replaceAll("hydraulicMixer", Names.blockHydraulicFilter.unlocalized);

                if (mapping.type == GameRegistry.Type.BLOCK) {
                    mapping.remap(GameData.getBlockRegistry().getObject(name));
                } else {
                    mapping.remap(GameData.getItemRegistry().getObject(name));
                }
            }
            if (name.startsWith("HydCraft:hydraulicWaterPump")) {
                name = name.replaceAll("hydraulicWaterPump", Names.blockHydraulicFluidPump.unlocalized);

                if (mapping.type == GameRegistry.Type.BLOCK) {
                    mapping.remap(GameData.getBlockRegistry().getObject(name));
                } else {
                    mapping.remap(GameData.getItemRegistry().getObject(name));
                }
            }
        }
*/
    }
}


