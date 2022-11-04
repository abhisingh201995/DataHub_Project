CREATE TABLE IF NOT EXISTS
  `${projectId}.${datasetName}.CFHistoryNew`
  ( updatedtm STRING,
    tenant STRING,
    wrapper STRING,
    pipeline STRING,
    function_name STRING,
    start_ts TIMESTAMP,
    end_ts TIMESTAMP,
    indicators_cnt INTEGER,
    cfruntime NUMERIC,
    apiruntime NUMERIC,
    batchNum NUMERIC,
    chunkNum NUMERIC,
    responceSize NUMERIC,
    func_dur_milli INTEGER,
    execution_id STRING )