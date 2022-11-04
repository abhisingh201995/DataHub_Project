CREATE OR REPLACE TABLE
  ${datasetName}.Measures AS
SELECT
  pipeline,
  metric_name,
  starttime,
  endtime,
  ARRAY_AGG(STRUCT(key,
      value)) AS header,
  STRUCT( ROUND(CAST(JSON_EXTRACT_SCALAR(measures_json,
          "$.mean") AS NUMERIC )/1048576) AS avg_mem_used_mb,
    JSON_EXTRACT_SCALAR(measures_json,
      "$.count") AS cf_instances ) AS measures
FROM (
  WITH
    keys AS(
    SELECT
      *
    FROM
      UNNEST(ARRAY(
        SELECT
          AS STRUCT updatedtm,
          pipeline,
          metric_name,
          ROW_NUMBER() OVER(PARTITION BY updatedtm, pipeline, metric_name)-1
        OFFSET
          ,
          JSON_EXTRACT_SCALAR(ld,
            "$.key") AS key
        FROM
          Metrics.MetricsHistory
        LEFT JOIN
          UNNEST(json_extract_array(json_result,
              '$.timeSeriesDescriptor.labelDescriptors')) AS ld )) AS key ),
    valueList AS(
    SELECT
      JSON_EXTRACT_SCALAR(lv,
        "$.stringValue") value2,
      ROW_NUMBER() OVER(PARTITION BY group_id)-1
    OFFSET
      ,
      updatedtm,
      pipeline,
      metric_name,
      starttime,
      endtime,
      measures_json
    FROM (
      SELECT
        updatedtm,
        pipeline,
        metric_name,
        json_query(tsd,
          "$") value,
        ROW_NUMBER() OVER() group_id,
        JSON_EXTRACT_SCALAR(pd,
          "$.timeInterval.startTime") AS starttime,
        JSON_EXTRACT_SCALAR(pd,
          "$.timeInterval.endTime") AS endtime,
        JSON_EXTRACT(v,
          "$.distributionValue") AS measures_json,
      FROM
        Metrics.MetricsHistory
      LEFT JOIN
        UNNEST(json_extract_array(json_result,
            '$.timeSeriesData')) AS tsd
      LEFT JOIN
        UNNEST(json_extract_array(tsd,
            '$.pointData')) AS pd
      LEFT JOIN
        UNNEST(json_extract_array(pd,
            '$.values')) AS v) AS base
    LEFT JOIN
      UNNEST(json_extract_array(base.value,
          '$.labelValues')) AS lv )
  SELECT
    vl.pipeline,
    vl.metric_name,
    key,
    ARRAY_AGG(value2) AS value,
    starttime,
    endtime,
    measures_json
  FROM
    keys
  RIGHT OUTER JOIN
    valueList vl
  ON
    keys.
  OFFSET
    = vl.
  OFFSET
    AND keys.updatedtm = vl.updatedtm
    AND keys.pipeline = vl.pipeline
    AND keys.metric_name= vl.metric_name
  GROUP BY
    pipeline,
    metric_name,
    key,
    starttime,
    endtime,
    measures_json )
GROUP BY
  pipeline,
  metric_name,
  starttime,
  endtime,
  measures_json
ORDER BY
  starttime