package k4unl.minecraft.Hydraulicraft.api;

import java.lang.reflect.Constructor;

import codechicken.multipart.TMultiPart;
import k4unl.minecraft.Hydraulicraft.TileEntities.transport.TileHydraulicHose;
import net.minecraft.tileentity.TileEntity;

/**
 * 
 * @author minemaarten
 * Original idea and class from PneumaticCraft.
 * Modified by K-4U for Hydraulicraft.
 */
public class HydraulicBaseClassSupplier {
	private static Constructor baseHandlerConstructor;
	private static Constructor baseTransporterConstructor;
	private static Constructor baseGeneratorConstructor;
	private static Constructor baseStorageConstructor;
	
    public static IBaseClass getConsumerClass(TileEntity target){
    	IBaseClass baseClassEntity = null;
        try {
            if(baseHandlerConstructor == null) baseHandlerConstructor = Class.forName("k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer").getConstructor(TileEntity.class);
            baseClassEntity = (IBaseClass)baseHandlerConstructor.newInstance(target);
        } catch(Exception e) {
            System.err.println("[Hydraulicraft API] An error has occured whilst trying to get a Consumer class. Here's a stacktrace:");
            e.printStackTrace();
        }
        return baseClassEntity;
    }
    
    public static IBaseTransporter getTransporterClass(TileEntity target){
    	IBaseTransporter baseClassEntity = null;
        try {
            if(baseTransporterConstructor == null) baseTransporterConstructor = Class.forName("k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileTransporter").getConstructor(TileEntity.class);
            baseClassEntity = (IBaseTransporter)baseTransporterConstructor.newInstance(target);
        } catch(Exception e) {
            System.err.println("[Hydraulicraft API] An error has occured whilst trying to get a transporter class. Here's a stacktrace:");
            e.printStackTrace();
        }
        return baseClassEntity;
    }
    
    public static IBaseTransporter getTransporterClass(TMultiPart target){
    	IBaseTransporter baseClassEntity = null;
        try {
            if(baseTransporterConstructor == null) baseTransporterConstructor = Class.forName("k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileTransporter").getConstructor(TMultiPart.class);
            baseClassEntity = (IBaseTransporter)baseTransporterConstructor.newInstance(target);
        } catch(Exception e) {
            System.err.println("[Hydraulicraft API] An error has occured whilst trying to get a transporter class. Here's a stacktrace:");
            e.printStackTrace();
        }
        return baseClassEntity;
    }
    
    
    public static IBaseGenerator getGeneratorClass(TileEntity target){
    	IBaseGenerator baseClassEntity = null;
        try {
            if(baseGeneratorConstructor == null) baseGeneratorConstructor = Class.forName("k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileGenerator").getConstructor(TileEntity.class);
            baseClassEntity = (IBaseGenerator)baseGeneratorConstructor.newInstance(target);
        } catch(Exception e) {
            System.err.println("[Hydraulicraft API] An error has occured whilst trying to get a generator class. Here's a stacktrace:");
            e.printStackTrace();
        }
        return baseClassEntity;
    }

	public static IBaseStorage getStorageClass(TileEntity target) {
		IBaseStorage baseClassEntity = null;
        try {
            if(baseStorageConstructor == null) baseStorageConstructor = Class.forName("k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileStorage").getConstructor(TileEntity.class);
            baseClassEntity = (IBaseStorage)baseStorageConstructor.newInstance(target);
        } catch(Exception e) {
            System.err.println("[Hydraulicraft API] An error has occured whilst trying to get a storage class. Here's a stacktrace:");
            e.printStackTrace();
        }
        return baseClassEntity;
	}
}
