package tfar.overpoweredarmorbar;

// This is a fake class that looks like the corresponding Forge class,

import net.minecraft.client.util.math.MatrixStack;

// to keep the changes to OverlayEventHandler as small as possible.

public class RenderGameOverlayEvent {
    
    public enum ElementType { ARMOR}
    private final MatrixStack stack;

    public RenderGameOverlayEvent(MatrixStack stack) {
        this.stack = stack;
    }
    public void setCanceled(boolean b) { }

    public MatrixStack getMatrixStack() { return stack; }
    public ElementType getType() { return ElementType.ARMOR; }
}
