package tfar.overpoweredarmorbar;

// This is a fake class that looks like the corresponding Forge class,

// to keep the changes to OverlayEventHandler as small as possible.

import com.mojang.blaze3d.vertex.PoseStack;

public class RenderGameOverlayEvent {
    
    public enum ElementType { ARMOR}
    private final PoseStack stack;

    public RenderGameOverlayEvent(PoseStack stack) {
        this.stack = stack;
    }
    public void setCanceled(boolean b) { }

    public PoseStack getMatrixStack() { return stack; }
    public ElementType getType() { return ElementType.ARMOR; }
}
