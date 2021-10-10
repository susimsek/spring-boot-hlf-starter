package io.susimsek.hlf.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hyperledger-fabric.ca-client")
public class HlfCAClientProperties {
    private String caName;
    private boolean allowAllHostNames = true;
    private String adminUserId;
    private String adminPassword;

    public String getCaName() {
        return caName;
    }

    public void setCaName(String caName) {
        this.caName = caName;
    }

    public boolean isAllowAllHostNames() {
        return allowAllHostNames;
    }

    public void setAllowAllHostNames(boolean allowAllHostNames) {
        this.allowAllHostNames = allowAllHostNames;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
