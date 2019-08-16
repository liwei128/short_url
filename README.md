## 短网址服务平台
效果演示<http://www.c1n.cn>
### 1、功能介绍

> 1.1 短网址生成，支持自定义域名和网址，登录用户生成的短网址永久有效。

[![url](https://raw.githubusercontent.com/liwei128/short_url/master/img/home.png)](https://raw.githubusercontent.com/liwei128/short_url/master/img/home.png)

> 1.2后台管理：短网址管理、页面访问量监控、用户管理、黑名单限制等等；支持用户登录注册，开放使用；

[![url](https://raw.githubusercontent.com/liwei128/short_url/master/img/manager.png)](https://raw.githubusercontent.com/liwei128/short_url/master/img/manager.png)

> 1.3访问量分析报表：访问量统计、访问设备统计、浏览器、国家、地区、网络供应商、访问记录明细、高频访问ip 应有尽有。

[![url](https://raw.githubusercontent.com/liwei128/short_url/master/img/flowData.png)](https://raw.githubusercontent.com/liwei128/short_url/master/img/flowData.png)

### 2、快速部署
> 2.1 环境准备
* 申请域名（需备案）
* 服务器资源
* 软件环境：mysql、redis、nginx、JDK1.8
> 2.2 需要关注的配置项
* java代码(java_code文件夹)配置文件application.properties
    ```html
    修改为自己的redis地址
    spring.redis.host=127.0.0.1
    spring.redis.port=6379
    spring.redis.password=password
    
    修改为自己的mysql地址
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/shorturl?useSSL=false&useUnicode=true&characterEncoding=utf8
    spring.datasource.username=root
    spring.datasource.password=password
    
    邮箱配置，用于发送通知（验证码、注册邮件等）
    spring.mail.host=smtp.aliyun.com
    spring.mail.port=465
    spring.mail.username=name@aliyun.com
    spring.mail.password=password
    
    邮件提醒地址（用户生成短网址后提醒管理员审核）
    system.reminderMail=123456@qq.com,12345678@qq.com
    
    短网址域名
    system.domain=url.com
    
    IP信息查询  官网http://user.ip138.com/   token需要去官网注册获取
    ip138.token=******
    ```
* 服务器资源
* 软件环境：mysql、redis、nginx、JDK1.8
    

