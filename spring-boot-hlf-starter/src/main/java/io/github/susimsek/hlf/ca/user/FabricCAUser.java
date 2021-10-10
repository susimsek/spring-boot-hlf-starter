package io.github.susimsek.hlf.ca.user;


import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.security.PrivateKey;
import java.util.Set;

public class FabricCAUser implements User {

    private String userId;
    private String orgMSP;
    private String affiliation;
    private X509Identity identity;
    private Set<String> roles;
    private String account;

    public FabricCAUser(String userId, String orgMSP, String affiliation) {
        this.userId = userId;
        this.orgMSP = orgMSP;
        this.affiliation = affiliation;
    }

    public FabricCAUser(String userId, String orgMSP, String affiliation, X509Identity identity, Set<String> roles, String account) {
        this.userId = userId;
        this.orgMSP = orgMSP;
        this.affiliation = affiliation;
        this.identity = identity;
        this.roles = roles;
        this.account = account;
    }

    public static CAUserBuilder builder() {
        return new CAUserBuilder();
    }

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return new Enrollment() {

            @Override
            public PrivateKey getKey() {
                return identity.getPrivateKey();
            }

            @Override
            public String getCert() {
                return Identities.toPemString(identity.getCertificate());
            }
        };
    }

    @Override
    public String getMspId() {
        return orgMSP;
    }


    public static class CAUserBuilder {
        private String userId;
        private String orgMSP;
        private String affiliation;
        private X509Identity identity;
        private Set<String> roles;
        private String account;

        CAUserBuilder() {
        }

        public CAUserBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public CAUserBuilder orgMSP(String orgMSP) {
            this.orgMSP = orgMSP;
            return this;
        }

        public CAUserBuilder affiliation(String affiliation) {
            this.affiliation = affiliation;
            return this;
        }

        public CAUserBuilder identity(X509Identity identity) {
            this.identity = identity;
            return this;
        }

        public CAUserBuilder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public CAUserBuilder account(String account) {
            this.account = account;
            return this;
        }

        public FabricCAUser build() {
            return new FabricCAUser(userId, orgMSP, affiliation, identity, roles, account);
        }

        public String toString() {
            return "CAUser.CAUserBuilder(userId=" + this.userId + ", orgMSP=" + this.orgMSP + ", affiliation=" + this.affiliation + ", identity=" + this.identity + ", roles=" + this.roles + ", account=" + this.account + ")";
        }
    }
}
