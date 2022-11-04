package com.ukg.datahub.perf.datainsertion;

public class InsertionDetails {

    public String projectId;
    public int peoplesTableRowCount;
    public String tenantName;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getpeoplesTableRowCount() {
        return peoplesTableRowCount;
    }

    public void setpeoplesTableRowCount(int rowCount) {
        this.peoplesTableRowCount = rowCount;
    }
}
