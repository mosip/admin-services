\c mosip_master

COMMENT ON COLUMN master.ca_cert_store.ca_cert_type
    IS NULL;

--Drop ca_cert_type Column (if it exists)--
ALTER TABLE IF EXISTS master.ca_cert_store
    DROP COLUMN IF EXISTS ca_cert_type;