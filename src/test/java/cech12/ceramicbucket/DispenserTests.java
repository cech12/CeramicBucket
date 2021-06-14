package cech12.ceramicbucket;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

@IntegrationTestClass("fluids")
public class DispenserTests {

    @IntegrationTest("dispenser_water")
    public void testDispenserPlaceWater(IntegrationTestHelper helper) {
        helper.pushButton(new BlockPos(0, 1, 0));
        helper.assertFluidAt(new BlockPos(1, 1, 1), Fluids.WATER, "Water should have been dispensed");
        ItemStack dispenserItemStack = ((DispenserTileEntity) Objects.requireNonNull(helper.getTileEntity(new BlockPos(0, 1, 1)))).getStackInSlot(0);
        helper.assertTrue(() -> !dispenserItemStack.isEmpty() && dispenserItemStack.getItem() == CeramicBucketItems.CERAMIC_BUCKET, "Empty bucket should have been remain in dispenser");
    }

    @IntegrationTest("dispenser_lava")
    public void testDispenserPlaceLava(IntegrationTestHelper helper) {
        helper.pushButton(new BlockPos(0, 1, 0));
        helper.assertFluidAt(new BlockPos(1, 1, 1), Fluids.LAVA, "Lava should have been dispensed");
        ItemStack dispenserItemStack = ((DispenserTileEntity) Objects.requireNonNull(helper.getTileEntity(new BlockPos(0, 1, 1)))).getStackInSlot(0);
        helper.assertTrue(dispenserItemStack::isEmpty, "No bucket should have been remain in dispenser");
    }

}
