# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table foodgroups (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  langual_code                  varchar(255),
  parent_id                     bigint,
  constraint pk_foodgroups primary key (id)
);

create table fooditems (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  scientific_name               varchar(255),
  lmv_food_number               bigint,
  lmv_project                   varchar(255),
  energy_kcal                   float,
  energy_kj                     float,
  carbohydrates_g               float,
  protein_g                     float,
  fibre_g                       float,
  whole_grain_g                 float,
  cholesterol_mg                float,
  water_g                       float,
  alcohol_g                     float,
  ash_g                         float,
  waste_percent                 float,
  sugars_g                      float,
  monosaccharides_g             float,
  disaccharides_g               float,
  sucrose_g                     float,
  fat_g                         float,
  sum_saturated_fats_g          float,
  fatty_acid_40_100_g           float,
  fatty_acid_120_g              float,
  fatty_acid_140_g              float,
  fatty_acid_160_g              float,
  fatty_acid_180_g              float,
  fatty_acid_200_g              float,
  sum_monounsaturated_fats_g    float,
  fatty_acid_161_g              float,
  fatty_acid_181_g              float,
  sum_polyunsaturated_fats_g    float,
  fatty_acid_182_g              float,
  fatty_acid_183_g              float,
  fatty_acid_204_g              float,
  epa_fatty_acid_205_g          float,
  dpa_fatty_acid_225_g          float,
  dha_fatty_acid_226_g          float,
  retinol_ug                    float,
  beta_karoten_ug               float,
  vitamin_a_ug                  float,
  vitamin_b6_ug                 float,
  vitamin_b12_ug                float,
  vitamin_c_mg                  float,
  vitamin_d_ug                  float,
  vitamin_e_mg                  float,
  vitamin_k_ug                  float,
  thiamine_mg                   float,
  riboflavin_mg                 float,
  niacin_mg                     float,
  niacin_equivalents_mg         float,
  folate_ug                     float,
  phosphorus_mg                 float,
  iodine_ug                     float,
  iron_mg                       float,
  calcium_mg                    float,
  potassium_mg                  float,
  magnesium_mg                  float,
  sodium_mg                     float,
  salt_g                        float,
  selenium_ug                   float,
  zink_mg                       float,
  part_of_plant_or_animal_id    bigint,
  physical_form_id              bigint,
  heat_treatment_id             bigint,
  cooking_method_id             bigint,
  industrial_process_id         bigint,
  preservation_method_id        bigint,
  packing_medium_id             bigint,
  packing_type_id               bigint,
  packing_material_id           bigint,
  label_claim_id                bigint,
  geographic_source_id          bigint,
  distinctive_features_id       bigint,
  constraint uq_fooditems_lmv_food_number unique (lmv_food_number),
  constraint pk_fooditems primary key (id)
);

create table fooditems_foodgroups (
  food_items_id                 bigint not null,
  food_groups_id                bigint not null,
  constraint pk_fooditems_foodgroups primary key (food_items_id,food_groups_id)
);

create table fooditems_foodsources (
  food_items_id                 bigint not null,
  food_sources_id               bigint not null,
  constraint pk_fooditems_foodsources primary key (food_items_id,food_sources_id)
);

create table foodsources (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  langual_code                  varchar(255),
  constraint pk_foodsources primary key (id)
);

create table langualterms (
  id                            bigint auto_increment not null,
  code                          varchar(255),
  name                          varchar(255) not null,
  type                          varchar(23),
  constraint ck_langualterms_type check ( type in ('PART_OF_PLANT_OR_ANIMAL','PHYSICAL_FORM','HEAT_TREATMENT','COOKING_METHOD','INDUSTRIAL_PROCESS','PRESERVATION_METHOD','PACKING_MEDIUM','PACKING_TYPE','PACKING_MATERIAL','LABEL_CLAIM','GEOGRAPHIC_SOURCE','DISTINCTIVE_FEATURES')),
  constraint pk_langualterms primary key (id)
);

alter table foodgroups add constraint fk_foodgroups_parent_id foreign key (parent_id) references foodgroups (id) on delete restrict on update restrict;
create index ix_foodgroups_parent_id on foodgroups (parent_id);

alter table fooditems add constraint fk_fooditems_part_of_plant_or_animal_id foreign key (part_of_plant_or_animal_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_part_of_plant_or_animal_id on fooditems (part_of_plant_or_animal_id);

alter table fooditems add constraint fk_fooditems_physical_form_id foreign key (physical_form_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_physical_form_id on fooditems (physical_form_id);

alter table fooditems add constraint fk_fooditems_heat_treatment_id foreign key (heat_treatment_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_heat_treatment_id on fooditems (heat_treatment_id);

alter table fooditems add constraint fk_fooditems_cooking_method_id foreign key (cooking_method_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_cooking_method_id on fooditems (cooking_method_id);

alter table fooditems add constraint fk_fooditems_industrial_process_id foreign key (industrial_process_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_industrial_process_id on fooditems (industrial_process_id);

alter table fooditems add constraint fk_fooditems_preservation_method_id foreign key (preservation_method_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_preservation_method_id on fooditems (preservation_method_id);

alter table fooditems add constraint fk_fooditems_packing_medium_id foreign key (packing_medium_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_packing_medium_id on fooditems (packing_medium_id);

alter table fooditems add constraint fk_fooditems_packing_type_id foreign key (packing_type_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_packing_type_id on fooditems (packing_type_id);

alter table fooditems add constraint fk_fooditems_packing_material_id foreign key (packing_material_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_packing_material_id on fooditems (packing_material_id);

alter table fooditems add constraint fk_fooditems_label_claim_id foreign key (label_claim_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_label_claim_id on fooditems (label_claim_id);

alter table fooditems add constraint fk_fooditems_geographic_source_id foreign key (geographic_source_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_geographic_source_id on fooditems (geographic_source_id);

alter table fooditems add constraint fk_fooditems_distinctive_features_id foreign key (distinctive_features_id) references langualterms (id) on delete restrict on update restrict;
create index ix_fooditems_distinctive_features_id on fooditems (distinctive_features_id);

alter table fooditems_foodgroups add constraint fk_fooditems_foodgroups_fooditems foreign key (food_items_id) references fooditems (id) on delete restrict on update restrict;
create index ix_fooditems_foodgroups_fooditems on fooditems_foodgroups (food_items_id);

alter table fooditems_foodgroups add constraint fk_fooditems_foodgroups_foodgroups foreign key (food_groups_id) references foodgroups (id) on delete restrict on update restrict;
create index ix_fooditems_foodgroups_foodgroups on fooditems_foodgroups (food_groups_id);

alter table fooditems_foodsources add constraint fk_fooditems_foodsources_fooditems foreign key (food_items_id) references fooditems (id) on delete restrict on update restrict;
create index ix_fooditems_foodsources_fooditems on fooditems_foodsources (food_items_id);

alter table fooditems_foodsources add constraint fk_fooditems_foodsources_foodsources foreign key (food_sources_id) references foodsources (id) on delete restrict on update restrict;
create index ix_fooditems_foodsources_foodsources on fooditems_foodsources (food_sources_id);


# --- !Downs

alter table foodgroups drop foreign key fk_foodgroups_parent_id;
drop index ix_foodgroups_parent_id on foodgroups;

alter table fooditems drop foreign key fk_fooditems_part_of_plant_or_animal_id;
drop index ix_fooditems_part_of_plant_or_animal_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_physical_form_id;
drop index ix_fooditems_physical_form_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_heat_treatment_id;
drop index ix_fooditems_heat_treatment_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_cooking_method_id;
drop index ix_fooditems_cooking_method_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_industrial_process_id;
drop index ix_fooditems_industrial_process_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_preservation_method_id;
drop index ix_fooditems_preservation_method_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_packing_medium_id;
drop index ix_fooditems_packing_medium_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_packing_type_id;
drop index ix_fooditems_packing_type_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_packing_material_id;
drop index ix_fooditems_packing_material_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_label_claim_id;
drop index ix_fooditems_label_claim_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_geographic_source_id;
drop index ix_fooditems_geographic_source_id on fooditems;

alter table fooditems drop foreign key fk_fooditems_distinctive_features_id;
drop index ix_fooditems_distinctive_features_id on fooditems;

alter table fooditems_foodgroups drop foreign key fk_fooditems_foodgroups_fooditems;
drop index ix_fooditems_foodgroups_fooditems on fooditems_foodgroups;

alter table fooditems_foodgroups drop foreign key fk_fooditems_foodgroups_foodgroups;
drop index ix_fooditems_foodgroups_foodgroups on fooditems_foodgroups;

alter table fooditems_foodsources drop foreign key fk_fooditems_foodsources_fooditems;
drop index ix_fooditems_foodsources_fooditems on fooditems_foodsources;

alter table fooditems_foodsources drop foreign key fk_fooditems_foodsources_foodsources;
drop index ix_fooditems_foodsources_foodsources on fooditems_foodsources;

drop table if exists foodgroups;

drop table if exists fooditems;

drop table if exists fooditems_foodgroups;

drop table if exists fooditems_foodsources;

drop table if exists foodsources;

drop table if exists langualterms;

