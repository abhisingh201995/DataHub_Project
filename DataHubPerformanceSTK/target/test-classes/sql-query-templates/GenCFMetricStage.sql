 CREATE OR REPLACE TABLE
      `${projectId}.${datasetName}.cf_stage_${stageTableNameSuffix}` AS
WITH
   rundata AS (
  SELECT
    DISTINCT jsonPayload.tenant AS tenant,
    jsonPayload.wrapper AS wrapper,
    jsonPayload.pipeline AS pipeline,
    jsonPayload.updatedtm AS updatedtm,
    labels.execution_id AS execution_id,
    resource.labels.function_name AS function_name
  FROM
    `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`),
  f_start AS (
  SELECT
    labels.execution_id AS execution_id,
    timestamp
  FROM
    `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`
  WHERE
    textPayload LIKE "%Function execution started%"),
  request_counts AS (
  SELECT
    DISTINCT labels.execution_id AS execution_id,
    ARRAY_LENGTH(JSON_EXTRACT_ARRAY(jsonPayload.message,
        '$.personIds')) person_req_cnt
  FROM
    `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`
  WHERE
    resource.labels.function_name ="getDataWFDhttp"
    AND ARRAY_LENGTH(JSON_EXTRACT_ARRAY(jsonPayload.message,
        '$.personIds'))IS NOT NULL ),
  runtime AS (
  SELECT
    DISTINCT labels.execution_id AS execution_id,
    REGEXP_EXTRACT(jsonPayload.message, r"'cfruntime': [^']*'") cfruntime_text,
    REGEXP_EXTRACT(jsonPayload.message, r"'apiruntime': [^']*'") apiruntime_text,
    REGEXP_EXTRACT(jsonPayload.message, r"Batch: [^']*,") batch_text,
    REGEXP_EXTRACT(jsonPayload.message, r"'chunk': [^']*}") chunk_text,
    REGEXP_EXTRACT(b. size_text, r"size: [^']*") size_text
  FROM
    `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`a
  LEFT OUTER JOIN (
    SELECT
      labels.execution_id AS execution_id,
      REGEXP_EXTRACT(jsonPayload.message, r"size: [^']*") size_text
    FROM
      `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`
    WHERE
      resource.labels.function_name="getDataWFDhttp"
      AND REGEXP_EXTRACT(jsonPayload.message, r"size: [^']*") IS NOT NULL) b
  ON
    a.labels.execution_id = b.execution_id
  WHERE
    a.resource.labels.function_name="getDataWFDhttp"
    AND REGEXP_EXTRACT(a.jsonPayload.message, r"'cfruntime': [^']*'") IS NOT NULL ),
  f_end AS(
  SELECT
    labels.execution_id AS execution_id,
    timestamp,
    NULL status,
    CASE
      WHEN textPayload LIKE "%finished with status: 'ok'" OR textPayload LIKE "%finished with status code: 200" THEN NULL
    ELSE
    textPayload
  END
    AS function_error
  FROM
    `${projectId}.logging.cloudfunctions_googleapis_com_cloud_functions_*`
  WHERE
    textPayload LIKE "%Function execution took%"
    OR textPayload LIKE "%invocation was interrupted%" )
SELECT
  rundata.updatedtm,
  rundata.tenant,
  rundata.wrapper,
  rundata.pipeline,
  function_name,
  f_start.timestamp AS start_ts,
  f_end.timestamp AS end_ts,
  request_counts.person_req_cnt as indicators_cnt,
  CAST(CASE
      WHEN function_name="getDataWFDhttp"AND runtime.cfruntime_text IS NOT NULL THEN REGEXP_EXTRACT(runtime.cfruntime_text, r"[0-9][\.\d]*")
    ELSE
    NULL
  END
    AS Numeric) cfruntime,
  CAST(
    CASE
      WHEN function_name="getDataWFDhttp"AND runtime.apiruntime_text IS NOT NULL THEN REGEXP_EXTRACT(runtime.apiruntime_text, r"[0-9][\.\d]*")
    ELSE
    NULL
  END
    AS Numeric) apiruntime,
  CAST (
    CASE
      WHEN function_name="getDataWFDhttp"AND runtime.batch_text IS NOT NULL THEN REGEXP_EXTRACT(runtime.batch_text, r"[0-9][\.\d]*")
    ELSE
    NULL
  END
    AS Numeric) batchNum,
  CAST(
    CASE
      WHEN function_name="getDataWFDhttp"AND runtime.chunk_text IS NOT NULL THEN REGEXP_EXTRACT(runtime.chunk_text, r"[0-9][\.\d]*")
    ELSE
    NULL
  END
    AS Numeric) chunkNum,
  CAST (
    CASE
      WHEN function_name="getDataWFDhttp"AND runtime.size_text IS NOT NULL THEN REGEXP_EXTRACT(runtime.size_text, r"[0-9][\.\d]*")
    ELSE
    NULL
  END
    AS Numeric) responceSize,
  TIMESTAMP_DIFF(f_end.timestamp,f_start.timestamp,MILLISECOND) func_dur_milli,
  rundata.execution_id
FROM
  rundata
LEFT OUTER JOIN
  f_start
ON
  rundata.execution_id = f_start.execution_id
LEFT OUTER JOIN
  f_end
ON
  rundata.execution_id = f_end.execution_id
LEFT OUTER JOIN
  runtime
ON
  rundata.execution_id = runtime.execution_id
LEFT OUTER JOIN
  request_counts
ON
  rundata.execution_id = request_counts.execution_id