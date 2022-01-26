CREATE SCHEMA IF NOT EXISTS master;
CREATE SCHEMA IF NOT EXISTS kernel;
CREATE SCHEMA IF NOT EXISTS admin;

CREATE MEMORY TABLE IF NOT EXISTS master.bulkupload_transaction
(
    id character varying(36)  NOT NULL,
    entity_name character varying(64) NOT NULL,
    upload_operation character varying(64)  NOT NULL,
    status_code character varying(36)  NOT NULL,
    record_count integer,
    uploaded_by character varying(256)  NOT NULL,
    upload_category character varying(36) ,
    uploaded_dtimes timestamp without time zone NOT NULL,
    upload_description character varying ,
    lang_code character varying(3)  NOT NULL,
    is_active boolean NOT NULL,
    cr_by character varying(256)  NOT NULL,
    cr_dtimes timestamp without time zone NOT NULL,
    upd_by character varying(256) ,
    upd_dtimes timestamp without time zone,
    is_deleted boolean DEFAULT false,
    del_dtimes timestamp without time zone
   
);

CREATE MEMORY TABLE IF NOT EXISTS batch_job_execution_params
(
    job_execution_id bigint NOT NULL,
    type_cd character varying(6)  NOT NULL,
    key_name character varying(100) NOT NULL,
    string_val character varying(5000),
    date_val timestamp without time zone,
    long_val bigint,
    double_val double precision,
    identifying character(1)  NOT NULL
);







-- Table: master.batch_job_execution

-- DROP TABLE master.batch_job_execution;

CREATE MEMORY TABLE IF NOT EXISTS batch_job_execution
(
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone,
    job_configuration_location character varying(2500),
    CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id)
);

-- Table: master.batch_step_execution

-- DROP TABLE master.batch_step_execution;

CREATE MEMORY TABLE IF NOT EXISTS batch_step_execution
(
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500) ,
    exit_message character varying(2500) ,
    last_updated timestamp without time zone,
    CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id)
);
-- Table: master.batch_job_instance

-- DROP TABLE master.batch_job_instance;

CREATE MEMORY TABLE IF NOT EXISTS batch_job_instance
(
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL,
    CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id),
    CONSTRAINT job_inst_un UNIQUE (job_name, job_key)
);
-- Table: master.batch_job_execution_context

-- DROP TABLE master.batch_job_execution_context;

CREATE MEMORY TABLE IF NOT EXISTS batch_job_execution_context
(
    job_execution_id bigint NOT NULL,
    short_context character varying(2500)  NOT NULL,
    serialized_context text,
    CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id)
);

CREATE MEMORY TABLE IF NOT EXISTS batch_step_execution_context
(
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text,
    CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id)

);
ALTER TABLE batch_job_execution_params ADD CONSTRAINT IF NOT EXISTS job_exec_params_fk FOREIGN KEY (job_execution_id)
        REFERENCES batch_job_execution (job_execution_id);

ALTER TABLE batch_job_execution_context ADD CONSTRAINT IF NOT EXISTS job_exec_ctx_fk FOREIGN KEY (job_execution_id)
        REFERENCES batch_job_execution (job_execution_id);

ALTER TABLE batch_job_execution ADD CONSTRAINT IF NOT EXISTS job_inst_exec_fk FOREIGN KEY (job_instance_id)
        REFERENCES batch_job_instance (job_instance_id);

ALTER TABLE batch_step_execution ADD CONSTRAINT IF NOT EXISTS job_exec_step_fk FOREIGN KEY (job_execution_id)
        REFERENCES batch_job_execution (job_execution_id);

ALTER TABLE batch_step_execution_context ADD CONSTRAINT IF NOT EXISTS step_exec_ctx_fk FOREIGN KEY (step_execution_id)
        REFERENCES batch_step_execution (step_execution_id);

CREATE SEQUENCE IF NOT EXISTS batch_step_execution_seq;
CREATE SEQUENCE IF NOT EXISTS batch_job_execution_seq;
CREATE SEQUENCE IF NOT EXISTS batch_job_seq;

