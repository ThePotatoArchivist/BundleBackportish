package archives.tater.bundlebackportish;

import archives.tater.bundlebackportish.mixin.client.HandledScreenInvoker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * Based on <a href="https://github.com/zacharybarbanell/bundle-scroll">Bundle Tweaks</a> by
 * <a href="https://github.com/zacharybarbanell">zacharybarnabell</a> (MIT License)
 */
public class BundleBackportishClientNetworking {
    private static double accScroll = 0;

    public static void register() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HandledScreen<?> handledScreen) {
                ScreenMouseEvents.afterMouseScroll(screen).register((_screen, x, y, horiz, vert) ->
                        BundleBackportishClientNetworking.onMouseScrolled(handledScreen, x, y, vert));
            }
        });
    }

    private static boolean onMouseScrolled(HandledScreen<?> screen, double x, double y, double scroll) {
        Slot slot = ((HandledScreenInvoker) screen).invokeGetSlotAt(x, y);
        if (slot == null) {
            return true;
        }
        ItemStack stack = slot.getStack();
        if (!(stack.getItem() instanceof BundleItem) || stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).isEmpty()) {
            return true;
        }
        if (accScroll * scroll < 0) {
            accScroll = 0;
        }
        accScroll += scroll;
        int amt = (int) accScroll;
        accScroll -= amt;
        if (amt == 0) {
            return true;
        }

        BundleSelection.add(stack, -amt);

        ClientPlayNetworking.send(new BundleScrollPayload(
                screen.getScreenHandler().syncId,
                screen.getScreenHandler().getRevision(),
                slot.id,
                -amt
        ));

        return false;
    }
}
