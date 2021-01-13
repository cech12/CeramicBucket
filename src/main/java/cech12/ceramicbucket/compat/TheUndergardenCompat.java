package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TheUndergardenCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    private static final String MOD_NAME = "undergarden";

    private static final ResourceLocation GWIBLING = new ResourceLocation(MOD_NAME, "gwibling");
    private static final ResourceLocation CATCH_GWIBLING = new ResourceLocation(MOD_NAME, "catch_gwibling");

    public TheUndergardenCompat() {
        super(MOD_NAME);
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(GWIBLING, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

    @Override
    public ResourceLocation getEntityObtainingAdvancement(@Nonnull Entity entity) {
        if (GWIBLING.equals(entity.getType().getRegistryName())) {
            return CATCH_GWIBLING;
        }
        return null;
    }
}
