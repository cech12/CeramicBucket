package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class QuarkCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();
    private final ObtainableEntityType slimeType;

    public QuarkCompat() {
        super("quark");
        ObtainableEntityType.Builder builder = new ObtainableEntityType.Builder(EntityType.SLIME, Fluids.EMPTY);
        ResourceLocation fillSound = ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.ENTITY_SLIME_SQUISH_SMALL);
        if (fillSound != null) builder.setFillSound(fillSound);
        ResourceLocation emptySound = ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.ENTITY_SLIME_JUMP_SMALL);
        if (emptySound != null) builder.setEmptySound(emptySound);
        this.slimeType = builder.build();
        this.obtainableEntityTypes.add(this.slimeType);
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

    @Override
    public boolean canEntityBeObtained(@Nonnull Fluid fluid, @Nonnull Entity entity) {
        return slimeType.isCorrectFluid(fluid)
                && entity.getType() == EntityType.SLIME
                && ((SlimeEntity) entity).getSlimeSize() == 1
                && entity.isAlive();
    }

}
