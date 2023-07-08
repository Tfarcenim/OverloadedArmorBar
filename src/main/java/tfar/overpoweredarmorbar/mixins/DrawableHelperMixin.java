/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfar.overpoweredarmorbar.mixins;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.overpoweredarmorbar.OverloadedArmorBar;

/**
 *
 * @author gbl
 */
@Mixin(GuiGraphics.class)
public class DrawableHelperMixin {
    @Inject(method="innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFFFFFF)V", at=@At("HEAD"), cancellable = true)
    private void checkDrawSuppressed(ResourceLocation resourceLocation, int i, int j, int k, int l, int m, float f, float g, float h, float n, float o, float p, float q, float r, CallbackInfo ci) {
        if (OverloadedArmorBar.drawTextureSuppressed) {
            ci.cancel();
        }
    }
}
