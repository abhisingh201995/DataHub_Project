INSERT INTO
  `${projectId}.${datasetName}.RUNHistoryNew` (testid,
    runmode,
    updatedtm,
    tenantname,
    wrapper,
    pipeline,
    performanceTier,
    dayCount,
    overrideEndDate,
    overrideStartDate,
    pipelineEndDate,
    pipelineStartDate,
    maxConcurrentApiCall,
    personCount,
    pipelineState,
    stage_cnt )
WITH
  runparms AS(
  SELECT
    '${testId}' AS testid,
    '${runMode}'AS runmode,
    '${updateDtm}' AS updatedtm,
    '${tenantName}'AS tenantname,
    '${wrapper}'AS wrapper,
    '${pipeline}'AS pipeline,
    '${performanceTier}' AS performanceTier,
    '${dayCount}' AS dayCount,
    '${overrideEndDate}'AS overrideEndDate,
    '${overrideStartDate}'AS overrideStartDate,
    '${pipelineEndDate}'AS pipelineEndDate,
    '${pipelineStartDate}'AS pipelineStartDate,
    '${maxConcurrentApiCall}'AS maxConcurrentApiCall,
    '${personCount}' AS personCount,
    '${pipelineState}' AS pipelineState),
  stagecnt AS(
  SELECT
    labels.execution_id AS execution_id,
    jsonPayload.updatedtm AS updatedtm,
    jsonPayload.pipeline AS pipeline,
    REPLACE(REPLACE(jsonPayload.message, 'Stage table has ', ''),'rows','') stage_cnt
  FROM
    `repd-e-eng-12.logging.cloudfunctions_googleapis_com_cloud_functions_*`
  WHERE
    jsonPayload.message LIKE "%Stage%rows"
    AND jsonPayload.pipeline ='${pipeline}'
    AND jsonPayload.updatedtm = '${updateDtm}')
SELECT
  runparms.testid,
  runparms.runmode,
  runparms.updatedtm,
  runparms.tenantname,
  runparms.wrapper,
  runparms.pipeline,
  runparms.performanceTier,
  runparms.dayCount,
  runparms.overrideEndDate,
  runparms.overrideStartDate,
  runparms.pipelineEndDate,
  runparms.pipelineStartDate,
  runparms.maxConcurrentApiCall,
  runparms.personCount,
  runparms.pipelineState,
  stagecnt.stage_cnt
FROM
  runparms
LEFT OUTER JOIN
  stagecnt
ON
  stagecnt.pipeline = '${pipeline}'
  AND stagecnt.updatedtm = '${updateDtm}'