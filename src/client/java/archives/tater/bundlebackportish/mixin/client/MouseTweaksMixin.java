package archives.tater.bundlebackportish.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yalter.mousetweaks.Main;

@Mixin(Main.class)
public class MouseTweaksMixin {
    @WrapOperation(
            method = "onMouseScrolled",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0)
    )
    private static boolean skipBundleScroll(ItemStack instance, Operation<Boolean> original) {
        return original.call(instance) || instance.getItem() instanceof BundleItem;
    }
}
