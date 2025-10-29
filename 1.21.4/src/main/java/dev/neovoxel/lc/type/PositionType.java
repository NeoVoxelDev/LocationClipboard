package dev.neovoxel.lc.type;

public enum PositionType {
    OFF("text.location-clipboard.position-type.off"),
    PLAYER("text.location-clipboard.position-type.player"),
    UNDERFOOT_BLOCK("text.location-clipboard.position-type.underfoot-block"),
    TARGETED_BLOCK("text.location-clipboard.position-type.targeted-block"),
    TARGETED_BLOCK_WITHOUT_FLUID("text.location-clipboard.position-type.targeted-block-without-fluid");

    private final String key;

    PositionType(String key) {
        this.key = key;
    }
}
