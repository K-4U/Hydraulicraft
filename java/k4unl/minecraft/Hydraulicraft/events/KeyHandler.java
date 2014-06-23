package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Key;
import k4unl.minecraft.Hydraulicraft.network.PacketPipeline;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketKeyPressed;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {
	/** Key index for easy handling */
	private static final Key[] keysToRegister = { 
		new Key("key.mining_helmet.desc", Keyboard.KEY_L)
	};
	
	private static KeyBinding[] keys;

	
	public static void init(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		
		keys = new KeyBinding[keysToRegister.length];
		for (int i = 0; i < keysToRegister.length; ++i) {
			keys[i] = new KeyBinding(keysToRegister[i].desc, keysToRegister[i].keyValue, "key." + ModInfo.LID + ".category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		// FMLClientHandler.instance().getClient().inGameHasFocus
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			if (keys[Constants.KEYS_MINING_HELMET].isPressed()) {
				PacketPipeline.instance.sendToServer(new PacketKeyPressed(Constants.KEYS_MINING_HELMET));
			}
		}
	}
}
