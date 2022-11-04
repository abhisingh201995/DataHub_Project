package com.ukg.datahub.perf.datainsertion;

public enum QueryTemplate {

    PEOPLE_INSERT("Stored_procedure_populatePeople.sql", "Insert_to_people_tables.sql"),
    TIMECARD_INSERT("Stored_procedure_populateTimeCard.sql", "Insert_to_timeCardTotal_table.sql"),
    PEOPLE_TIMECARD_DELETE("Stored_procedure_resetPeopletoBase.sql", "Delete_people_and_timecard_data.sql"),
    UPDATE_TABLE("Update_pipeline_performance_table.sql"),
    CREATE_CFHISTORY_TABLE("CreateCFHistory.sql"),
    CREATE_BQHISTORY_TABLE("CreateBQHistory.sql"),
    CREATE_METRICSHISTORY_TABLE("CreateMetricsHistory.sql"),
    CREATE_RUNHISTORY_TABLE("CreateRunHistory.sql"),
    INSERT_INTO_RUNHISTORY_TABLE("Insert_to_runHistory_table.sql"),
    MOVE_TO_CFHISTORY("MoveToCFHistory.sql"),
    MOVE_TO_BQHISTORY("MoveToBQHistory.sql"),
    MOVE_TO_METRICHISTORY("MoveToMetricHistory.sql"),
    INSERT_INTO_METRIC_STAGE("InsertIntoMetricsStaging.sql"),
    GEN_CFMETRICS_STAGE("GenCFMetricStage.sql"),
    GEN_BQMETRICS_STAGE("GenBQMetricStage.sql"),
    CREATE_METRIC_STAGE("CreateMetricStage.sql"),
    DROP_STAGE_TABLES("Drop_stage_tables.sql"),
    GENERATE_METRICS_MEASURES("Generate_Metrics_Measures.sql");

    private String BASE_PATH = "sql-query-templates/";
    private String scriptFileName;
    private String scriptExecutionFileName;


    QueryTemplate(String scriptFileName, String scriptExecutionFileName) {
        this.scriptFileName = scriptFileName;
        this.scriptExecutionFileName = scriptExecutionFileName;
    }

    QueryTemplate(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public String getScriptFilePath() {
        return BASE_PATH + this.scriptFileName;
    }

    public String getExecuteScriptFilePath() {
        return BASE_PATH + this.scriptExecutionFileName;
    }

}
