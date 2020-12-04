package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.api.crafting.FluidIngredient;
import cech12.ceramicbucket.compat.ModCompat;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.item.AbstractCeramicBucketItem;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.api.crafting.FilledCeramicBucketIngredient;
import cech12.ceramicbucket.item.crafting.CeramicBucketDyeRecipe;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    // Use for data generation and development
    public static final boolean DEVELOPMENT_MODE = Boolean.parseBoolean(System.getProperty(MOD_ID + ".developmentMode", "false"));

    public CeramicBucketMod() {
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));

        MinecraftForge.EVENT_BUS.addListener(CeramicBucketMod::onEntityInteract);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(
                (stack, color) -> (color > 0) ? -1 : ((AbstractCeramicBucketItem)stack.getItem()).getColor(stack),
                CeramicBucketItems.CERAMIC_BUCKET,
                CeramicBucketItems.FILLED_CERAMIC_BUCKET,
                CeramicBucketItems.CERAMIC_MILK_BUCKET,
                CeramicBucketItems.CERAMIC_ENTITY_BUCKET);
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        //dye recipe serializer
        event.getRegistry().register(CeramicBucketDyeRecipe.SERIALIZER);
        //ingredient serializers
        CraftingHelper.register(FluidIngredient.Serializer.NAME, FluidIngredient.Serializer.INSTANCE);
        CraftingHelper.register(FilledCeramicBucketIngredient.Serializer.NAME, FilledCeramicBucketIngredient.Serializer.INSTANCE);
    }

    /**
     * Add milking and obtaining interaction.
     */
    private static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        if (entity == null) return;

        PlayerEntity player = event.getPlayer();
        ItemStack itemstack = player.getHeldItem(event.getHand());

        if (ServerConfig.MILKING_ENABLED.get() && ModCompat.canEntityBeMilked(entity)) {
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
            }
        } else if (ServerConfig.FISH_OBTAINING_ENABLED.get()) {
            //check if filled ceramic bucket is there and contains a fluid
            //or ceramic bucket is there
            Fluid fluid = FluidUtil.getFluidContained(itemstack).orElse(FluidStack.EMPTY).getFluid();
            if ((fluid != Fluids.EMPTY && itemstack.getItem() != CeramicBucketItems.FILLED_CERAMIC_BUCKET)
                    || (fluid == Fluids.EMPTY && itemstack.getItem() != CeramicBucketItems.CERAMIC_BUCKET)
                    || (itemstack.getItem() == CeramicBucketItems.FILLED_CERAMIC_BUCKET
                        && itemstack.canApplyAtEnchantingTable(Enchantments.INFINITY)
                        && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemstack) > 0)) {
                return;
            }
            //check if the entity can be inside of a ceramic entity bucket
            if (ModCompat.canEntityBeObtained(fluid, entity)) {
                ItemStack filledBucket = ((CeramicEntityBucketItem)CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(fluid, entity);
                CeramicBucketUtils.copyBucketColor(itemstack, filledBucket);
                ((CeramicEntityBucketItem) CeramicBucketItems.CERAMIC_ENTITY_BUCKET).playFillSound(player, filledBucket);
                if (!event.getWorld().isRemote()) {
                    itemstack.shrink(1);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, filledBucket);
                    }
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
