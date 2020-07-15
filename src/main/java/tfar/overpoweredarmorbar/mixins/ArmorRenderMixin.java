package tfar.overpoweredarmorbar.mixins;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.overpoweredarmorbar.OverloadedArmorBar;
import tfar.overpoweredarmorbar.RenderGameOverlayEvent;

@Mixin(InGameHud.class)
public class ArmorRenderMixin {
    
    @Inject(method="renderStatusBars", at=@At(value = "INVOKE_STRING", target="net.minecraft.util.profiler.Profiler.push(Ljava/lang/String;)V", args = { "ldc=armor" }))
    public void startSuppressingTextureDraw(MatrixStack stack, CallbackInfo ci) {
        OverloadedArmorBar.drawTextureSuppressed = true;
    }

    @Inject(method="renderStatusBars", at=@At(value = "INVOKE_STRING", target="net.minecraft.util.profiler.Profiler.swap(Ljava/lang/String;)V", args = { "ldc=health" }))
    public void stopSuppressingTextureDraw(MatrixStack stack, CallbackInfo ci) {
        OverloadedArmorBar.drawTextureSuppressed = false;
        OverloadedArmorBar.oeHandler.onRenderGameOverlayEventPre(new RenderGameOverlayEvent(stack));
    }
}
