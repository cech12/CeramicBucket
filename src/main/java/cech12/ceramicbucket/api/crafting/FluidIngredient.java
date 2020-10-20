package cech12.ceramicbucket.api.crafting;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * This ingredient class enables to define fluids as ingredient.
 * Example JSON object: { "type": "ceramicbucket:fluid", "tag": "minecraft:lava", "amount": 1000, "exact":true }
 * The tag is a fluid tag of the fluid that a fluid container should contain to be a fitting ingredient.
 * If "exact" is true, the fluid container must contain exactly the defined amount.
 * If "exact" is not set or false, the fluid container must contain at least the defined amount.
 *
 * Hint: The fluid container is fully emptied by the Minecraft crafting operation and the item stack of its
 * {@link net.minecraftforge.common.extensions.IForgeItem#getContainerItem(ItemStack)} implementation is left.
 */
public class FluidIngredient extends Ingredient {

    protected final Tag<Fluid> fluidTag;
    protected final int amount;
    protected final boolean exact;
    private ItemStack[] matchingStacks;

    public FluidIngredient(Tag<Fluid> fluidTag, int amount, boolean exact) {
        super(Stream.of());
        this.fluidTag = fluidTag;
        this.amount = amount;
        this.exact = exact;
    }

    public FluidIngredient(ResourceLocation resourceLocation, int amount, boolean exact) {
        this(new FluidTags.Wrapper(resourceLocation), amount, exact);
    }

    @Override
    public boolean test(ItemStack itemStack) {
        AtomicBoolean result = new AtomicBoolean(false);
        if (itemStack != null) {
            FluidUtil.getFluidContained(itemStack).ifPresent(fluidStack ->
                    result.set(fluidStack.getFluid().isIn(fluidTag) && ((this.exact) ? fluidStack.getAmount() == this.amount : fluidStack.getAmount() >= this.amount))
            );
        }
        return result.get();
    }

    @Override
    @Nonnull
    public ItemStack[] getMatchingStacks() {
        if (this.matchingStacks == null) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            ArrayList<Fluid> addedFluids = new ArrayList<>();
            this.fluidTag.getAllElements().forEach(fluid -> {
                //only fluid buckets which are obtainable
                ItemStack bucket = FluidUtil.getFilledBucket(new FluidStack(fluid, amount));
                Fluid bucketFluid = FluidUtil.getFluidContained(bucket).orElse(FluidStack.EMPTY).getFluid();
                if (bucketFluid != Fluids.EMPTY && !addedFluids.contains(bucketFluid)) {
                    stacks.add(bucket);
                    stacks.add(CeramicBucketUtils.getFilledCeramicBucket(bucketFluid));
                    addedFluids.add(bucketFluid);
                }
            });
            this.matchingStacks = stacks.toArray(new ItemStack[0]);
        }
        return this.matchingStacks;
    }

    @Override
    public boolean hasNoMatchingItems() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    protected void invalidate() {
        this.matchingStacks = null;
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
        jsonObject.addProperty("tag", this.fluidTag.toString());
        jsonObject.addProperty("amount", this.amount);
        jsonObject.addProperty("exact", this.amount);
        return jsonObject;
    }

    public static final class Serializer implements IIngredientSerializer<FluidIngredient> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation NAME = new ResourceLocation(CeramicBucketMod.MOD_ID, "fluid");

        private Serializer() {}

        @Override
        public FluidIngredient parse(PacketBuffer buffer) {
            ResourceLocation tag = buffer.readResourceLocation();
            int amount = buffer.readInt();
            boolean exact = buffer.readBoolean();
            return new FluidIngredient(tag, amount, exact);
        }

        @Override
        public FluidIngredient parse(@Nonnull JsonObject json) {
            String tag = JSONUtils.getString(json, "tag");
            int amount = JSONUtils.getInt(json, "amount");
            boolean exact = false;
            if (JSONUtils.hasField(json, "exact")) {
                exact = JSONUtils.getBoolean(json, "exact");
            }
            return new FluidIngredient(new FluidTags.Wrapper(new ResourceLocation(tag)), amount, exact);
        }

        @Override
        public void write(PacketBuffer buffer, FluidIngredient ingredient) {
            buffer.writeString(ingredient.fluidTag.toString());
            buffer.writeInt(ingredient.amount);
            buffer.writeBoolean(ingredient.exact);
        }
    }
}
