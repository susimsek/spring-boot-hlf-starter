package io.susimsek.hlf.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hyperledger-fabric.gateway")
public class HlfGatewayProperties {
    private boolean discovery = true;
    private CAUser caUser;
    private String channelName;
    private String chaincodeName;


    public boolean isDiscovery() {
        return discovery;
    }

    public void setDiscovery(boolean discovery) {
        this.discovery = discovery;
    }

    public CAUser getCaUser() {
        return caUser;
    }

    public void setCaUser(CAUser caUser) {
        this.caUser = caUser;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public static class CAUser {
        private boolean registrationEnabled = false;
        private String userId;
        private String affiliation;
        private String orgMsp;

        public boolean isRegistrationEnabled() {
            return registrationEnabled;
        }

        public void setRegistrationEnabled(boolean registrationEnabled) {
            this.registrationEnabled = registrationEnabled;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAffiliation() {
            return affiliation;
        }

        public void setAffiliation(String affiliation) {
            this.affiliation = affiliation;
        }

        public String getOrgMsp() {
            return orgMsp;
        }

        public void setOrgMsp(String orgMsp) {
            this.orgMsp = orgMsp;
        }
    }


}
