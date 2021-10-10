package io.github.susimsek.hlf.ca.user.service.impl;

import io.github.susimsek.hlf.ca.user.FabricCAUser;
import io.github.susimsek.hlf.ca.user.service.FabricCAUserService;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.cert.CertificateException;

public class FabricCAUserServiceImpl implements FabricCAUserService {

    Logger log = LoggerFactory.getLogger(FabricCAUserService.class);

    private final HFCAClient hfcaClient;
    private final Wallet wallet;
    private final String adminUserId;
    private final String adminUserPassword;

    public FabricCAUserServiceImpl(HFCAClient hfcaClient, Wallet wallet, String adminUserId, String adminUserPassword) {
        this.hfcaClient = hfcaClient;
        this.wallet = wallet;
        this.adminUserId = adminUserId;
        this.adminUserPassword = adminUserPassword;
    }

    @Override
    public void enrollAdmin(String orgMspId) throws IOException, InvalidArgumentException, EnrollmentException, CertificateException {
        if (wallet.get(adminUserId) != null) {
            log.warn(String.format("An identity for the admin user \"%s\" already exists in the wallet", adminUserId));
            return;
        }
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = hfcaClient.enroll(adminUserId, adminUserPassword, enrollmentRequestTLS);
        Identity identity = Identities.newX509Identity(orgMspId, enrollment);
        wallet.put(adminUserId, identity);
        log.info(String.format("Successfully enrolled user \"%s\" and imported it into the wallet", adminUserId));
    }

    @Override
    public void registerAndEnrollUser(String orgMspId, String userId, String affiliation) throws Exception {
        if (wallet.get(userId) != null) {
            log.warn(String.format("An identity for the user \"%s\" already exists in the wallet", userId));
            return;
        }
        X509Identity adminIdentity = (X509Identity) wallet.get(adminUserId);
        if (adminIdentity == null) {
            log.warn(String.format("\"%s\" needs to be enrolled and added to the wallet first", adminUserId));
            return;
        }
        FabricCAUser admin = FabricCAUser.builder()
                .userId(adminUserId)
                .orgMSP(orgMspId)
                .affiliation(affiliation)
                .identity(adminIdentity)
                .build();
        // Register the user, enroll the user, and import the new identity into the wallet.
        Enrollment enrollment = getEnrollment(admin, userId);
        Identity user = Identities.newX509Identity(orgMspId, enrollment);
        wallet.put(userId, user);
        log.info(String.format("Successfully enrolled user \"%s\" and imported it into the wallet%n", userId));
    }

    @Override
    public Boolean userExist(String userId) throws IOException {
        return wallet.get(userId) != null;
    }

    private Enrollment getEnrollment(FabricCAUser admin, String userId) throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(userId);
        registrationRequest.setAffiliation(admin.getAffiliation());
        registrationRequest.setEnrollmentID(userId);
        String enrollmentSecret = hfcaClient.register(registrationRequest, admin);
        return hfcaClient.enroll(userId, enrollmentSecret);
    }
}
