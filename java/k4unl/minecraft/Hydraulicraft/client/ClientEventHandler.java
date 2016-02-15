package k4unl.minecraft.Hydraulicraft.client;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {

    public static final ResourceLocation pressureGauge = new ResourceLocation(ModInfo.LID, "textures/gui/pressureGauge.png");

    public static void init() {

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }


    @SubscribeEvent
    public void fogDensityEvent(EntityViewRenderEvent.FogDensity event) {

        if (ItemDivingSuit.isWearingFullSuit(Minecraft.getMinecraft().thePlayer)) {
            event.density = 0.1F;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void fogColors(EntityViewRenderEvent.FogColors event) {

        if (ItemDivingSuit.isWearingFullSuit(Minecraft.getMinecraft().thePlayer)) {
            //223 31 53
            event.red = 223F / 255F;
            event.green = 31F / 255F;
            event.blue = 53F / 255F;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if ((event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.type != RenderGameOverlayEvent.ElementType.JUMPBAR) ||
                event.isCancelable()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer.isInWater() && HCConfig.INSTANCE.getBool("waterPressureKills") && Hydraulicraft.hasPressureGaugeInInventory) {
            GL11.glPushMatrix();

            //GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);

            GL11.glEnable(GL11.GL_ALPHA_TEST);

            int x = HCConfig.INSTANCE.getInt("x", "pressureUI");
            int y = HCConfig.INSTANCE.getInt("y", "pressureUI");
            int w = 50;
            int h = 16;

            float zLevel = 0F;

            WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();


            //Log.info("Pressure:" + mc.thePlayer.getEntityData().getInteger("pressure"));

            GL11.glScaled(HCConfig.INSTANCE.getDouble("scale", "pressureUI"), HCConfig.INSTANCE.getDouble("scale", "pressureUI"), HCConfig.INSTANCE.getDouble("scale", "pressureUI"));
            mc.getTextureManager().bindTexture(pressureGauge);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos((double) (x + 0), (double) (y + h), (double) zLevel).tex(0.0, 1.0).endVertex();
            worldRenderer.pos((double) (x + w), (double) (y + h), (double) zLevel).tex(1.0, 1.0).endVertex();
            worldRenderer.pos((double) (x + w), (double) (y + 0), (double) zLevel).tex(1.0, 0.0).endVertex();
            worldRenderer.pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex(0.0, 0.0).endVertex();
            Tessellator.getInstance().draw();

            zLevel += 2F;
            mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + "" + Hydraulicraft.pressure + "", x + 5, y + 5, (int) zLevel);
            mc.fontRendererObj.drawString(EnumChatFormatting.WHITE + " Bar", x + mc.fontRendererObj.getStringWidth("999") + 5, y + 5, (int) zLevel);

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);

            //GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void modelBakeEvent(ModelBakeEvent event) {

    }
}
