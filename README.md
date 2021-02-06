### 优聚集

2020年初，疫情肆虐，想着





### 声明

1、

尽管我已经仔细检查过代码，但是不可避免的可能代码中依旧有一些我个人的私密信息，尤其是诸如邮箱密码之类的，如果被发现了，那么请告知我，或者最起码请不要用它干一些危险的事情，谢谢合作。

2、

因为本套代码之初并不是为了开源而写的，所以对于一些测试中的敏感信息我都是直接写死在代码中的，所以如果您发现了一些私密信息请告知我。



3、

本套代码是基于SpringBoot 2.3.0.RELEASE

在当时，应该是最新的版本，但是在2021年来说，可能也许有点旧了，当然，不是很旧。

这套代码和现在的优聚集 [https://ujuji.com/](https://ujuji.com/) 有很大区别，因为现在的优聚集网站时是我完全在2021年通过golang重构了的。**所以当您发现这和优聚集https://ujuji.com/ 差别很大时，很正常，因为现有优聚集是重构之后的版本。**



4、

如果您想自己部署优聚集前后端项目，最起码得有以下知识点：

- 数据库
- Java
- Maven
- SpringBoot（如果不想修改增加功能，也就无伤大雅）
- Vue.js



### 构建

1、修改配置文件

`src\main\resources\application.yml`

- 修改服务端口

  ```yml
  server:
    port: 4037
  ```

- 修改邮件服务

  ```yml
  mail:
    host: smtp.domain.com
    password: 123456@test
    username: ujuji@domain.com
    port: 25
  ```

- 修改数据库信息

  sql文件是：`java_navigation.sql`

  将其导入数据库并修改如下信息：

  ```yml
  spring:
    profiles: dev
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: root
      url: jdbc:mysql://localhost:3306/java_navigation?useSSL=false&serverTimezone=UTC
    artemis:
      port: 4029
  ```

- Redis信息修改：

  ```yml
  redis:
    host: 127.0.0.1
    password:
    port: 6379
    lettuce:
      pool:
        max-active: 8 #最大连接数
        max-wait: -1 # 表示未限制
        max-idle: 8 # 接池中的最大空闲连接
        min-idle: 0 # 最小空闲链接
    timeout: 5000 # 超时时间
  cache:
    redis:
      time-to-live: 1m
  ```

- jwt的`secret`修改：

  ```yml
  jwt:
    secret: 21312142131232
    expiration: 86400000
    header: Authorization
    prefix: "Bearer "
  ```

- 跨域设置：

  当在生产环境中使用是，您还需要修改跨域（当然，如果是开放子站注册的话，下面这个就无所谓了，所以在代码中src\main\java\com\ujuji\navigation\config\WebConfig.java 放行了所有origin）

  ```yml
  cors:
    origins:
      - http://localhost:8080
  ```



2、当基本配置修改完后，就可以构建了：

执行（需要maven环境）

```bash
mvn clean package
```

成功后，会输出：

```ini
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  11.233 s
[INFO] Finished at: 2021-02-06T10:08:10+08:00
[INFO] ------------------------------------------------------------------------
```

构建出的产物是：

`target\navigation-1.0.0.jar`

简单来说把他放到服务器中执行 `java -jar  navigation-1.0.0.jar`就行



****



好了，基本配置的就完了，**懂Java会Spring不用我说都知道怎么弄，不会的，我写再多也是一脸懵**



之后的通过nginx反向代理到80端口之类的就不多讲了，如果有哪位大神愿意出一个详细的教程，有视频更好。欢迎@我



### 感谢



感谢Jetbrains 这家伟大的IDE开发公司，他们的全家桶极其好用

[https://jetbrains.com/](https://jetbrains.com/)



### 协议

使用本代码请保留前端界面到 https://ujuji.com 的链接

Apache License 2.0

