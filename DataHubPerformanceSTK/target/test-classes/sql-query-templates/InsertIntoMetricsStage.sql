INSERT INTO
    `${projectId}.${datasetName}.metric_stage_${updatedtm}`
(  updatedtm,
    tenant,
    wrapper,
    pipeline,
    metric_name,
    start_ts,
    end_ts,
    json_result)
values(
${updatedtm},
${tenant},
${wrapper},
${pipeline},
${metric_name},
${start_ts},
${end_ts},
${json_result}
)