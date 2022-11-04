package com.DataObjects.DhssDataObject;

public class TenantPipelineSettingsDTO {
        public String pipelineName;
        public String settingName;
        public String settingType;
        public Object settingValue;
        public String settingHelpText;
        public String updatedDateTime;

        public String getPipelineName() {
                return pipelineName;
        }

        public void setPipelineName(String pipelineName) {
                this.pipelineName = pipelineName;
        }

        public String getSettingName() {
                return settingName;
        }

        public void setSettingName(String settingName) {
                this.settingName = settingName;
        }

        public String getSettingType() {
                return settingType;
        }

        public void setSettingType(String settingType) {
                this.settingType = settingType;
        }

        public Object getSettingValue() {
                return settingValue;
        }

        public void setSettingValue(Object settingValue) {
                this.settingValue = settingValue;
        }

        public String getSettingHelpText() {
                return settingHelpText;
        }

        public void setSettingHelpText(String settingHelpText) {
                this.settingHelpText = settingHelpText;
        }

        public String getUpdatedDateTime() {
                return updatedDateTime;
        }

        public void setUpdatedDateTime(String updatedDateTime) {
                this.updatedDateTime = updatedDateTime;
        }

    }
