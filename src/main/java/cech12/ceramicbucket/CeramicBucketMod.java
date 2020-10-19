package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.api.crafting.FluidIngredient;
import cech12.ceramicbucket.compat.ModCompat;
import cech12.ceramicbucket.config.Config;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.api.crafting.FilledCeramicBucketIngredient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
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
        PlayerEntity player = event.getPlayer();
        ItemStack itemstack = player.getHeldItem(event.getHand());
        LivingEntity entity = (LivingEntity) event.getTarget();
        //only interact with cows and fish
        if (ModCompat.canEntityBeMilked(entity)) {
            if (itemstack.getItem() == CeramicBucketItems.CERAMIC_BUCKET && !player.abilities.isCreativeMode) {
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
                return;
            }
        }
        if (Config.FISH_OBTAINING_ENABLED.get()) {
            //check if filled ceramic bucket is there and contains a fluid
            //or ceramic bucket is there
            Fluid fluid = FluidUtil.getFluidContained(itemstack).orElse(FluidStack.EMPTY).getFluid();
            if ((fluid != Fluids.EMPTY && itemstack.getItem() != CeramicBucketItems.FILLED_CERAMIC_BUCKET)
                    || (fluid == Fluids.EMPTY && itemstack.getItem() != CeramicBucketItems.CERAMIC_BUCKET)) {
                return;
            }
            //check if the entity can be inside of a ceramic entity bucket
            if (ModCompat.canEntityTypeBeObtained(fluid, entity.getType())) {
                if (!event.getWorld().isRemote()) {
                    itemstack.shrink(1);
                    ItemStack filledBucket = ((CeramicEntityBucketItem)CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(fluid, entity);
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(event.getHand(), filledBucket);
                    } else if (!player.inventory.addItemStackToInventory(filledBucket)) {
                        player.dropItem(filledBucket, false);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

}
