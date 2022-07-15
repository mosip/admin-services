Release migration and respective revoke sql scripts.


## From 1.1.5 to 1.2.0

1. Take the complete `mosip_master` database backup.
2. Execute `1.2.0_master-scripts_release.sql` to migrate from 1.1.5 db to 1.2.0 compatible db.
3. On successful execution, run data migration python scripts under `migration-scripts/1.2.0/*`.
4. On failure, drop the migration failed DB and restore database backup.