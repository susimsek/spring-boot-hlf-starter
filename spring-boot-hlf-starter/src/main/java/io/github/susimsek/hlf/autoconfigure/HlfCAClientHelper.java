package io.github.susimsek.hlf.autoconfigure;

import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.NetworkConfig.CAInfo;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


final class HlfCAClientHelper {

    public static CAInfo extractCAInfo(String caName, Resource networkConfig) throws InvalidArgumentException, IOException, NetworkConfigurationException {
        NetworkConfig ccp = getNetworkConfig(networkConfig.getFilename(), networkConfig.getInputStream());
        return getCAInfo(ccp.getClientOrganization().getCertificateAuthorities(), caName);
    }

    public static byte[] extractPem(CAInfo caInfo) throws InvalidArgumentException {
        Properties properties = caInfo.getProperties();
        Optional<byte[]> optionalPemBytes = Optional.ofNullable((byte[]) properties.get("pemBytes"));
        return optionalPemBytes.orElseThrow(() -> new InvalidArgumentException("pem in tlsCACerts is not defined."));
    }

    private static NetworkConfig getNetworkConfig(String filename, InputStream is) throws InvalidArgumentException, NetworkConfigurationException {
        String extension = getExtension(filename).orElseThrow(() -> new InvalidArgumentException(String.format("%s file is not json or yaml file", filename)));
        NetworkConfig ccp;
        switch (extension) {
            case "json":
                ccp = NetworkConfig.fromJsonStream(is);
                break;
            case "yaml":
            case "yml":
                ccp = NetworkConfig.fromYamlStream(is);
                break;
            default:
                throw new InvalidArgumentException(String.format("%s file is not json or yaml file", filename));
        }
        return ccp;
    }


    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private static CAInfo getCAInfo(List<CAInfo> caInfos, String caName) throws InvalidArgumentException {
        return findCAInfoByCAName(caInfos, caName).orElseThrow(() -> new InvalidArgumentException(String.format("%s in certificateAuthorities is not defined.", caName)));
    }

    private static Optional<CAInfo> findCAInfoByCAName(List<CAInfo> caInfos, String caName) throws InvalidArgumentException {
        if (caInfos == null) {
            throw new InvalidArgumentException("CertificateAuthorities is not defined.");
        }
        return caInfos.stream()
                .filter(caInfo -> caInfo.getCAName().equals(caName))
                .findFirst();
    }
}
