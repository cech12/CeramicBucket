package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MinecraftCompat extends ModCompat.Mod implements ModCompat.MobMilkingMod, ModCompat.EntityTypeObtainingMod {

    private static final ResourceLocation AXOLOTL_IN_A_BUCKET = new ResourceLocation("minecraft", "husbandry/axolotl_in_a_bucket");
    private static final ResourceLocation TACTICAL_FISHING = new ResourceLocation("minecraft", "husbandry/tactical_fishing");

    List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public MinecraftCompat() {
        super("minecraft");
        this.addFish(EntityType.AXOLOTL);
        this.addFish(EntityType.PUFFERFISH);
        this.addFish(EntityType.SALMON);
        this.addFish(EntityType.COD);
        this.addFish(EntityType.TROPICAL_FISH);
    }

    private void addFish(EntityType<?> fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(fish, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean canEntityBeMilked(Entity entity) {
        return entity instanceof Cow || entity instanceof Goat;
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

    @Override
    public ResourceLocation getEntityObtainingAdvancement(@Nonnull Fluid fluid, @Nonnull Entity entity) {
        if (entity.getType() == EntityType.AXOLOTL) {
            return AXOLOTL_IN_A_BUCKET;
        } else {
            return TACTICAL_FISHING;
        }
    }
}
