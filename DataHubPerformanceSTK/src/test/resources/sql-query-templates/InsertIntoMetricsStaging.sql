INSERT INTO
    `${projectId}.${datasetName}.metric_stage_${stageTableNameSuffix}`
(  updatedtm,
    tenant,
    wrapper,
    pipeline,
    metric_name,
    start_ts,
    end_ts,
    json_result)
values(
'${updatedtm}',
'${tenant}',
'${wrapper}',
'${pipeline}',
'${metric_name}',
'${start_ts}',
'${end_ts}',
'${json_result}'
)