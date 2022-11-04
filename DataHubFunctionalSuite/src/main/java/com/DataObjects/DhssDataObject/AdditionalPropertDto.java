package com.DataObjects.DhssDataObject;

public class AdditionalPropertDto {

    String edapTenantId;
    String tenantName;
    String isViewPointTenant;
    String isUkgProCustomer;
    String isLicensedCustomer;
    String isGcpUkgOwned;
    String isScrubbed;
    String performanceTier;
    String timeZone;
    String gcpProjectId;
    String shortName;

    public AdditionalPropertDto() {
    }

    public AdditionalPropertDto(String edapTenantId, String tenantName, String isViewPointTenant, String isUkgProCustomer, String isLicensedCustomer, String isGcpUkgOwned, String isScrubbed, String performanceTier, String timeZone, String gcpProjectId, String shortName) {
        this.edapTenantId = edapTenantId;
        this.tenantName = tenantName;
        this.isViewPointTenant = isViewPointTenant;
        this.isUkgProCustomer = isUkgProCustomer;
        this.isLicensedCustomer = isLicensedCustomer;
        this.isGcpUkgOwned = isGcpUkgOwned;
        this.isScrubbed = isScrubbed;
        this.performanceTier = performanceTier;
        this.timeZone = timeZone;
        this.gcpProjectId = gcpProjectId;
        this.shortName = shortName;
    }

    public String getEdapTenantId() {
        return edapTenantId;
    }

    public void setEdapTenantId(String edapTenantId) {
        this.edapTenantId = edapTenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getIsViewPointTenant() {
        return isViewPointTenant;
    }

    public void setIsViewPointTenant(String isViewPointTenant) {
        this.isViewPointTenant = isViewPointTenant;
    }

    public String getIsUkgProCustomer() {
        return isUkgProCustomer;
    }

    public void setIsUkgProCustomer(String isUkgProCustomer) {
        this.isUkgProCustomer = isUkgProCustomer;
    }

    public String getIsLicensedCustomer() {
        return isLicensedCustomer;
    }

    public void setIsLicensedCustomer(String isLicensedCustomer) {
        this.isLicensedCustomer = isLicensedCustomer;
    }

    public String getIsGcpUkgOwned() {
        return isGcpUkgOwned;
    }

    public void setIsGcpUkgOwned(String isGcpUkgOwned) {
        this.isGcpUkgOwned = isGcpUkgOwned;
    }

    public String getIsScrubbed() {
        return isScrubbed;
    }

    public void setIsScrubbed(String isScrubbed) {
        this.isScrubbed = isScrubbed;
    }

    public String getPerformanceTier() {
        return performanceTier;
    }

    public void setPerformanceTier(String performanceTier) {
        this.performanceTier = performanceTier;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getGcpProjectId() {
        return gcpProjectId;
    }

    public void setGcpProjectId(String gcpProjectId) {
        this.gcpProjectId = gcpProjectId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
