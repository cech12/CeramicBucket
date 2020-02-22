package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    /**
     * Add cow interaction.
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        //only interact with cows
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
        }
    }

}
