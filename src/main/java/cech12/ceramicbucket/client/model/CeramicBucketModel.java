package cech12.ceramicbucket.client.model;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This implementation is based on net.minecraftforge.client.model.DynamicBucketModel.
 * Multiple changes were done to simplify the class
 */
public class CeramicBucketModel implements IModelGeometry<CeramicBucketModel> {

    // minimal Z offset to prevent depth-fighting
    private static final float NORTH_Z_FLUID = 7.498f / 16f;
    private static final float SOUTH_Z_FLUID = 8.502f / 16f;

    @Nonnull
    private final Fluid fluid;

    private final boolean isCracked;

    public CeramicBucketModel(@Nonnull Fluid fluid)
    {
        this.fluid = fluid;
        this.isCracked = CeramicBucketUtils.isFluidTooHotForCeramicBucket(fluid);
    }

    /**
     * Returns a new ModelDynBucket representing the given fluid, but with the same
     * other properties (flipGas, tint, coverIsMask).
     */
    public CeramicBucketModel withFluid(Fluid newFluid)
    {
        return new CeramicBucketModel(newFluid);
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        Material particleLocation = owner.isTexturePresent("particle") ? owner.resolveTexture("particle") : null;
        Material baseLocation = owner.isTexturePresent("base") ? owner.resolveTexture("base") : null;
        if (this.isCracked && owner.isTexturePresent("crackedBase")) {
            baseLocation = owner.resolveTexture("crackedBase");
        }
        Material fluidMaskLocation = owner.isTexturePresent("fluid") ? owner.resolveTexture("fluid") : null;

        IModelTransform transformsFromModel = owner.getCombinedTransform();

        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? spriteGetter.apply(ForgeHooksClient.getBlockMaterial(fluid.getAttributes().getStillTexture())) : null;

        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap =
                PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;

        if (particleSprite == null) particleSprite = fluidSprite;

        // if the fluid is lighter than air, will manipulate the initial state to be rotated 180deg to turn it upside down
        if (fluid != Fluids.EMPTY && fluid.getAttributes().isLighterThanAir())
        {
            modelTransform = new SimpleModelTransform(
                    modelTransform.getRotation().blockCornerToCenter().composeVanilla(
                            new TransformationMatrix(null, new Quaternion(0, 0, 1, 0), null, null)).blockCenterToCorner());
        }

        TransformationMatrix transform = modelTransform.getRotation();

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        if (baseLocation != null)
        {
            // build base (insidest)
            builder.addAll(ItemLayerModel.getQuadsForSprites(ImmutableList.of(baseLocation), transform, spriteGetter));
        }

        if (fluidMaskLocation != null && fluidSprite != null)
        {
            TextureAtlasSprite templateSprite = spriteGetter.apply(fluidMaskLocation);
            if (templateSprite != null)
            {
                // build liquid layer (inside)
                //int luminosity = fluid.getAttributes().getLuminosity();
                int color = fluid.getAttributes().getColor();
                builder.addAll(ItemTextureQuadConverter.convertTexture(transform, templateSprite, fluidSprite, NORTH_Z_FLUID, Direction.NORTH, color, 1));
                builder.addAll(ItemTextureQuadConverter.convertTexture(transform, templateSprite, fluidSprite, SOUTH_Z_FLUID, Direction.SOUTH, color, 1));
            }
        }

        return new BakedModel(bakery, owner, this, builder.build(), particleSprite, Maps.immutableEnumMap(transformMap), Maps.newHashMap(), transform.isIdentity(), modelTransform, owner.isSideLit());
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        Set<Material> texs = Sets.newHashSet();

        if (owner.isTexturePresent("particle")) texs.add(owner.resolveTexture("particle"));
        if (owner.isTexturePresent("base")) texs.add(owner.resolveTexture("base"));
        if (owner.isTexturePresent("crackedBase")) texs.add(owner.resolveTexture("crackedBase"));
        if (owner.isTexturePresent("fluid")) texs.add(owner.resolveTexture("fluid"));

        return texs;
    }

    public enum Loader implements IModelLoader<CeramicBucketModel>
    {
        INSTANCE;

        @Override
        public IResourceType getResourceType()
        {
            return VanillaResourceType.MODELS;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
        {
            // no need to clear cache since we create a new model instance
        }

        @Override
        public CeramicBucketModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
        {
            // create new model
            return new CeramicBucketModel(Fluids.EMPTY);
        }
    }

    private static final class ContainedFluidOverrideHandler extends ItemOverrideList
    {
        private static final ResourceLocation REBAKE_LOCATION = new ResourceLocation("ceramicbucket:bucket_override");
        private final ModelBakery bakery;

        private ContainedFluidOverrideHandler(ModelBakery bakery)
        {
            this.bakery = bakery;
        }

        @Override
        public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity)
        {
            return FluidUtil.getFluidContained(stack)
                    .map(fluidStack -> {
                        CeramicBucketModel.BakedModel model = (BakedModel)originalModel;

                        Fluid fluid = fluidStack.getFluid();
                        String name = fluid.getRegistryName().toString();

                        if (!model.cache.containsKey(name))
                        {
                            CeramicBucketModel parent = model.parent.withFluid(fluid);
                            IBakedModel bakedModel = parent.bake(model.owner, bakery, ModelLoader.defaultTextureGetter(), model.originalTransform, model.getOverrides(), REBAKE_LOCATION);
                            model.cache.put(name, bakedModel);
                            return bakedModel;
                        }

                        return model.cache.get(name);
                    })
                    // not a fluid item apparently
                    .orElse(originalModel); // empty bucket
        }
    }

    // the dynamic bucket is based on the empty bucket
    private static final class BakedModel extends BakedItemModel
    {
        private final IModelConfiguration owner;
        private final CeramicBucketModel parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final IModelTransform originalTransform;

        BakedModel(ModelBakery bakery,
                   IModelConfiguration owner, CeramicBucketModel parent,
                   ImmutableList<BakedQuad> quads,
                   TextureAtlasSprite particle,
                   ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transforms,
                   Map<String, IBakedModel> cache,
                   boolean untransformed,
                   IModelTransform originalTransform, boolean isSideLit)
        {
            super(quads, particle, transforms, new ContainedFluidOverrideHandler(bakery), untransformed, isSideLit);
            this.owner = owner;
            this.parent = parent;
            this.cache = cache;
            this.originalTransform = originalTransform;
        }
    }
}
