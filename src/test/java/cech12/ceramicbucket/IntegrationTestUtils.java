package cech12.ceramicbucket;

import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.ArrayList;
import java.util.List;

public class IntegrationTestUtils {

    public static void placeFluid(IntegrationTestHelper helper, BlockPos pos, Fluid fluid) {
        helper.setBlockState(pos, fluid.defaultFluidState().createLegacyBlock());
    }

    public static Entity placeEntity(IntegrationTestHelper helper, BlockPos pos, EntityType<?> entity) {
        return helper.relativePos(pos).map(actualPos -> entity.spawn(helper.getWorld(), null, null, actualPos, SpawnReason.STRUCTURE, true, true)).orElse(null);
    }

    public static Entity getEntity(IntegrationTestHelper helper, BlockPos pos) {
        List<Entity> entityList = getEntities(helper, pos);
        return entityList.isEmpty() ? null : entityList.get(0);
    }

    public static List<Entity> getEntities(IntegrationTestHelper helper, BlockPos pos) {
        return helper.relativePos(pos).map(p -> helper.getWorld().getEntities(null, new AxisAlignedBB(p.getX(), p.getY(), p.getZ(), p.getX() + 1, p.getY() + 1, p.getZ() + 1))).orElse(new ArrayList<>());
    }

    public static ActionResult<ItemStack> useItem(IntegrationTestHelper helper, BlockPos pos, Item item) {
        return useItem(helper, pos, new ItemStack(item));
    }

    public static ActionResult<ItemStack> useItem(IntegrationTestHelper helper, BlockPos pos, ItemStack stack) {
        return helper.relativePos(pos).map(actualPos -> {
            PlayerEntity player = FakePlayerFactory.getMinecraft(helper.getWorld()); // This is required because forge NPEs in place block
            player.setItemInHand(Hand.MAIN_HAND, stack);
            //centered one block above looking down
            player.absMoveTo(actualPos.getX() + 0.5D, actualPos.getY() + 1.0D, actualPos.getZ() + 0.5D, 0F, 90F);
            return stack.use(helper.getWorld(), player, Hand.MAIN_HAND);
        }).orElse(ActionResult.fail(ItemStack.EMPTY));
    }

    public static ItemStack useItemOnEntity(IntegrationTestHelper helper, Entity entity, ItemStack stack) {
        PlayerEntity player = FakePlayerFactory.getMinecraft(helper.getWorld()); // This is required because forge NPEs in place block
        player.setItemInHand(Hand.MAIN_HAND, stack);
        player.interactOn(entity, Hand.MAIN_HAND);
        return player.getItemInHand(Hand.MAIN_HAND);
    }

}
