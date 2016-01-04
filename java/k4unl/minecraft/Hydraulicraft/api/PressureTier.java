package k4unl.minecraft.Hydraulicraft.api;


import net.minecraft.util.IStringSerializable;

public enum PressureTier implements IStringSerializable {
    LOWPRESSURE(0), MEDIUMPRESSURE(1), HIGHPRESSURE(2), INVALID(3);

    private final int tier;

    public static final PressureTier[] VALID_TIERS = {LOWPRESSURE, MEDIUMPRESSURE, HIGHPRESSURE};

    private PressureTier(int _tier) {
        this.tier = _tier;
    }

    @Override
    public String toString() {
        return "TIER-" + tier;
    }

    public static PressureTier fromOrdinal(int tier) {
        if (tier <= 2 && tier >= 0) {
            return VALID_TIERS[tier];
        } else {
            return INVALID;
        }
    }

    @Override
    public String getName() {
        return toString();
    }

    public int toInt() {
        return this.tier;
    }
}
