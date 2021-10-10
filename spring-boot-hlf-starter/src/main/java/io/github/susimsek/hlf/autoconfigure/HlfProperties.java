package io.github.susimsek.hlf.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("hyperledger-fabric")
public class HlfProperties {
    private Resource networkConfig;
    private String walletPath = "wallet";

    public Resource getNetworkConfig() {
        return networkConfig;
    }

    public void setNetworkConfig(Resource networkConfig) {
        this.networkConfig = networkConfig;
    }

    public String getWalletPath() {
        return walletPath;
    }

    public void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }
}
