


-- object: master.ca_cert_store | type: TABLE --
-- DROP TABLE IF EXISTS master.ca_cert_store CASCADE;
CREATE TABLE master.ca_cert_store (
	cert_id character varying(36) NOT NULL,
	cert_subject character varying(500) NOT NULL,
	cert_issuer character varying(500) NOT NULL,
	issuer_id character varying(36) NOT NULL,
	cert_not_before timestamp,
	cert_not_after timestamp,
	crl_uri character varying(120),
	cert_data character varying,
	cert_thumbprint character varying(100),
	cert_serial_no character varying(50),
	partner_domain character varying(36),
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean,
	del_dtimes timestamp,
	ca_cert_type character varying(25),
	CONSTRAINT ca_cert_store_pk PRIMARY KEY (cert_id)

);

-- indexes section -------------------------------------------------
CREATE INDEX pk_cacs_id ON master.ca_cert_store USING btree (cert_id);

-- ddl-end --


-- PERFORMANCE OPTIMIZATION INDEXES
CREATE INDEX idx_ca_cert_store_cr_dtimes ON master.ca_cert_store (cr_dtimes);
CREATE INDEX idx_ca_cert_store_upd_dtimes ON master.ca_cert_store (upd_dtimes);
CREATE INDEX idx_ca_cert_store_del_dtimes ON master.ca_cert_store (del_dtimes);
CREATE INDEX idx_ca_cert_times ON ca_cert_store (cr_dtimes, upd_dtimes, del_dtimes);
CREATE INDEX IF NOT EXISTS idx_ca_cert_domain ON master.ca_cert_store USING btree (partner_domain);
CREATE INDEX IF NOT EXISTS idx_ca_cert_isdeleted ON master.ca_cert_store USING btree (is_deleted);