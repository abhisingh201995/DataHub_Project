package com.ukg.datahub.perf.datainsertion;

import com.Utilities.GcpHelper;
import com.ukg.datahub.perf.utilities.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataInsertionServiceImpl implements DataInsertionService {

   private Long personConvertUniqueName ;
    protected static Logger logger = LogManager.getLogger(DataInsertionServiceImpl.class);
    private GcpHelper gcpHelper;

    @Override
    public void insertData(InsertionDetails insertionDetails) {
        populatePeopleAndTimeCardTable(insertionDetails);
    }

    private void populatePeopleAndTimeCardTable(InsertionDetails details) {

        String projectId = details.getProjectId();
        String tenantName = details.getTenantName();

        long start = System.currentTimeMillis();

        personConvertUniqueName = start;
        gcpHelper = new GcpHelper(projectId);

        //Delete existing data from People and timecard table
        logger.info("Deleting data from time card and peoples table...!!");
        createAndExecuteSqlScript(QueryTemplate.PEOPLE_TIMECARD_DELETE, projectId, tenantName, 0);

        //Insert Into Peoples table
        logger.info("Inserting  data to people table   " + details.getpeoplesTableRowCount() + " Rows");
        createAndExecuteSqlScript(QueryTemplate.PEOPLE_INSERT, projectId, tenantName, details.getpeoplesTableRowCount());

        //Insert into timeCard table
        logger.info("Inserting data to time card table....!!");
        createAndExecuteSqlScript(QueryTemplate.TIMECARD_INSERT, projectId, tenantName, 0);

        // some time passes
        long end = System.currentTimeMillis();

        long elapsedTime = end - start;

        System.out.println(elapsedTime + " ms");
    }

    private void createAndExecuteSqlScript(QueryTemplate template, String projectId, String tenantName, int parameter) {
        try {
            String script = FileUtils.loadFile(template.getScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("tenantName").by(tenantName)
                    .replacing("personConvertUniqueName").by("personConvert" + personConvertUniqueName)
                    .resolve();
            gcpHelper.queryScript(script);
            String callScript = FileUtils.loadFile(template.getExecuteScriptFilePath())
                    .replacing("projectId").by(projectId)
                    .replacing("tenantName").by(tenantName)
                    .resolve();
            if (parameter <= 0) {
                gcpHelper.queryBatch(callScript);
            } else {
                gcpHelper.queryWithAdditionalParameter(callScript, parameter);
            }
        } catch (Exception e) {
            throw new RuntimeException("failed to create and execute the sql script", e);
        }
    }
}

