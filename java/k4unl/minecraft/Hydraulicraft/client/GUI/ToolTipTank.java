package k4unl.minecraft.Hydraulicraft.client.GUI;

import java.util.ArrayList;
import java.util.List;

public class ToolTipTank extends ToolTip {

    String title;
    String unit;
    float  value;
    float  max;

    public ToolTipTank(int _x, int _y, int _w, int _h, String _title,
                       String _unit, float _value, float _max) {

        super(_x, _y, _w, _h);
        title = _title;
        unit = _unit;
        value = _value;
        max = _max;
    }

    @Override
    public List<String> getText() {

        List<String> text = new ArrayList<String>();
        text.add(title);
        text.add((int) value + "/" + (int) max + " " + unit);
        return text;
    }
}
