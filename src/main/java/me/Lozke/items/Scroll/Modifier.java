package me.Lozke.items.Scroll;

public enum Modifier {
    DMG("DMG"),
    HP("HP"),
    HP_REGEN("HP/s"),
    ENERGY("Energy"),
    ALL_STAT("All Stats");

    public static Modifier[] types = Modifier.values();

    private String format;

    Modifier(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
