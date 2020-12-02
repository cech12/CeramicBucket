package cech12.ceramicbucket.client.model;

import cech12.ceramicbucket.item.CeramicEntityBucketItem;
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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class CeramicEntityBucketModel implements IModelGeometry<CeramicEntityBucketModel> {

    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_MAP = Maps.newHashMap();

    private final EntityType<?> entityType;

    private final boolean isCracked;

    public CeramicEntityBucketModel(@Nullable EntityType<?> entityType, boolean isCracked)
    {
        this.entityType = entityType;
        this.isCracked = isCracked;
    }

    public static ResourceLocation getEntityTexture(ResourceLocation entityLocation) {
        ResourceLocation texture = TEXTURE_MAP.get(entityLocation);
        if (texture == null) {
            String textureLocation = String.format("ceramicbucket:item/bucket_content/%s/%s", entityLocation.getNamespace(), entityLocation.getPath());
            texture = new ResourceLocation(textureLocation);
            TEXTURE_MAP.put(entityLocation, texture);
        }
        return texture;
    }

    public CeramicEntityBucketModel withEntityType(@Nonnull EntityType<?> entityType, boolean isCracked)
    {
        return new CeramicEntityBucketModel(entityType, isCracked);
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
        ResourceLocation entityLocation = null;
        if (this.entityType != null) {
            entityLocation = getEntityTexture(ForgeRegistries.ENTITIES.getKey(this.entityType));
        }
        TextureAtlasSprite entitySprite = entityLocation != null ? spriteGetter.apply(entityLocation) : null;

        IModelState state = sprite.getState();

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;
        if (particleSprite == null) particleSprite = entitySprite;

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

        if (entitySprite != null) {
            builder.addAll(ItemLayerModel.getQuadsForSprite(1, entitySprite, format, state.apply(Optional.empty())));
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

        return texs;
    }

    public enum Loader implements IModelLoader<CeramicEntityBucketModel>
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
        public CeramicEntityBucketModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
        {
            // create new model
            return new CeramicEntityBucketModel(null, false);
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
            if (stack.getItem() instanceof CeramicEntityBucketItem) {
                BakedModel model = (BakedModel)originalModel;
                CeramicEntityBucketItem bucket = (CeramicEntityBucketItem) stack.getItem();
                EntityType<?> containedEntityType = bucket.getEntityTypeFromStack(stack);
                if (containedEntityType != null) {
                    ResourceLocation typeName = ForgeRegistries.ENTITIES.getKey(containedEntityType);
                    if (typeName != null) {
                        String name = typeName.toString();
                        //reset cache if temperature config changed
                        boolean cracksBucket = bucket.isCrackedBucket(stack);
                        if (model.cracksBucket != cracksBucket) {
                            model.cracksBucket = cracksBucket;
                            model.cache.clear();
                        }
                        if (!model.cache.containsKey(name)) {
                            CeramicEntityBucketModel unbaked = model.parent.withEntityType(containedEntityType, cracksBucket);
                            IBakedModel bakedModel = unbaked.bakeInternal(model.owner, bakery, ModelLoader.defaultTextureGetter(), new SimpleModelState(model.transforms), model.format, null);
                            model.cache.put(name, bakedModel);
                            return bakedModel;
                        }
                        return model.cache.get(name);
                    }
                }
            }
            return originalModel;
        }
    }

    // the dynamic bucket is based on the empty bucket
    private static final class BakedModel extends BakedItemModel
    {
        private final IModelConfiguration owner;
        private final CeramicEntityBucketModel parent;
        protected final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final VertexFormat format;

        private boolean cracksBucket;

        BakedModel(ModelBakery bakery,
                   IModelConfiguration owner, CeramicEntityBucketModel parent,
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
