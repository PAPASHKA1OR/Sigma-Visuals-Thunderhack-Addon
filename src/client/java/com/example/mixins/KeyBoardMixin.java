//типа челу чит ыыыыыыы

//package gradient.client.client.mixin;
//
//import gradient.client.client.gui.ClickGui;
//import net.minecraft.client.Keyboard;
//import net.minecraft.client.MinecraftClient;
//import org.lwjgl.glfw.GLFW;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//
//@Mixin(Keyboard.class)
//public class KeyBoardMixin {
//    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
//    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
//        if (key == GLFW.GLFW_KEY_P) {
//            MinecraftClient.getInstance().setScreen(new ClickGui());
//        }
//    }
//}
