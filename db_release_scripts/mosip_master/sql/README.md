Release migration and respective revoke sql scripts.


## From 1.1.5 to 1.2.0

1. Execute `1.2.0_master-scripts_release.sql` to migrate from 1.1.5 db to 1.2.0 compatible db.
2. On successful execution, run data migration python scripts under `migration-scripts/1.2.0/*`.
3. On failure, execute `1.2.0_master-scripts_revoke.sql` to restore to pre-migration point.


## Below backups are taken during the migration, Kindly remove them after verifying the migrated database.

template_migr_bkp
module_detail_migr_bkp
identity_schema_migr_bkp
biometric_type_migr_bkp
reg_exceptional_holiday_migr_bkp
device_spec_migr_bkp
device_type_migr_bkp
device_master_migr_bkp
machine_spec_migr_bkp
machine_type_migr_bkp
machine_master_migr_bkp
loc_holiday_migr_bkp
dynamic_field_migr_bkp


NOTE:

1. Unique constraint on `name`, `key_index`, `sign_key_index` was applied on `machine_master` table, All the available duplicates are dumped into `machine_master_migr_dupes` table.
2. Primary key was changed on `loc_holiday` table, Old primary key (id, location_code, lang_code) is changed to (holiday_date, location_code, lang_code)
	All the duplicates w.r.t new primary key is dumped into `loc_holiday_migr_dupes` table.