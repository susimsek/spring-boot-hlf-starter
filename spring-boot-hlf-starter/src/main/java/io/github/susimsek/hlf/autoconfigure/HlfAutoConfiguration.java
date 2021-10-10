package io.github.susimsek.hlf.autoconfigure;

import io.github.susimsek.hlf.ca.user.service.impl.FabricCAUserServiceImpl;
import io.github.susimsek.hlf.ca.user.FabricCAUser;
import io.github.susimsek.hlf.ca.user.service.FabricCAUserService;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.NetworkConfig.CAInfo;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration(proxyBeanMethods = false)
public class HlfAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties({HlfCAClientProperties.class, HlfProperties.class})
    static class HlfCAClientConfiguration {

        @Bean
        @ConditionalOnMissingBean(CryptoSuite.class)
        public CryptoSuite CryptoSuite() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvalidArgumentException, CryptoException {
            return CryptoSuiteFactory.getDefault().getCryptoSuite();
        }

        @Bean
        @ConditionalOnProperty(prefix = "hyperledger-fabric", name = {"network-config", "ca-client.ca-name"})
        @ConditionalOnMissingBean(HFCAClient.class)
        public HFCAClient hfcaClient(CryptoSuite cryptoSuite, HlfCAClientProperties hlfCAClientProperties, HlfProperties hlfProperties) throws org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, IOException, NetworkConfigurationException {
            CAInfo caInfo = HlfCAClientHelper.extractCAInfo(hlfCAClientProperties.getCaName(), hlfProperties.getNetworkConfig());
            Properties props = caInfo.getProperties();
            props.put("pemBytes", HlfCAClientHelper.extractPem(caInfo));
            props.put("allowAllHostNames", hlfCAClientProperties.isAllowAllHostNames());
            HFCAClient caClient = HFCAClient.createNewInstance(caInfo.getCAName(), caInfo.getUrl(), props);
            caClient.setCryptoSuite(cryptoSuite);
            return caClient;
        }

        @Bean
        @ConditionalOnProperty(prefix = "hyperledger-fabric", name = "wallet-path", matchIfMissing = true)
        @ConditionalOnMissingBean(Wallet.class)
        public Wallet wallet(HlfProperties hlfProperties) throws IOException {
            return Wallets.newFileSystemWallet(Paths.get(hlfProperties.getWalletPath()));
        }

        @Bean
        @ConditionalOnMissingBean(FabricCAUserService.class)
        public FabricCAUserService fabricCAUserService(HFCAClient hfcaClient, Wallet wallet, HlfCAClientProperties hlfCAClientProperties) {
            return new FabricCAUserServiceImpl(hfcaClient, wallet, hlfCAClientProperties.getAdminUserId(), hlfCAClientProperties.getAdminPassword());
        }

    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties({HlfGatewayProperties.class, HlfProperties.class})
    static class HlfGatewayConfiguration {

        @Bean
        @ConditionalOnProperty(prefix = "hyperledger-fabric", name = {"network-config", "gateway.ca-user.user-id"})
        @ConditionalOnMissingBean(Gateway.class)
        public Gateway gateway(HlfGatewayProperties hlfGatewayProperties, HlfProperties hlfProperties, Wallet wallet, FabricCAUserService fabricCAUserService) throws Exception {
            if (hlfGatewayProperties.getCaUser().isRegistrationEnabled()) {
                FabricCAUser user = FabricCAUser.builder()
                        .orgMSP(hlfGatewayProperties.getCaUser().getOrgMsp())
                        .userId(hlfGatewayProperties.getCaUser().getUserId())
                        .affiliation(hlfGatewayProperties.getCaUser().getAffiliation())
                        .build();
                HlfGatewayHelper.registerFabricCAUserIfNotExists(fabricCAUserService, user);
            }
            Gateway.Builder builder = Gateway.createBuilder();
            builder
                    .discovery(hlfGatewayProperties.isDiscovery())
                    .networkConfig(hlfProperties.getNetworkConfig().getInputStream())
                    .identity(wallet, hlfGatewayProperties.getCaUser().getUserId());
            return builder.connect();
        }

        @Bean
        @ConditionalOnProperty(prefix = "hyperledger-fabric.gateway", name = "channel-name")
        @ConditionalOnMissingBean(Network.class)
        public Network network(HlfGatewayProperties hlfGatewayProperties, Gateway gateway) {
            return gateway.getNetwork(hlfGatewayProperties.getChannelName());
        }

        @Bean
        @ConditionalOnProperty(prefix = "hyperledger-fabric.gateway", name = {"chaincode-name"})
        @ConditionalOnMissingBean(Contract.class)
        public Contract contract(HlfGatewayProperties hlfGatewayProperties, Network network) {
            return network.getContract(hlfGatewayProperties.getChaincodeName());
        }

    }
}
