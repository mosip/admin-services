-- ca_cert_type column is removed/deleted from ca_cert_store table --
ALTER TABLE IF EXISTS master.ca_cert_store DROP COLUMN IF EXISTS ca_cert_type;

-- ROLLBACK FOR PERFORMANCE OPTIMIZATION INDEXES

DROP INDEX IF EXISTS master.idx_ca_cert_store_cr_dtimes;
DROP INDEX IF EXISTS master.idx_ca_cert_store_upd_dtimes;
DROP INDEX IF EXISTS master.idx_ca_cert_store_del_dtimes;

DROP INDEX IF EXISTS idx_ca_cert_times;

DROP INDEX CONCURRENTLY IF EXISTS master.idx_machine_keyindex_not_deleted;
DROP INDEX IF EXISTS master.idx_machine_keyindex_lower;
DROP INDEX IF EXISTS master.idx_mac_master_regcntr_id_id;
DROP INDEX IF EXISTS master.idx_mac_master_cr_dtimes;
DROP INDEX IF EXISTS master.idx_mac_master_upd_dtimes;
DROP INDEX IF EXISTS master.idx_mac_master_del_dtimes;

DROP INDEX IF EXISTS master.uq_mac_master_name_lower;
DROP INDEX IF EXISTS master.uq_mac_master_key_index_lower;

DROP INDEX IF EXISTS master.idx_syncjob_cr_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_upd_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_del_dtimes;
DROP INDEX IF EXISTS master.idx_syncjob_active_upd;

DROP MATERIALIZED VIEW IF EXISTS mv_syncjob_max_times;

DROP INDEX CONCURRENTLY IF EXISTS idx_userdetails_regcenter_active;

DROP INDEX IF EXISTS master.idx_user_detail_regcntr_id_active;
DROP INDEX IF EXISTS master.idx_user_detail_sync_window;
DROP INDEX IF EXISTS master.idx_user_detail_timestamps;
DROP INDEX IF EXISTS master.idx_user_detail_active_id;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr_flags;
DROP INDEX IF EXISTS master.idx_user_detail_regcntr_change;

-- END ROLLBACK FOR PERFORMANCE OPTIMIZATION INDEXES
