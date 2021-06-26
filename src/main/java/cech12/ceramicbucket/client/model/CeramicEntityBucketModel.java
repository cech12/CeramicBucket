package cech12.ceramicbucket.client.model;

import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemMultiLayerBakedModel;
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
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        RenderMaterial particleLocation = owner.isTexturePresent("particle") ? owner.resolveTexture("particle") : null;
        RenderMaterial baseLocation = owner.isTexturePresent("base") ? owner.resolveTexture("base") : null;
        if (this.isCracked && owner.isTexturePresent("crackedBase")) {
            baseLocation = owner.resolveTexture("crackedBase");
        }
        RenderMaterial entityLocation = null;
        if (this.entityType != null) {
            entityLocation = new RenderMaterial(PlayerContainer.BLOCK_ATLAS, getEntityTexture(ForgeRegistries.ENTITIES.getKey(this.entityType))); //AtlasTexture.LOCATION_BLOCKS_TEXTURE
        }
        TextureAtlasSprite entitySprite = entityLocation != null ? spriteGetter.apply(entityLocation) : null;

        IModelTransform transformsFromModel = owner.getCombinedTransform();


        ImmutableMap<ItemCameraTransforms.TransformType, TransformationMatrix> transformMap =
                PerspectiveMapWrapper.getTransforms(new ModelTransformComposition(transformsFromModel, modelTransform));

        TextureAtlasSprite particleSprite = particleLocation != null ? spriteGetter.apply(particleLocation) : null;
        if (particleSprite == null) particleSprite = entitySprite;


        TransformationMatrix transform = modelTransform.getRotation();

        ItemMultiLayerBakedModel.Builder builder = ItemMultiLayerBakedModel.builder(owner, particleSprite, new CeramicEntityBucketModel.ContainedFluidOverrideHandler(overrides, bakery, owner, this), transformMap);

        if (baseLocation != null)
        {
            builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemLayerModel.getQuadsForSprites(ImmutableList.of(baseLocation), transform, spriteGetter));
        }

        if (entitySprite != null)
        {
            //tint of -1 to avoid coloring the entity layer
            builder.addQuads(ItemLayerModel.getLayerRenderType(false), ItemLayerModel.getQuadsForSprite(-1, entitySprite, transform));
        }

        builder.setParticle(particleSprite);

        return builder.build();
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        Set<RenderMaterial> texs = Sets.newHashSet();

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
        private final ItemOverrideList nested;
        private final ModelBakery bakery;
        private final IModelConfiguration owner;
        private final CeramicEntityBucketModel parent;

        private boolean isCracked;

        private ContainedFluidOverrideHandler(ItemOverrideList nested, ModelBakery bakery, IModelConfiguration owner, CeramicEntityBucketModel parent)
        {
            this.nested = nested;
            this.bakery = bakery;
            this.owner = owner;
            this.parent = parent;
        }

        @Nullable
        @Override
        public IBakedModel resolve(IBakedModel originalModel, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity)
        {
            IBakedModel overridden = nested.resolve(originalModel, stack, world, entity);
            if (overridden != originalModel) return overridden;
            if (stack.getItem() instanceof CeramicEntityBucketItem) {
                CeramicEntityBucketItem bucket = (CeramicEntityBucketItem) stack.getItem();
                EntityType<?> containedEntityType = bucket.getEntityTypeFromStack(stack);
                if (containedEntityType != null) {
                    ResourceLocation typeName = ForgeRegistries.ENTITIES.getKey(containedEntityType);
                    if (typeName != null) {
                        String name = typeName.toString();
                        //reset cache if temperature config changed
                        boolean isCracked = bucket.isCrackedBucket(stack);
                        if (this.isCracked != isCracked) {
                            this.isCracked = isCracked;
                            cache.clear();
                        }
                        if (!cache.containsKey(name)) {
                            CeramicEntityBucketModel unbaked = this.parent.withEntityType(containedEntityType, isCracked);
                            IBakedModel bakedModel = unbaked.bake(owner, bakery, ModelLoader.defaultTextureGetter(), ModelRotation.X0_Y0, this, REBAKE_LOCATION);
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

}
