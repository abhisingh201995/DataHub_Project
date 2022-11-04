CREATE TABLE IF NOT EXISTS
  `${projectId}.${datasetName}.BQHistory`
  ( creation_time TIMESTAMP,
    project_id STRING,
    execution_id STRING,
    job_type STRING,
    job_id STRING,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    run_time_in_milli INTEGER,
    query STRING,
    total_bytes_processed INTEGER,
    total_slot_ms INTEGER,
    num_slot FLOAT64,
    percent_95 FLOAT64,
    dataset_id STRING,
    table_id STRING,
    total_bytes_billed INTEGER)