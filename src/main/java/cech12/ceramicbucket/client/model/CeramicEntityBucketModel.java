package cech12.ceramicbucket.client.model;

import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelTransformComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
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
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        Material particleLocation = owner.isTexturePresent("particle") ? owner.resolveTexture("particle") : null;
        Material baseLocation = owner.isTexturePresent("base") ? owner.resolveTexture("base") : null;
        if (this.isCracked && owner.isTexturePresent("crackedBase")) {
            baseLocation = owner.resolveTexture("crackedBase");
        }
        Material entityLocation = null;
        if (this.entityType != null) {
            entityLocation = new Material(PlayerContainer.LOCATION_BLOCKS_TEXTURE, getEntityTexture(ForgeRegistries.ENTITIES.getKey(this.entityType))); //AtlasTexture.LOCATION_BLOCKS_TEXTURE
        }
        TextureAtlasSprite entitySprite = entityLocation != null ? spriteGetter.apply(entityLocation) : null;

        IModelTransform transformsFromModel = owner.getCombinedTransform();


        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap =
                PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;
        if (particleSprite == null) particleSprite = entitySprite;


        TransformationMatrix transform = modelTransform.getRotation();

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        if (baseLocation != null)
        {
            builder.addAll(ItemLayerModel.getQuadsForSprites(ImmutableList.of(baseLocation), transform, spriteGetter));
        }

        if (entityLocation != null)
        {
            builder.addAll(ItemLayerModel.getQuadsForSprites(ImmutableList.of(entityLocation), transform, spriteGetter));
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
        private static final ResourceLocation REBAKE_LOCATION = new ResourceLocation("ceramicbucket:entity_bucket_override");

        private final Map<String, IBakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
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
                        int breakTemperature = ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.get();
                        if (model.breakTemperature != breakTemperature) {
                            model.breakTemperature = breakTemperature;
                            model.cache.clear();
                        }
                        if (!cache.containsKey(name))
                        {
                            CeramicEntityBucketModel unbaked = model.parent.withEntityType(containedEntityType, bucket.cracksBucket(stack));
                            IBakedModel bakedModel = unbaked.bake(model.owner, bakery, ModelLoader.defaultTextureGetter(), model.originalTransform, model.getOverrides(), REBAKE_LOCATION);
                            cache.put(name, bakedModel);
                            return bakedModel;
                        }
                        return cache.get(name);
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
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final IModelTransform originalTransform;

        private int breakTemperature;

        BakedModel(ModelBakery bakery,
                   IModelConfiguration owner, CeramicEntityBucketModel parent,
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
            this.breakTemperature = ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.get();
        }
    }
}
