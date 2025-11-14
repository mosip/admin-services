-- ca_cert_type column is added to the ca_cert_store table --
ALTER TABLE IF EXISTS master.ca_cert_store ADD COLUMN ca_cert_type character varying(25);
