package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.display.DisplayArchelonShell;
import com.github.sniffity.panthalassa.server.entity.display.DisplayGiantOrthoconeShell;
import com.github.sniffity.panthalassa.server.entity.display.DisplayKronosaurusSkull;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileBlastTorpedo;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTranquilizingTorpedo;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleECSV;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MODID);

    public static final RegistryObject<EntityType<EntityKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.of(EntityKronosaurus::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<EntityMegalodon>> MEGALODON = ENTITY_TYPES.register ("megalodon",()->
            EntityType.Builder.of(EntityMegalodon::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "megalodon").toString()));

    public static final RegistryObject<EntityType<EntityArchelon>> ARCHELON = ENTITY_TYPES.register ("archelon",()->
            EntityType.Builder.of(EntityArchelon::new,MobCategory.MONSTER)
                    .sized(2.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "archelon").toString()));

    public static final RegistryObject<EntityType<EntityMosasaurus>> MOSASAURUS = ENTITY_TYPES.register ("mosasaurus",()->
            EntityType.Builder.of(EntityMosasaurus::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "mosasaurus").toString()));

    public static final RegistryObject<EntityType<EntityCoelacanth>> COELACANTH = ENTITY_TYPES.register ("coelacanth",()->
            EntityType.Builder.of(EntityCoelacanth::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "coelacanth").toString()));

    public static final RegistryObject<EntityType<EntityDunkleosteus>> DUNKLEOSTEUS = ENTITY_TYPES.register ("dunkleosteus",()->
            EntityType.Builder.of(EntityDunkleosteus::new,MobCategory.MONSTER)
                    .sized(1.5F, 1.3F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "dunkleosteus").toString()));

    public static final RegistryObject<EntityType<EntityLeedsichthys>> LEEDSICHTHYS = ENTITY_TYPES.register ("leedsichthys",()->
            EntityType.Builder.of(EntityLeedsichthys::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "leedsichthys").toString()));

    public static final RegistryObject<EntityType<EntityGiantOrthocone>> GIANT_ORTHOCONE = ENTITY_TYPES.register ("giant_orthocone",()->
            EntityType.Builder.of(EntityGiantOrthocone::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "giant_orthocone").toString()));

    public static final RegistryObject<EntityType<EntityBasilosaurus>> BASILOSAURUS = ENTITY_TYPES.register ("basilosaurus",()->
            EntityType.Builder.of(EntityBasilosaurus::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "basilosaurus").toString()));

    public static final RegistryObject<EntityType<EntityThalassomedon>> THALASSOMEDON = ENTITY_TYPES.register ("thalassomedon",()->
            EntityType.Builder.of(EntityThalassomedon::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "thalassomedon").toString()));

    public static final RegistryObject<EntityType<VehicleMRSV>> MRSV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleMRSV>of(VehicleMRSV::new,MobCategory.MISC)
                    .sized(2.0F, 1.5F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<VehicleAGII>> AGII = ENTITY_TYPES.register ("abyss_glider_2_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleAGII>of(VehicleAGII::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "abyss_glider_2_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<VehiclePCSV>> PCSV = ENTITY_TYPES.register ("proteus_class_submersible_vehicle",()->
            EntityType.Builder
                    .<VehiclePCSV>of(VehiclePCSV::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "proteus_class_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<VehicleECSV>> ECSV = ENTITY_TYPES.register ("epimetheus_class_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleECSV>of(VehicleECSV::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "epimetheus_class_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<ProjectileBlastTorpedo>> BLAST_TORPEDO = ENTITY_TYPES.register ("blast_torpedo",()->
            EntityType.Builder
                    .<ProjectileBlastTorpedo>of(ProjectileBlastTorpedo::new,MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Panthalassa.MODID, "blast_torpedo").toString()));

    public static final RegistryObject<EntityType<ProjectileTranquilizingTorpedo>> TRANQUILIZING_TORPEDO = ENTITY_TYPES.register ("tranquilizing_torpedo",()->
            EntityType.Builder
                    .<ProjectileTranquilizingTorpedo>of(ProjectileTranquilizingTorpedo::new,MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(new ResourceLocation(Panthalassa.MODID, "tranquilizing_torpedo").toString()));

    public static final RegistryObject<EntityType<DisplayGiantOrthoconeShell>> GIANT_ORTHOCONE_SHELL = ENTITY_TYPES.register ("giant_orthocone_shell_display",()->
            EntityType.Builder
                    .of(DisplayGiantOrthoconeShell::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "giant_orthocone_shell_display").toString()));
    public static final RegistryObject<EntityType<DisplayKronosaurusSkull>> KRONOSAURUS_SKULL = ENTITY_TYPES.register ("kronosaurus_skull_display",()->
            EntityType.Builder
                    .of(DisplayKronosaurusSkull::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus_skull_display").toString()));
    public static final RegistryObject<EntityType<DisplayArchelonShell>> ARCHELON_SHELL = ENTITY_TYPES.register ("archelon_shell_display",()->
            EntityType.Builder
                    .of(DisplayArchelonShell::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "archelon_shell_display").toString()));
}