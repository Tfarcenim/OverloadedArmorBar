package tfar.overpoweredarmorbar.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.overpoweredarmorbar.OverloadedArmorBar;
import tfar.overpoweredarmorbar.overlay.OverlayEventHandler;

@Mixin(Gui.class)
public class ArmorRenderMixin {
    
    @Inject(method="renderPlayerHealth", at=@At(value = "INVOKE_STRING", target="Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", args = { "ldc=armor" }))
    public void startSuppressingTextureDraw(PoseStack stack, CallbackInfo ci) {
        OverloadedArmorBar.drawTextureSuppressed = true;
    }

    @Inject(method="renderPlayerHealth", at=@At(value = "INVOKE_STRING", target="Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args = { "ldc=health" }))
    public void stopSuppressingTextureDraw(PoseStack stack, CallbackInfo ci) {
        OverloadedArmorBar.drawTextureSuppressed = false;
        OverlayEventHandler.onRenderGameOverlayEventPre(stack);
    }
}
