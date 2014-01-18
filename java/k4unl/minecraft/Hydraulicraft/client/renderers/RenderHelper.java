package k4unl.minecraft.Hydraulicraft.client.renderers;

import org.lwjgl.opengl.GL11;

public class RenderHelper {

	public static void vertexWithTexture(float x, float y, float z, float tL, float tT){
		GL11.glTexCoord2f(tL, tT);
		GL11.glVertex3f(x, y, z);
	}
}
