 CREATE OR REPLACE TABLE
      `${projectId}.${datasetName}.bq_stage_${stageTableNameSuffix}` AS
    WITH
      exidjobid AS (
      SELECT
        DISTINCT labels.execution_id AS execution_id,
        REGEXP_EXTRACT(jsonPayload.message, r"(?:'job_id':\s')[^']*'") job_id_text,
      FROM
        `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`
      WHERE
        resource.labels.function_name="bqController"
        AND REGEXP_EXTRACT(jsonPayload.message, r"(?:'job_id':\s')[^']*'") IS NOT NULL)
    SELECT
      creation_time,
      project_id,
      exidjobid.execution_id,
      job_type,
      job_id,
      start_time,
      end_time,
      TIMESTAMP_DIFF(end_time, start_time, MILLISECOND) AS run_time_in_milli,
      query,
      total_bytes_processed,
      total_slot_ms,
      ROUND(total_slot_ms / TIMESTAMP_DIFF(end_time,start_time,MILLISECOND)) AS num_slot,
      ROUND( PERCENTILE_CONT( total_slot_ms / TIMESTAMP_DIFF(end_time,start_time,MILLISECOND),
          0.95) OVER() ) AS percent_95,
      destination_table.dataset_id,
      destination_table.table_id,
      total_bytes_billed
    FROM
      `${projectId}.${regionId}.INFORMATION_SCHEMA.JOBS_BY_PROJECT`,
      exidjobid
    WHERE
      creation_time BETWEEN TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 1 DAY)
      AND CURRENT_TIMESTAMP()
      AND total_bytes_billed >1
      AND job_id = REGEXP_EXTRACT(job_id_text, r"\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b")
    ORDER BY
      creation_time