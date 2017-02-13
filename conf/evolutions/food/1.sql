# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table foodgroups (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  langual_code                  varchar(255),
  parent_id                     bigint,
  constraint uq_foodgroups_parent_id unique (parent_id),
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
  constraint uq_fooditems_lmv_food_number unique (lmv_food_number),
  constraint pk_fooditems primary key (id)
);

create table fooditems_foodgroups (
  food_items_id                 bigint not null,
  food_groups_id                bigint not null,
  constraint pk_fooditems_foodgroups primary key (food_items_id,food_groups_id)
);

alter table foodgroups add constraint fk_foodgroups_parent_id foreign key (parent_id) references foodgroups (id) on delete restrict on update restrict;

alter table fooditems_foodgroups add constraint fk_fooditems_foodgroups_fooditems foreign key (food_items_id) references fooditems (id) on delete restrict on update restrict;
create index ix_fooditems_foodgroups_fooditems on fooditems_foodgroups (food_items_id);

alter table fooditems_foodgroups add constraint fk_fooditems_foodgroups_foodgroups foreign key (food_groups_id) references foodgroups (id) on delete restrict on update restrict;
create index ix_fooditems_foodgroups_foodgroups on fooditems_foodgroups (food_groups_id);


# --- !Downs

alter table foodgroups drop foreign key fk_foodgroups_parent_id;

alter table fooditems_foodgroups drop foreign key fk_fooditems_foodgroups_fooditems;
drop index ix_fooditems_foodgroups_fooditems on fooditems_foodgroups;

alter table fooditems_foodgroups drop foreign key fk_fooditems_foodgroups_foodgroups;
drop index ix_fooditems_foodgroups_foodgroups on fooditems_foodgroups;

drop table if exists foodgroups;

drop table if exists fooditems;

drop table if exists fooditems_foodgroups;

