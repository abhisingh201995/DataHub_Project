INSERT INTO
`${projectId}.${datasetName}.MetricsHistory`
(  updatedtm,
    tenant,
    wrapper,
    pipeline,
    metric_name,
    start_ts,
    end_ts,
    json_result)
SELECT
updatedtm,
    tenant,
    wrapper,
    pipeline,
    metric_name,
    start_ts,
    end_ts,
    json_result
FROM
  `${projectId2}.${datasetName2}.metric_stage_${stageTableNameSuffix}`