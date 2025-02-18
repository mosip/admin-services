\c mosip_master

ALTER TABLE IF EXISTS master.ca_cert_store
    ADD COLUMN ca_cert_type character varying(25);

COMMENT ON COLUMN master.ca_cert_store.ca_cert_type
    IS 'CA_Certificate Type: Specifies the type of CA_Certificate e.g., Root, Intermediate, end-entity';