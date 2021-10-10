# Spring Boot Hyperledger Fabric Starter

Spring Boot Starter Hyperledger Fabric provides an easy way to get your Spring boot application using Hyperledger Fabric Gateway SDK v2.2 up and running quickly.

<img src="https://github.com/susimsek/spring-boot-hlf-starter/blob/main/images/spring-boot-hlf-starter.png" alt="Spring Boot Hlf Starter" width="100%" height="100%"/>

To use the Hyperledger Fabric Spring Boot Starter in your projects you can include the maven dependency in your project pom file:

```xml
<dependency>
  <groupId>io.susimsek</groupId>
  <artifactId>spring-boot-hlf-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Spring Boot Configuration

#### Hyperledger Fabric Configuration

+ **hyperledger-fabric.wallet-path**
  The path to wallet.Default value is 'wallet'
+ **hyperledger-fabric.network-config**
  The path to the common connection profile.
+ **hyperledger-fabric.ca-client.admin-user-id**
  The Certificate Authority admin user id
+ **hyperledger-fabric.ca-client.admin-password**
  The Certificate Authority admin user password
+ **hyperledger-fabric.gateway.ca-user.registration-enabled**
  Enable or disable The Certificate Authority registration.Default value is 'false'.
+ **hyperledger-fabric.gateway.ca-user.user-id**
  The Certificate Authority user id
+ **hyperledger-fabric.gateway.ca-user.org-msp**
  The Certificate Authority user Membership Service Providers (MSP) id
+ **hyperledger-fabric.gateway.ca-user.affiliation**
  The Certificate Authority user affiliation
+ **hyperledger-fabric.gateway.channel-name**
  The channel name
+ **hyperledger-fabric.gateway.chaincode-name**
  The chaincode name
+ **hyperledger-fabric.ca-client.allow-all-host-names**
  boolen(true/false) override certificates CN Host matching.Default value is 'true'.
+ **hyperledger-fabric.gateway.discovery**
  Enable or disable service discovery for all transaction submissions for this gateway.Default value is 'true'.

## Spring Boot Configuration Example

```properties
hyperledger-fabric.wallet-path=wallet
hyperledger-fabric.network-config=classpath:cp/connection-org1.json
hyperledger-fabric.ca-client.admin-user-id=admin
hyperledger-fabric.ca-client.admin-password=adminpw
hyperledger-fabric.ca-client.ca-name=ca-org1
hyperledger-fabric.gateway.ca-user.registration-enabled=true
hyperledger-fabric.gateway.ca-user.user-id=tom
hyperledger-fabric.gateway.ca-user.org-msp=Org1MSP
hyperledger-fabric.gateway.ca-user.affiliation=org1.department1
hyperledger-fabric.gateway.channel-name=mychannel
hyperledger-fabric.gateway.chaincode-name=basic
hyperledger-fabric.ca-client.allow-all-host-names=true
hyperledger-fabric.gateway.discovery=true
```