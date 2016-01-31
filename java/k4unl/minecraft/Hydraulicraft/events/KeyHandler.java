package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Key;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketKeyPressed;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
	/** Key index for easy handling */
	private static final Key[] keysToRegister = { 
		new Key("key.mining_helmet.desc", Keyboard.KEY_L)
	};
	
	private static KeyBinding[] keys;

	
	public static void init(){
		MinecraftForge.EVENT_BUS.register(new KeyHandler());
		
		keys = new KeyBinding[keysToRegister.length];
		for (int i = 0; i < keysToRegister.length; ++i) {
			keys[i] = new KeyBinding(keysToRegister[i].desc, keysToRegister[i].keyValue, "key." + ModInfo.LID + ".category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		// FMLClientHandler.instance().getClient().inGameHasFocus
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			if (keys[Constants.KEYS_MINING_HELMET].isPressed()) {
				NetworkHandler.INSTANCE.sendToServer(new PacketKeyPressed(Constants.KEYS_MINING_HELMET));
			}
		}
	}
}
