package k4unl.minecraft.Hydraulicraft.client.GUI;

import java.util.ArrayList;
import java.util.List;

public class ToolTip {
    List<String> text;
    int          x, y, w, h;

    public ToolTip(int x, int y, int width, int height) {
        this.text = new ArrayList<String>();
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }

    public ToolTip(int x, int y, int width, int height, String text) {
        this(x, y, width, height);
        this.text.add(text);
    }

    public ToolTip(int x, int y, int width, int height, String[] text) {
        this(x, y, width, height);
        for (String t : text)
            this.text.add(t);
    }

    public List<String> getText() {
        return text;
    }
}

