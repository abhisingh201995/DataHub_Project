package com.DataObjects.DhssDataObject;

import java.io.Serializable;
import java.util.Date;

public class DhssTenantInfoDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private long edapTenantId;
	private String tenantName;
	private String shortName;
	private String description;
	private Boolean isActive;
	private Boolean isDeleted;
	private String appKey;
	private String clientId;
	private String clientSecret;
	private String openAMURL;
	private String wfdURL;
	private String federatedTenantVanityURL;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private String solution;
	private String environment;
	private Boolean isSSOEnabled;
	private String tenantSSOUrl;
	private String tenantVanityURL;
	private String tenantSeedUser;
	private String wfdUsername;
	private String localePolicy;
	private String wfdPwd;
	private String isViewPointTenant;
	private String isUkgProCustomer;
	private String isLicensedCustomer;
	private String isGcpUkgOwned;
	private String isScrubbed;
	private String performanceTier;
	private String timeZone;
	private String gcpProjectId;

	public String getGcpProjectId() {
		return gcpProjectId;
	}

	public void setGcpProjectId(String gcpProjectId) {
		this.gcpProjectId = gcpProjectId;
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

	public String getWfdUsername() {
		return wfdUsername;
	}

	public void setWfdUsername(String wfdUsername) {
		this.wfdUsername = wfdUsername;
	}

	public String getLocalePolicy() {
		return localePolicy;
	}

	public void setLocalePolicy(String localePolicy) {
		this.localePolicy = localePolicy;
	}

	public String getTenantSeedUser() {
		return tenantSeedUser;
	}

	public void setTenantSeedUser(String tenantSeedUser) {
		this.tenantSeedUser = tenantSeedUser;
	}

	public long getEdapTenantId() {
		return edapTenantId;
	}

	public void setEdapTenantId(long edapTenantId) {
		this.edapTenantId = edapTenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getOpenAMURL() {
		return openAMURL;
	}

	public void setOpenAMURL(String openAMURL) {
		this.openAMURL = openAMURL;
	}

	public String getWfdURL() {
		return wfdURL;
	}

	public void setWfdURL(String wfdURL) {
		this.wfdURL = wfdURL;
	}

	public String getFederatedTenantVanityURL() {
		return federatedTenantVanityURL;
	}

	public void setFederatedTenantVanityURL(String federatedTenantVanityURL) {
		this.federatedTenantVanityURL = federatedTenantVanityURL;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Boolean getIsSSOEnabled() {
		return isSSOEnabled;
	}

	public void setIsSSOEnabled(Boolean isSSOEnabled) {
		this.isSSOEnabled = isSSOEnabled;
	}

	public String getTenantSSOUrl() {
		return tenantSSOUrl;
	}

	public void setTenantSSOUrl(String tenantSSOUrl) {
		this.tenantSSOUrl = tenantSSOUrl;
	}

	public String getTenantVanityURL() {
		return tenantVanityURL;
	}

	public void setTenantVanityURL(String tenantVanityURL) {
		this.tenantVanityURL = tenantVanityURL;
	}

	public String getWfdPwd() {
		return wfdPwd;
	}

	public void setWfdPwd(String wfdPwd) {
		this.wfdPwd = wfdPwd;
	}

	/*@Override
	public String toString() {
		return "DhssTenantInfo [edapTenantId=" + edapTenantId + ", tenantName=" + tenantName + ", shortName="
				+ shortName + ", description=" + description + ", isActive=" + isActive + ", isDeleted=" + isDeleted
				+ ", appKey=" + appKey + ", clientId=" + clientId + ", clientScrt=" + CoreConstants.MASKING_VAL
				+ ", openAMURL=" + openAMURL + ", wfdURL=" + wfdURL + ", federatedTenantVanityURL="
				+ federatedTenantVanityURL + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", updatedDate=" + updatedDate + ", updatedBy=" + updatedBy + ", tenantProvisioningType="
				+ tenantProvisioningType + ", localeProfileId=" + localeProfileId + ", solution=" + solution
				+ ", environment=" + environment + ", isSSOEnabled=" + isSSOEnabled + ", tenantSSOUrl=" + tenantSSOUrl
				+ ", idpLoginURL=" + idpLoginURL + ", tenantVanityURL=" + tenantVanityURL + ", tenantStatus="
				+ tenantStatus + ", tenantSeedUser=" + tenantSeedUser + ", isCleanUp=" + isCleanUp
				+ ", sourceTenantShortName=" + sourceTenantShortName + ", applicationTenantProperties="
				+ applicationTenantProperties + ", targetTenantShortName=" + targetTenantShortName
				+ ", targetEnvironment=" + targetEnvironment + ", scrubType=" + scrubType + ", sourceURL=" + sourceURL
				+ ", targetURL=" + targetURL + ", isCopySource=" + isCopySource + ", isCopyTarget=" + isCopyTarget
				+ ", copyPeerShortName=" + copyPeerShortName + ", languageDesc=" + languageDesc + ", isLocaleActive="
				+ isLocaleActive + ", wfdUsername=" + wfdUsername + ", localePolicy=" + localePolicy
				+ ", isViewPointTenant=" + isViewPointTenant + ", isUkgProCustomer=" + isUkgProCustomer
				+ ", isLicensedCustomer=" + isLicensedCustomer + ", isGcpUkgOwned=" + isGcpUkgOwned + ", isScrubbed="
				+ isScrubbed + ", performanceTier=" + performanceTier + ", timeZone=" + timeZone +", gcpProjectId=" + gcpProjectId + " ]";
	}*/

}