CREATE OR REPLACE TABLE `${projectId}.${datasetName}.metric_stage_${stageTableNameSuffix}`
(
   updatedtm STRING,
    tenant STRING,
    wrapper STRING,
    pipeline STRING,
    metric_name STRING,
    start_ts TIMESTAMP,
    end_ts TIMESTAMP,
    json_result STRING
)
  
    