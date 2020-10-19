package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;

import java.util.ArrayList;
import java.util.List;

public class UpgradeAquaticCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    List<ObtainableEntityType> obtainableEntityTypes;

    public UpgradeAquaticCompat() {
        super("upgrade_aquatic");
        this.obtainableEntityTypes = new ArrayList<>();
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.SQUID, Fluids.WATER).build());
        //TODO add more buckets, if it supports MC 1.16.2+
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
