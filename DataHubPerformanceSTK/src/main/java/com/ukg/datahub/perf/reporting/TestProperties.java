
package com.ukg.datahub.perf.reporting;

public enum TestProperties {
    PR_PROP_TEST_DESC("testDesc"),
    PR_PROP_TEST_ID("testId"),
    PR_PROP_PRODUCT_LOGIN_URL("productLoginURL"),
    PR_PROP_DB_USER("dbUser"),
    PR_PROP_DB_NAME("dbName"),
    PR_PROP_GCP_DATA_PROJECT("gcpDataProject"),
    PR_PROP_TENANT_NAME("tenantName");


    private final String value;

    TestProperties(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
