package cech12.ceramicbucket.recipe;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * A Ceramic Bucket filled with a fluid can be used in recipes with this ingredient class.
 * Usage is the same as for a vanilla ingredient.
 * Example JSON object: { "type": "ceramicbucket:filled_ceramic_bucket", "tag": "forge:lava" }
 * The tag is a fluid tag of the fluid that the Ceramic Bucket should contain.
 */
public class FilledCeramicBucketIngredient extends Ingredient {

    protected final Tag<Fluid> fluidTag;

    public FilledCeramicBucketIngredient(Tag<Fluid> fluidTag) {
        super(Stream.of());
        this.fluidTag = fluidTag;
    }

    public FilledCeramicBucketIngredient(ResourceLocation resourceLocation) {
        this(new FluidTags.Wrapper(resourceLocation));
    }

    @Override
    public boolean test(ItemStack itemStack) {
        AtomicBoolean result = new AtomicBoolean(false);
        if (itemStack != null && itemStack.getItem() == CeramicBucketItems.FILLED_CERAMIC_BUCKET) {
            FluidUtil.getFluidContained(itemStack).ifPresent(fluidStack -> result.set(fluidStack.getFluid().isIn(fluidTag)));
        }
        return result.get();
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        FilledCeramicBucketItem bucketItem = (FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET;
        return fluidTag.getAllElements().stream()
                .map(bucketItem::getFilledInstance)
                .filter(this)
                .toArray(ItemStack[]::new);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public boolean hasNoMatchingItems() {
        return false;
    }

    @Override
    @Nonnull
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Nonnull
    public JsonElement serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", Serializer.NAME.toString());
        jsonObject.addProperty("tag", this.fluidTag.getId().toString());
        return jsonObject;
    }

    public static final class Serializer implements IIngredientSerializer<FilledCeramicBucketIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = new ResourceLocation(CeramicBucketMod.MOD_ID, "filled_ceramic_bucket");

        private Serializer() {}

        @Override
        public FilledCeramicBucketIngredient parse(PacketBuffer buffer) {
            ResourceLocation tag = buffer.readResourceLocation();
            return new FilledCeramicBucketIngredient(tag);
        }

        @Override
        public FilledCeramicBucketIngredient parse(@Nonnull JsonObject json) {
            String tag = JSONUtils.getString(json, "tag");
            return new FilledCeramicBucketIngredient(new FluidTags.Wrapper(new ResourceLocation(tag)));
        }

        @Override
        public void write(PacketBuffer buffer, FilledCeramicBucketIngredient ingredient) {
            buffer.writeString(ingredient.fluidTag.getId().toString());
        }
    }
}
