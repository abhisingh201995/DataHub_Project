INSERT INTO
`${projectId}.${datasetName}.CFHistoryNew`
( updatedtm ,
    tenant ,
    wrapper ,
    pipeline ,
    function_name ,
    start_ts ,
    end_ts ,
    indicators_cnt ,
    cfruntime ,
    apiruntime ,
    batchNum ,
    chunkNum ,
    responceSize ,
    func_dur_milli ,
    execution_id  )
SELECT
 updatedtm ,
    tenant ,
    wrapper ,
    pipeline ,
    function_name ,
    start_ts ,
    end_ts ,
    indicators_cnt ,
    cfruntime ,
    apiruntime ,
    batchNum ,
    chunkNum ,
    responceSize ,
    func_dur_milli ,
    execution_id
FROM
  `${projectId2}.${datasetName2}.cf_stage_${stageTableNameSuffix}`