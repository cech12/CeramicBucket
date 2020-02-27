package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.Config;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.fish.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    public CeramicBucketMod() {
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON, MOD_ID + "-common.toml");
    }

    /**
     * Add cow and fish interaction.
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        //only interact with cows and fishes
        if (event.getTarget() instanceof CowEntity) {
            CowEntity cowEntity = (CowEntity) event.getTarget();
            PlayerEntity player = event.getPlayer();
            ItemStack itemstack = player.getHeldItem(event.getHand());
            if (itemstack.getItem() == CeramicBucketItems.CERAMIC_BUCKET && !player.abilities.isCreativeMode && !cowEntity.isChild()) {
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                if (!event.getWorld().isRemote()) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(event.getHand(), new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET))) {
                        player.dropItem(new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET), false);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        } else if (event.getTarget() instanceof AbstractFishEntity) {
            AbstractFishEntity fishEntity = (AbstractFishEntity) event.getTarget();
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
                        bucket = CeramicBucketItems.PUFFERFISH_CERAMIC_BUCKET.getDefaultInstance();
                    } else if (fishEntity instanceof CodEntity) {
                        bucket = CeramicBucketItems.COD_CERAMIC_BUCKET.getDefaultInstance();
                    } else if (fishEntity instanceof TropicalFishEntity) {
                        bucket = CeramicBucketItems.TROPICAL_FISH_CERAMIC_BUCKET.getDefaultInstance();
                    } else {
                        bucket = CeramicBucketItems.SALMON_CERAMIC_BUCKET.getDefaultInstance();
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
