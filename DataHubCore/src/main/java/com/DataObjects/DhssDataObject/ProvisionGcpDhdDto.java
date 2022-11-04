package com.DataObjects.DhssDataObject;

public class ProvisionGcpDhdDto {

    String tenantName;
    String gcpProjectId;
    String gcpProjectName;
    String serviceAccountKey;

    public ProvisionGcpDhdDto(String tenantName, String gcpProjectId, String gcpProjectName, String serviceAccountKey) {
        this.tenantName = tenantName;
        this.gcpProjectId = gcpProjectId;
        this.gcpProjectName = gcpProjectName;
        this.serviceAccountKey = serviceAccountKey;
    }

    public ProvisionGcpDhdDto() {
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getGcpProjectId() {
        return gcpProjectId;
    }

    public void setGcpProjectId(String gcpProjectId) {
        this.gcpProjectId = gcpProjectId;
    }

    public String getGcpProjectName() {
        return gcpProjectName;
    }

    public void setGcpProjectName(String gcpProjectName) {
        this.gcpProjectName = gcpProjectName;
    }

    public String getServiceAccountKey() {
        return serviceAccountKey;
    }

    public void setServiceAccountKey(String serviceAccountKey) {
        this.serviceAccountKey = serviceAccountKey;
    }
}
