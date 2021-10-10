package io.susimsek.hlf.ca.user.service;

import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.io.IOException;
import java.security.cert.CertificateException;

public interface FabricCAUserService {

    void enrollAdmin(String orgMspId) throws IOException, InvalidArgumentException, EnrollmentException, CertificateException;

    void registerAndEnrollUser(String orgMspId, String userId, String affiliation) throws Exception;

    Boolean userExist(String userId) throws IOException;
}
