-- ca_cert_type column is added to the ca_cert_store table --
ALTER TABLE IF EXISTS master.ca_cert_store ADD COLUMN ca_cert_type character varying(25);

-- UPGRADE FOR PERFORMANCE OPTIMIZATION INDEXES

CREATE INDEX idx_ca_cert_store_cr_dtimes ON master.ca_cert_store (cr_dtimes);
CREATE INDEX idx_ca_cert_store_upd_dtimes ON master.ca_cert_store (upd_dtimes);
CREATE INDEX idx_ca_cert_store_del_dtimes ON master.ca_cert_store (del_dtimes);
CREATE INDEX idx_ca_cert_times ON ca_cert_store (cr_dtimes, upd_dtimes, del_dtimes);

CREATE INDEX CONCURRENTLY idx_machine_keyindex_not_deleted ON master.machine_master (LOWER(key_index)) WHERE is_deleted = false OR is_deleted IS NULL;
CREATE INDEX idx_machine_keyindex_lower ON master.machine_master (LOWER(key_index));
CREATE INDEX IF NOT EXISTS idx_mac_master_regcntr_id_id ON master.machine_master (regcntr_id, id);
CREATE INDEX IF NOT EXISTS idx_mac_master_cr_dtimes ON master.machine_master (cr_dtimes);
CREATE INDEX IF NOT EXISTS idx_mac_master_upd_dtimes ON master.machine_master (upd_dtimes);
CREATE INDEX IF NOT EXISTS idx_mac_master_del_dtimes ON master.machine_master (del_dtimes);
CREATE UNIQUE INDEX IF NOT EXISTS uq_mac_master_name_lower ON master.machine_master (LOWER(name)) WHERE is_deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uq_mac_master_key_index_lower ON master.machine_master (LOWER(key_index)) WHERE is_deleted = false;

CREATE INDEX IF NOT EXISTS idx_syncjob_cr_dtimes ON master.sync_job_def (cr_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_upd_dtimes ON master.sync_job_def (upd_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_del_dtimes ON master.sync_job_def (del_dtimes);
CREATE INDEX IF NOT EXISTS idx_syncjob_active_upd ON master.sync_job_def (upd_dtimes) WHERE is_deleted = false;

CREATE MATERIALIZED VIEW mv_syncjob_max_times AS SELECT MAX(cr_dtimes) AS max_cr, MAX(upd_dtimes) AS max_upd, MAX(del_dtimes) AS max_del FROM master.sync_job_def;
REFRESH MATERIALIZED VIEW mv_syncjob_max_times;

CREATE INDEX CONCURRENTLY idx_userdetails_regcenter_active ON user_detail (regcntr_id) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_id_active ON master.user_detail (regcntr_id) WHERE is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_sync_window ON master.user_detail (regcntr_id, cr_dtimes, upd_dtimes, del_dtimes);
CREATE INDEX IF NOT EXISTS idx_user_detail_timestamps ON master.user_detail (cr_dtimes, upd_dtimes, del_dtimes);
CREATE INDEX IF NOT EXISTS idx_user_detail_active_id ON master.user_detail (LOWER(id)) WHERE is_active = true AND is_deleted = false;
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr ON master.user_detail(regcntr_id);
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_flags ON master.user_detail(regcntr_id, is_deleted, is_active);
CREATE INDEX IF NOT EXISTS idx_user_detail_regcntr_change ON master.user_detail(regcntr_id, cr_dtimes, upd_dtimes, del_dtimes);

---END UPGRADE FOR PERFORMANCE OPTIMIZATION INDEXES--
