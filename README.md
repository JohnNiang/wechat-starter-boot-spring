# Spring Boot Starter for Wechat

> 当前项目处于开发期，暂无任何实际的可用功能，感兴趣的朋友可以先关注一下！

## 目的

**简化微信对接流程，避免重复性工作和填坑。**

## 引入指南

### Gradle

#### Snapshot

```groovy
repositories {
    ...
    maven {
        url 'https://oss.sonatype.org/content/groups/public/'
    }
}

implementation 'me.johnniang.wechat:wechat-starter-boot-spring:0.0.7-SNAPSHOT'
```

#### Release

```groovy
implementation 'me.johnniang.wechat:wechat-starter-boot-spring:0.0.4'
```

### Maven

#### Snapshot

```xml
<repositories>
    <repository>
        <id>nexus_repo</id>
        <name>Nexus oss repo</name>
        <url>https://oss.sonatype.org/content/groups/public/</url>
    </repository>
</repositories>

<dependency>
    <groupId>me.johnniang.wechat</groupId>
    <artifactId>wechat-starter-boot-spring</artifactId>
    <version>0.0.7-SNAPSHOT</version>
</dependency>
```


#### Release

```xml
<dependency>
    <groupId>me.johnniang.wechat</groupId>
    <artifactId>wechat-starter-boot-spring</artifactId>
    <version>0.0.4</version>
</dependency>

```

## 功能

- [x] 完成 **Wechat Token** 的获取（需要完善缓存）
- [ ] 完成 **Wechat OAuth Token** 的获取
- [x] 完成 **签名验证** 功能
- [x] 完成 **发送客服消息** 功能
- [x] 完成 **静默获取微信用户基本信息** 功能
- [x] 完成 **通过 sns 方式获取用户基本信息** 功能
- [ ] 完成 **创建微信二维码** 功能
- [ ] 完成 **上传媒体文件** 功能
- [ ] 完成 **Ticket** 的获取
- [ ] 完成 **构建 JSSDK Config** 功能
- [ ] 完成 **微信支付** 功能
- [x] 完成 **微信消息的接收** 功能

## 开发文档

### 配置微信相关参数

```yaml
johnniang:
  wechat:
    appId: your_app_id
    appSecret: your_app_secret
    validationToken: custom_validation_token for wechat validation
    key: your_payment_key
    mchId: your_mch_id
    paymentNotificationUrl: payment_notification_url
    unifiedOrderUrl: https://api.mch.weixin.qq.com/pay/unifiedorder # Default is sandbox url
```

### WechatService

已经预置 WechatService，开发者可通过以下方式调用：

```java
@Autowired
private WechatService wecahtService;
```

```java
 /**
* Gets wechat token.
*
* @return wechat token
*/
@NonNull
WechatToken getWechatToken();

/**
* Gets wechat token string.
*
* @return wechat token string
*/
@NonNull
String getWechatTokenString();

/**
* Checks signature.
*
* @param signature signature must not be blank
* @param timestamp timestamp must not be blank
* @param nonce     nonce must not be blank
* @return true if the signature is correct, false otherwise
*/
boolean checkSignature(@NonNull String signature, @NonNull String timestamp, @NonNull String nonce);

/**
* Gets wechat user info.
*
* @param openid wechat openid must not be blank
* @return actual wechat user info
*/
@NonNull
WechatUser getWechatUser(@NonNull String openid);

/**
* Gets wechat user info via sns.
*
* @param openid            wechat openid must not be blank
* @param oAuth2AccessToken oauth2 access token
* @return actual wechat user info
*/
@NonNull
WechatUser getWechatUserViaSns(@NonNull String openid, @NonNull String oAuth2AccessToken);

/**
* Sends kf message to wechat user.
*
* @param message kf message content must not be null
*/
void sendKfMessage(@NonNull KfMessage message);
```

### DefaultWechatController

预置了 DefaultWechatController，开发这可通过一下方式使用（当然不是必须的）：

```java

@Controller
@RequestMapping("/api/wechat")
public class WechatController extends DefaultWechatController {

    // 用户可以根据自己的需求进行重构，或者增添相应的方法。

}
```

DefaultWechatController 默认的行为有：

```java
/**
* Validate signature from wechat.
*
* @param signature signature from wechat
* @param timestamp timestamp from wechat
* @param nonce     nonce from wechat
* @param echoStr   echoStr from wechat
* @return echoStr from wechat
*/
@GetMapping
public String validate(@RequestParam("signature") String signature,
                    @RequestParam("timestamp") String timestamp,
                    @RequestParam("nonce") String nonce,
                    @RequestParam("echostr") String echoStr)
```

```java
/**
* Handle wechat message.
*
* @param request http servlet request (Auto inject)
* @return passive wechat message
* @throws IOException throws when HttpServletRequest#getInputStream() invokes error
*/
@PostMapping(produces = MediaType.APPLICATION_XML_VALUE)
public WechatMessage handleWechatMessage(HttpServletRequest request)
```
