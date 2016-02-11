package k4unl.minecraft.Hydraulicraft.tileEntities.harvester;

/**
 * @author Koen Beckers (K-4U)
 */
public enum HarvesterReasonForNotForming {
    TROLLEY_EXPECTED, FRAME_ROTATION_WRONG, FRAME_EXPECTED, END_BLOCK_EXPECTED, TOO_SHORT, TOO_LONG, VERTICAL_FRAME_EXPECTED,
    TOO_WIDE, PISTON_EXPECTED, OTHER;

    private String translationKey;

    HarvesterReasonForNotForming() {

        String name = name();
        String text = "";
        boolean caps = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                caps = true;
            } else {
                text += Character.isLetter(c) ? (caps ? Character.toUpperCase(c) : Character.toLowerCase(c)) : c;
                caps = false;
            }
        }
        translationKey = text;
    }

    public String getTranslation() {

        return "lang.harvester.notFormingError." + translationKey;
    }
}
