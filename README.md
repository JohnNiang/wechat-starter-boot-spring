# Spring Boot Starter for Wechat

> 当前项目处于开发期，暂无任何实际的可用功能，感兴趣的朋友可以先关注一下！

## 目的

**简化微信对接流程，避免重复性工作和填坑。**

## 引入指南

> 当前仅发布在 Snapshot 仓库中，待功能完善后将会 release 到 maven center。

### Gradle

```groovy
repositories {
    ...
    maven {
        url 'https://oss.sonatype.org/content/groups/public/'
    }
}

implementation 'me.johnniang.wechat:wechat-starter-boot-spring:{version}-SNAPSHOT'
```

### Maven

unkown...

## 功能

- [x] 完成 **Wechat Token** 的获取（需要完善缓存）
- [ ] 完成 **Wechat OAuth Token** 的获取
- [ ] 完成 **签名验证** 功能
- [ ] 完成 **发送客服消息** 功能
- [ ] 完成 **静默获取微信用户基本信息** 功能
- [ ] 完成 **通过 sns 方式获取用户基本信息** 功能
- [ ] 完成 **创建微信二维码** 功能
- [ ] 完成 **上传媒体文件** 功能
- [ ] 完成 **Ticket** 的获取
- [ ] 完成 **构建 JSSDK Config** 功能
- [ ] 完成 **微信支付** 功能