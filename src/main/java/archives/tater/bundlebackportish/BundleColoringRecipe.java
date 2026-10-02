package archives.tater.bundlebackportish;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Predicate;

import static archives.tater.bundlebackportish.BundleBackportishItems.*;
import static java.util.Map.entry;

public class BundleColoringRecipe extends SpecialCraftingRecipe {

    public BundleColoringRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    private <T> @Nullable T find(Iterable<T> collection, Predicate<T> condition) {
        for (var item : collection) {
            if (condition.test(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasBundle = false;
        boolean hasDye = false;

        for (var stack : input.getStacks()) {
            if (stack.getItem() instanceof BundleItem) {
                if (hasBundle) return false;
                hasBundle = true;
                continue;
            }

            if (COLORS.containsKey(stack.getItem())) {
                if (hasDye) return false;
                hasDye = true;
                continue;
            }

            return false;
        }

        return hasBundle && hasDye;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        var bundle = find(input.getStacks(), stack -> stack.getItem() instanceof BundleItem);
        var dye = find(input.getStacks(), stack -> COLORS.containsKey(stack.getItem()));
        if (bundle == null || dye == null) return ItemStack.EMPTY; // This shouldn't happen but just in case
        var outputBundle = COLORS.get(dye.getItem());
        if (bundle.isOf(outputBundle)) return ItemStack.EMPTY;
        return bundle.copyComponentsToNewStack(outputBundle, 1);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return BundleBackportish.COLORING_RECIPE;
    }

    public static final Map<Item, Item> COLORS = Map.ofEntries(
            entry(Items.WHITE_DYE, WHITE_BUNDLE),
            entry(Items.ORANGE_DYE, ORANGE_BUNDLE),
            entry(Items.MAGENTA_DYE, MAGENTA_BUNDLE),
            entry(Items.LIGHT_BLUE_DYE, LIGHT_BLUE_BUNDLE),
            entry(Items.YELLOW_DYE, YELLOW_BUNDLE),
            entry(Items.LIME_DYE, LIME_BUNDLE),
            entry(Items.PINK_DYE, PINK_BUNDLE),
            entry(Items.GRAY_DYE, GRAY_BUNDLE),
            entry(Items.LIGHT_GRAY_DYE, LIGHT_GRAY_BUNDLE),
            entry(Items.CYAN_DYE, CYAN_BUNDLE),
            entry(Items.PURPLE_DYE, PURPLE_BUNDLE),
            entry(Items.BLUE_DYE, BLUE_BUNDLE),
            entry(Items.BROWN_DYE, BROWN_BUNDLE),
            entry(Items.GREEN_DYE, GREEN_BUNDLE),
            entry(Items.RED_DYE, RED_BUNDLE),
            entry(Items.BLACK_DYE, BLACK_BUNDLE)
    );

}
