package pet.minecraft.Hydraulicraft;

import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
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
	
	
	
	
	
}
