# 什么是JWT？

> Json web token (JWT), 是为了在网络应用环境间传递声明而执行的一种基于JSON的开放标准（[(RFC 7519](https://link.jianshu.com?t=https:/tools.ietf.org/html/rfc7519)).该token被设计为紧凑且安全的，特别适用于分布式站点的单点登录（SSO）场景。JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从资源服务器获取资源，也可以增加一些额外的其它业务逻辑所必须的声明信息，该token也可直接被用于认证，也可被加密

# 什么时候使用JWT

一般有以下两个场景

## Authentication

这是最常用的场景，一旦用户登录之后，之后的请求都会带上JWT token，才会允许用户访问被该token授权的资源。在跨域的场景也会很容易使用（cookie不允许跨域访问）

## **Information Exchange**

JWT在不同系统之间安全的传递信息是一种好的方式。

# 传统的session认证

我们知道http协议本身是一种无状态的协议，而这就意味着如果用户向我们的应用提供了用户名和密码来进行用户认证，那么下一次请求时，用户还要再进行一次用户认证才行，根据http协议，我们并不能知道是哪个用户发出的请求，所以为了让我们的应用能识别是哪个用户发出的请求，我们只能在服务器存储一份用户登录信息，这份信息会在响应时传递给浏览器，告诉其保存为cookie，以便下次请求时发送给服务端，这样我们的应用就能识别请求来自那个用户了，这就是传统的基于session认证。

但是这种基于session的认证使应用本身很难得到扩展，随着不同客户端用户的增加，独立的服务器已无法承载更多的用户，而这时候基于session认证应用的问题就会暴露出来

## 基于session认证所显露的问题

### **Session**

每个用户经过我们的应用认证之后，我们的应用都要在服务端做一次记录，以方便用户下次请求的鉴别，通常而言session都是保存在内存中，而随着认证用户的增多，服务端的开销会明显增大。

### 扩展性

用户认证之后，服务端做认证记录，如果认证的记录被保存在内存中的话，这意味着用户下次请求还必须要请求在这台服务器上,这样才能拿到授权的资源，这样在分布式的应用上，相应的限制了负载均衡器的能力。这也意味着限制了应用的扩展能力。

### **CSRF**

因为是基于cookie来进行用户识别的, cookie如果被截获，用户就会很容易受到跨站请求伪造的攻击。

# 基于token的鉴权机制

基于token的鉴权机制类似于http协议也是无状态的，它不需要在服务端去保留用户的认证信息或者会话信息。这就意味着基于token认证机制的应用不需要去考虑用户在哪一台服务器登录了，这就为应用的扩展提供了便利。

流程上是这样的：

l  用户使用用户名密码来请求服务器

l  服务器进行验证用户的信息

l  服务器通过验证发送给用户一个token

l  客户端存储token，并在每次请求时附送上这个token值

l  服务端验证token值，并返回数据

这个token必须要在每次请求时传递给服务端，它应该保存在请求头里， 另外，服务端要支持`CORS(``跨来源资源共享``)`策略，一般我们在服务端这么做就可以了`Access-Control-Allow-Origin: *`

# JWT结构

由三部分组成，以（.）隔开

- Header
- Payload
- Signature

例如：

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ

## header

JWT的header承载两部分信息

- 声明类型，这里是jwt
- 声明加密算法，通常直接使用HMAC SHA256

完整的header就像下面这样

{

  'typ': 'JWT',

  'alg': 'HS256'

}

然后将头部进行base64加密（该加密是可以对称解密的),构成了第一部分.

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
```

## payload

payload是存放有效信息的地方，这个名字像是特指飞机上承载的货品，这些有效信息包含三个部分。

- 标准中注册的声明
- 公共的声明
- 私有的声明

### **标准中注册的声明** (建议但不强制使用) 

- iss:	 jwt签发者
- sub:  jwt所面向的用户
- aud:  接收jwt的一方
- exp:  jwt的过期时间，这个过期时间必须要大于签发时间
- nbf:  定义在什么时间之前，该jwt都是不可用的.  
- iat:  jwt的签发时间 
- jti:  jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

### **公共的声明** 

> 公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息.但不建议添加敏感信息，因为该部分在客户端可解密.

### **私有的声明**

> 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。

 

定义一个payload

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

然后将其进行base64加密，得到Jwt的第二部分。

```
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9
```

## signature

jwt的第三部分是一个签证信息，这个签证信息由三部分组成：

- header (base64后的)
- payload (base64后的)
- secret

这个部分需要base64加密后的header和base64加密后的payload使用`.`连接组成的字符串，然后通过header中声明的加密方式进行加密`secret`组合加密，然后就构成了jwt的第三部分。

 

```javascript
// javascript
var encodedString = base64UrlEncode(header) + '.' + base64UrlEncode(payload);
var signature = HMACSHA256(encodedString, 'secret'); // TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```

![img](file:///C:/Users/liusl12/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)

 

**将这三部分用**`**.**`**连接成一个完整的字符串****,****构成了最终的****jwt:**

```
  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```



**注意：secret是保存在服务器端的，jwt的签发生成也是在服务器端的，secret就是用来进行jwt的签发和jwt的验证，所以，它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。**

# **如何应用**

一般是在请求头里加入`Authorization`，并加上`Bearer`标注：

![img](file:///C:/Users/liusl12/AppData/Local/Temp/msohtmlclip1/01/clip_image003.png)

```js
fetch('api/user/1', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
```

服务端会验证token，如果验证通过就会返回相应的资源。整个流程就是这样的:

![How does a JSON Web Token works](file:///C:/Users/liusl12/AppData/Local/Temp/msohtmlclip1/01/clip_image005.png)

#jjwt学习文档

##依赖引入

> maven

```yaml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
</dependency>
```

##源码分析

+ Jwts.java

![1525759700232](C:\Users\liusl12\AppData\Local\Temp\1525759700232.png)



这个类是Jjwt的核心抽象类，创建token和解析token都通过该方法，

![1525759815678](C:\Users\liusl12\AppData\Local\Temp\1525759815678.png)

该方法是构建header的方法，打开接口JwsHeader.java,可以看到一些定义好的header

![1525760052328](C:\Users\liusl12\AppData\Local\Temp\1525760052328.png)

DefaultJwsHeader.java重写了这四个方法

```java
public class DefaultJwsHeader extends DefaultHeader implements JwsHeader {
    public DefaultJwsHeader() {
    }

    public DefaultJwsHeader(Map<String, Object> map) {
        super(map);
    }

    public String getAlgorithm() {
        return this.getString("alg");
    }

    public JwsHeader setAlgorithm(String alg) {
        this.setValue("alg", alg);
        return this;
    }

    public String getKeyId() {
        return this.getString("kid");
    }

    public JwsHeader setKeyId(String kid) {
        this.setValue("kid", kid);
        return this;
    }
}
```

##创建token

调用Jwts.builder()方法，返回一个DefaultJwtBuilder对象，打开DefaultJwtBuilder.java

```java
public class DefaultJwtBuilder implements JwtBuilder {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Header header;
    private Claims claims;
    private String payload;
    private SignatureAlgorithm algorithm;
    private Key key;
    private byte[] keyBytes;
    private CompressionCodec compressionCodec;

    public DefaultJwtBuilder() {
    }
    ...
}
```

创建token时可以使用的方法：

![1525761138828](C:\Users\liusl12\AppData\Local\Temp\1525761138828.png)

在学习jwt的时候，知道jwt包含三部分：

+ header
+ payload
+ signature

所以我们可以调用以上的这么多方法来构建这三部分

例如：

```java
public String createJwtToken(){
    Key key = MacProvider.generateKey();    //随机产生一个signing key
    return Jwts.builder().setSubject("woaijiaban").signWith(SignatureAlgorithm.HS256,key).compact();     //用Jwt方法产生一个token，签名算法用HS256，可以使用其他算法
}
```

例子中调用了setSubject()方法将设置标准申明中的“sub”，标准申明有哪些？可以查看Claims.java

![1525761760434](C:\Users\liusl12\AppData\Local\Temp\1525761760434.png)

或者还可以自定义私有申明

```java
public String createJwtToken(){
    Key key = MacProvider.generateKey();    //随机产生一个signing key
    return Jwts.builder().setSubject("woaijiaban").claim("name","liusl").signWith(SignatureAlgorithm.HS256,key).compact();     //用Jwt方法产生一个token，签名算法用HS256，可以使用其他算法
}
```

再调用signWith（）方法，使用HS256方式加密，这个方法第二个参数是保存在服务器端的security key，最后compact（）方法返回token

signWith（）方法第二个参数可接受三种类型的参数：

![1525762496653](C:\Users\liusl12\AppData\Local\Temp\1525762496653.png)

最终产生的token为：

eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiInd29haWphaWJhbiciLCJuYW1lIjoibGl1c2wifQ.zE-OikG13MZS_guXiaJaQwPb3P-wpCpCes0-4_250N0

可以解析看看是否为我们创建的token

![1525762562515](C:\Users\liusl12\AppData\Local\Temp\1525762562515.png)

##认证token

通过调用Jwts.parser()方法实现，返回一个DefaultJwtParser.java对象，打开DefaultJwtParser.java

```java
public class DefaultJwtParser implements JwtParser {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private ObjectMapper objectMapper = new ObjectMapper();
    private byte[] keyBytes;
    private Key key;
    private SigningKeyResolver signingKeyResolver;
    private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();
    Claims expectedClaims = new DefaultClaims();
    private Clock clock;
    private long allowedClockSkewMillis;

    public DefaultJwtParser() {
        this.clock = DefaultClock.INSTANCE;
        this.allowedClockSkewMillis = 0L;
    }
    ...
}
```

该类包含了很多方法：

![1525762899805](C:\Users\liusl12\AppData\Local\Temp\1525762899805.png)

**认证原理分析**:

我们知道jwt有三部分组成：

+ head
+ payload
+ signature

当认证token的时候，首先会通过该token解析出signature，与保存在服务端的signature比较，如果一致，则说明token合法。同时可以解析出保存在payload中的内容。

例如：

```java
public String verifyAccessToken(String token){
    try{
        Jwts.parser().setSigningKey(securitySetting.getSecurityToken()).parseClaimsJws("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3b2FpamlhYmFuIiwibmFtZSI6ImxpdXNsIn0.qqfMrX9J93IPTX3wGJHKliiXhvHwE85lEQl_vVZ3FRQ");
        return "Success";
    }
    catch (SignatureException e){
        return "Failture";
    }
};
```

首先调用了setSigningKey()方法，参数是保存在服务端的security key，在通过parseClaimsJws（）将要验证的token传入，进行验证如果验证失败，则会抛出异常。

还可以解析token中的header或者payload，可以通过调用getBody()/getHeader()方法