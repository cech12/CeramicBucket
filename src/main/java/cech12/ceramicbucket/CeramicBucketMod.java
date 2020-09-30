package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.api.crafting.FluidIngredient;
import cech12.ceramicbucket.compat.ModCompat;
import cech12.ceramicbucket.config.Config;
import cech12.ceramicbucket.item.CeramicFishBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import cech12.ceramicbucket.api.crafting.FilledCeramicBucketIngredient;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.fish.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    public CeramicBucketMod() {
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON, MOD_ID + "-common.toml");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
    }

    private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.Serializer.INSTANCE);
        CraftingHelper.register(FilledCeramicBucketIngredient.Serializer.NAME, FilledCeramicBucketIngredient.Serializer.INSTANCE);
    }

    /**
     * Add cow and fish interaction.
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof LivingEntity)) {
            return;
        }
        LivingEntity entity = (LivingEntity) event.getTarget();
        //only interact with cows and fishes
        if (entity instanceof CowEntity || ModCompat.canEntityBeMilked(entity)) {
            PlayerEntity player = event.getPlayer();
            ItemStack itemstack = player.getHeldItem(event.getHand());
            if (itemstack.getItem() == CeramicBucketItems.CERAMIC_BUCKET && !player.abilities.isCreativeMode && !entity.isChild()) {
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                if (!event.getWorld().isRemote()) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(event.getHand(), ((CeramicMilkBucketItem)CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance());
                    } else if (!player.inventory.addItemStackToInventory(((CeramicMilkBucketItem)CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance())) {
                        player.dropItem(((CeramicMilkBucketItem)CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(), false);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        } else if (entity instanceof AbstractFishEntity) {
            AbstractFishEntity fishEntity = (AbstractFishEntity) entity;
            PlayerEntity player = event.getPlayer();
            ItemStack itemstack = player.getHeldItem(event.getHand());
            if (itemstack.getItem() == CeramicBucketItems.FILLED_CERAMIC_BUCKET && ((FilledCeramicBucketItem) itemstack.getItem()).getFluid(itemstack) == Fluids.WATER && fishEntity.isAlive()) {
                fishEntity.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
                if (!event.getWorld().isRemote()) {
                    itemstack.shrink(1);
                    //-------------------------------------------
                    //get ceramic variant
                    ItemStack bucket;
                    if (fishEntity instanceof PufferfishEntity) {
                        bucket = ((CeramicFishBucketItem) CeramicBucketItems.PUFFERFISH_CERAMIC_BUCKET).getFilledInstance();
                    } else if (fishEntity instanceof CodEntity) {
                        bucket = ((CeramicFishBucketItem) CeramicBucketItems.COD_CERAMIC_BUCKET).getFilledInstance();
                    } else if (fishEntity instanceof TropicalFishEntity) {
                        bucket = ((CeramicFishBucketItem) CeramicBucketItems.TROPICAL_FISH_CERAMIC_BUCKET).getFilledInstance();
                    } else {
                        bucket = ((CeramicFishBucketItem) CeramicBucketItems.SALMON_CERAMIC_BUCKET).getFilledInstance();
                    }
                    //setBucketData
                    if (fishEntity.hasCustomName()) {
                        bucket.setDisplayName(fishEntity.getCustomName());
                    }
                    if (fishEntity instanceof TropicalFishEntity) {
                        CompoundNBT compoundnbt = bucket.getOrCreateTag();
                        compoundnbt.putInt("BucketVariantTag", ((TropicalFishEntity)fishEntity).getVariant());
                    }
                    //-------------------------------------------
                    if (!fishEntity.world.isRemote) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, bucket);
                    }
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(event.getHand(), bucket);
                    } else if (!player.inventory.addItemStackToInventory(bucket)) {
                        player.dropItem(bucket, false);
                    }
                    fishEntity.remove();
                }
                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

}
