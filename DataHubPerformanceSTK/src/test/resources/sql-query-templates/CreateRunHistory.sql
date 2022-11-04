CREATE TABLE IF NOT EXISTS
`${projectId}.${datasetName}.RUNHistoryNew`
  ( testid STRING,
    runmode STRING,
    updatedtm STRING,
    tenantname STRING,
    wrapper STRING,
    pipeline STRING,
    performanceTier STRING,
    dayCount STRING,
    overrideEndDate STRING,
    overrideStartDate STRING,
    pipelineEndDate STRING,
    pipelineStartDate STRING,
    maxConcurrentApiCall STRING,
    personCount STRING,
    pipelineState STRING,
    stage_cnt STRING)
  