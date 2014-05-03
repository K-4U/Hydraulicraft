package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
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
	public static final int MINING_HELMET = 0;
	/** Key descriptions; use a language file to localize the description later */
	private static final String[] desc = {"key.mining_helmet.desc"};
	/** Default key values */
	private static final int[] keyValues = {Keyboard.KEY_L};
	private static KeyBinding[] keys;

	
	public static void init(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		
		keys = new KeyBinding[desc.length];
		for (int i = 0; i < desc.length; ++i) {
			keys[i] = new KeyBinding(desc[i], keyValues[i], "key." + ModInfo.LID + ".category");
			ClientRegistry.registerKeyBinding(keys[i]);
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		// FMLClientHandler.instance().getClient().inGameHasFocus
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			if (keys[MINING_HELMET].isPressed()) {
				Log.info("Key pressed!");
				if(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3) != null){
					if(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getItem() instanceof ItemMiningHelmet){
						ItemMiningHelmet.togglePower(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3));
						Functions.showMessageInChat(Minecraft.getMinecraft().thePlayer, "Helmet is now " + (ItemMiningHelmet.isPoweredOn(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3)) ? "on" : "off"));
					}
				}
				//TutorialMain.packetPipeline.sendToServer(new OpenGuiPacket(TutorialMain.GUI_CUSTOM_INV));
			}
		}
	}

	
}
