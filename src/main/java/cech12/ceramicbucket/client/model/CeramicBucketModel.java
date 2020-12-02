package cech12.ceramicbucket.client.model;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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

    public CeramicBucketModel(@Nonnull Fluid fluid, boolean isCracked)
    {
        this.fluid = fluid;
        this.isCracked = isCracked;
    }

    /**
     * Returns a new ModelDynBucket representing the given fluid, but with the same
     * other properties (flipGas, tint, coverIsMask).
     */
    public CeramicBucketModel withFluid(Fluid newFluid)
    {
        return new CeramicBucketModel(newFluid, CeramicBucketUtils.isFluidTooHotForCeramicBucket(newFluid));
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format, ItemOverrideList overrides)
    {
        return bakeInternal(owner, bakery, spriteGetter, sprite, format, owner.getCombinedState());
    }

    protected IBakedModel bakeInternal(IModelConfiguration owner, ModelBakery bakery, Function<ResourceLocation, TextureAtlasSprite> spriteGetter, ISprite sprite, VertexFormat format, @Nullable IModelState transformsFromModel)
    {
        ResourceLocation particleLocation = owner.isTexturePresent("particle") ? new ResourceLocation(owner.resolveTexture("particle")) : null;
        ResourceLocation baseLocation = owner.isTexturePresent("base") ? new ResourceLocation(owner.resolveTexture("base")) : null;
        if (this.isCracked && owner.isTexturePresent("crackedBase")) {
            baseLocation = new ResourceLocation(owner.resolveTexture("crackedBase"));
        }
        ResourceLocation fluidMaskLocation = owner.isTexturePresent("fluid") ? new ResourceLocation(owner.resolveTexture("fluid")) : null;

        TextureAtlasSprite fluidSprite = fluid != Fluids.EMPTY ? spriteGetter.apply(fluid.getAttributes().getStillTexture()) : null;

        IModelState state = sprite.getState();

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;

        if (particleSprite == null) particleSprite = fluidSprite;

        // if the fluid is lighter than air, will manipulate the initial state to be rotated 180deg to turn it upside down
        if (fluid != Fluids.EMPTY && fluid.getAttributes().isLighterThanAir())
        {
            sprite = new ModelStateComposition(state, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(null, new Quat4f(0, 0, 1, 0), null, null)));
            state = sprite.getState();
        }

        TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = transformsFromModel != null ?
                PerspectiveMapWrapper.getTransforms(new ModelStateComposition(transformsFromModel, state)) :
                PerspectiveMapWrapper.getTransforms(state);

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        if (baseLocation != null)
        {
            TextureAtlasSprite baseSprite = spriteGetter.apply(baseLocation);
            if (baseSprite != null) {
                builder.addAll(ItemLayerModel.getQuadsForSprite(1, baseSprite, format, state.apply(Optional.empty())));
            }
        }

        if (fluidMaskLocation != null && fluidSprite != null)
        {
            TextureAtlasSprite templateSprite = spriteGetter.apply(fluidMaskLocation);
            if (templateSprite != null)
            {
                // build liquid layer (inside)
                //int luminosity = fluid.getAttributes().getLuminosity();
                int color = fluid.getAttributes().getColor();
                builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, templateSprite, fluidSprite, NORTH_Z_FLUID, Direction.NORTH, color, 1));
                builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, templateSprite, fluidSprite, SOUTH_Z_FLUID, Direction.SOUTH, color, 1));
            }
        }

        return new BakedModel(bakery, owner, this, builder.build(), particleSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap(), transform.isIdentity());
    }

    @Override
    public Collection<ResourceLocation> getTextureDependencies(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<String> missingTextureErrors)
    {
        Set<ResourceLocation> texs = Sets.newHashSet();

        if (owner.isTexturePresent("particle")) texs.add(new ResourceLocation(owner.resolveTexture("particle")));
        if (owner.isTexturePresent("base")) texs.add(new ResourceLocation(owner.resolveTexture("base")));
        if (owner.isTexturePresent("crackedBase")) texs.add(new ResourceLocation(owner.resolveTexture("crackedBase")));
        if (owner.isTexturePresent("fluid")) texs.add(new ResourceLocation(owner.resolveTexture("fluid")));

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
            return new CeramicBucketModel(Fluids.EMPTY, false);
        }
    }

    private static final class ContainedFluidOverrideHandler extends ItemOverrideList
    {
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

                        //reset cache if temperature config changed
                        boolean isCracked = CeramicBucketUtils.isFluidTooHotForCeramicBucket(fluid);
                        if (model.isCracked != isCracked) {
                            model.isCracked = isCracked;
                            model.cache.clear();
                        }

                        if (!model.cache.containsKey(name)) {
                            CeramicBucketModel parent = model.parent.withFluid(fluid);
                            IBakedModel bakedModel = parent.bakeInternal(model.owner, bakery, ModelLoader.defaultTextureGetter(), new SimpleModelState(model.transforms), model.format, null);
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
        protected final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final VertexFormat format;

        private boolean isCracked;

        BakedModel(ModelBakery bakery,
                   IModelConfiguration owner, CeramicBucketModel parent,
                   ImmutableList<BakedQuad> quads,
                   TextureAtlasSprite particle,
                   VertexFormat format,
                   ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
                   Map<String, IBakedModel> cache,
                   boolean untransformed)
        {
            super(quads, particle, transforms, new ContainedFluidOverrideHandler(bakery), untransformed);
            this.owner = owner;
            this.parent = parent;
            this.cache = cache;
            this.transforms = transforms;
            this.format = format;
        }
    }
}
