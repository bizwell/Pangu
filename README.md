# 重要说明

**该项目孵化于百味云公司(bizwell.cn)企业内部环境，项目的通用性和可用性存在问题，目前提交的代码仅供参考，故推送至本仓库的 Preview 分支**

现有如下已知问题，请各位看客注意：
- 绝大部分组件启动依赖 Disconf 作为配置管理工具，需在团队内部搭建该软件
- 框架自 2016 年 11 月诞生，直至 2017 年 3 月份之后初创作者基本未参与维护，距今经多人维护，可能存在偏离作者初衷的代码，如规范的不一致等现象
- 存在已知的诸多 BUG 尚未修复
- 某些依赖项目可能在公司的内部 Maven 库中，如 dubbox 是经过魔改后放到私有库供内部使用的
- 有一些代码和思想已经过时
- 文档不甚完善

> 随后，作者 [Muyv](https://github.com/Muyv) 计划对该项目 fork 后进行持续维护，将项目改造成更通用、更先进和易用的开发框架，地址是：[https://github.com/Muyv/Pangu](https://github.com/Muyv/Pangu)，感兴趣的同仁可 watch 该项目来关注进展。

-------

# Pangu 是什么
为方便应用开发团队快速构建服务端应用程序而提供的开发框架。

# Pangu 能为你带来什么
- 快速搭建服务端应用骨架，让你快速进入业务功能开发阶段
- 提供各种高度封装的实用工具包，帮你造好轮子
- 提供各种重组件支持，封装成美观的 API，减少你的配置代码
- 便捷的插件，让你快速部署应用，无需做过多累赘的打包、发布工作
- 不断完善的组件、更多的功能加入和技术支持，让你更专注于业务开发

# 环境要求
- Java 8
- Maven 3

# 主要思路
- 基于 Spring，为了更好地解耦和管理组件
- 注解驱动，脱离繁琐的 XML 配置
- 封装常用组件，同样使用注解声明
- 程序代码是一等公民，配置文件只提供参数，不要干涉程序结构，配置文件能做到的程序都能做到而且更好更符合程序员思维，代码的变更应当通过合理的 DevOps 机制来应对，而非在线上编辑依赖注入关系（配置文件有其适用场景，一切把程序配置化的思路都是耍流氓）
- 职责细分，技术研发团队更关注技术细节，产品开发团队更关注业务逻辑
- 统一规范，用约定的规则来减少团队合作成本，相对于灵活性来说，高效产出才能造就商业价值
- 框架提供一定的灵活性，支持用 Spring 的 XML 和 JavaConfig 来声明额外的组件和配置，灵活性存在的目的是解决框架暂时未覆盖到的需求
- 程序运行时独立占用进程，程序本就应该这样运行，不是吗？

# 使用指南
这里的例子基于 Eclipse，阅读时请注意用语的特殊性

## 构建工程
1. 创建一个 Maven 工程
2. 在 pom.xml 中添加如下内容：
```xml
<parent>
	<groupId>com.joindata.inf</groupId>
	<artifactId>joindata-app</artifactId>
	<version>${pangu.version}</version>
	<relativePath />
</parent>
```
这样就可以使用 Pangu 中预定义好的依赖和其他配置
3. 创建 Java 代码目录和资源目录，并加入到 Build Path 中

## 编写代码
### 包名
请根据规范创建顶级包

### 启动类

#### 编写启动类
在顶级包中创建一个类，该类包含如下内容：
- main 方法，方法体中包含一句 `Bootstrap.boot();` 作用是启动应用
- `@JoindataApp` 或 `@JoindataWebApp` 注解，注解的 value 值必须填写，这是声明应用的 ID
可以参考该示例：
```java
package com.joindata.newproduct.foobar;
 
import com.joindata.inf.boot.Bootstrap;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.support.disconf.EnableDisconf;
	
@JoindataWebApp("NEWPRODUCT.FOOBAR")    // 这个标记了当前应用是个 Web 应用，并且指定应用 ID
public class App
{
	public static void main(String[] args)
	{
		Bootstrap.boot();               // 有这一句就可以启动应用了
	}
}
```

#### 启动类的作用
启动类是程序运行的入口，所以用了经典的 main 方法启动方式来编写。启动类的作用是：
- 定义当前应用的基本信息
- 通过注解声明当前应用要导入哪些组件
- 启动应用容器
- 用户可以自行在 main 方法中写一些代码用来实现一些特殊需求

#### 关于 Bootstrap 启动器
main 方法中通过调用 `Bootstrap.boot()` 来启动框架。`Bootstrap` 是属于 `com.joindata.inf.boot` 包的类，这个包含如下内容：
- 导入了 Spring 基本框架和 Spring Web MVC 框架，以及嵌入式的 Web 服务器
- 定义了一些基本的 Spring 配置
- 定义了一些基本的 Web 容器配置
- 定义注解，用于标注应用信息
- 启动器 `Bootstrap` 类，用于根据实际环境来配置容器并启动 Spring 或 Web 容器

有必要说一下 `com.joindata.inf.boot.annotation` 中提供的两个注解：

|**名称**|**位置**|**作用**|**备注**|
|-----|-----|-----|-----|
|@JoindataApp		| 启动类	| 标注这个注解会启动 Spring 容器，如果不写 Web 服务可以用它 ||
|@JoindataWebApp	| 启动类	| 标注这个注解会启动 Spring+SpringWebMVC+Web 容器						||

### 业务代码	
请参阅 Demo 项目

#### 要点
根据规范创建好各个包，就可以进行业务代码编写了，编写时注意如下几个重点即可：
- 所有需要编写代码的包都应该在顶级包下
- 顶级包下最好只有启动类
- 使用控制层、事务（服务）层、持久层这种模型来定义程序层次结构
- 用注解来声明和使用各个 Bean

#### 基本注解介绍
框架默认启动了注解驱动，这也是提倡的开发方式，几个重要的注解：

|名称|位置|作用|包名|
|--- |--- |--- |--- |
|@Autowired|成员变量|声明要使用这个组件，Spring 会将组件实例注入到该变量|org.springframework.beans.factory.annotation|
|@RestController|类|声明这是个 REST 风格的控制器组件，框架会做关于 REST 服务的特殊处理|org.springframework.web.bind.annotation|
|@Repository|接口或类|声明这是个持久层组件|org.springframework.stereotype|
|@Controller|类|声明这是个控制器组件|org.springframework.stereotype|
|@Service|类|声明这是个服务层组件的实现|org.springframework.stereotype|
|@Component|类|声明这类是个组件，该归 Spring 管理|org.springframework.stereotype|
|@PathVariable|RequestMapping 方法的参数|标注这个参数是绑定 @RequestMapping 中的 {param} 占位符的值，也就是请求的 URL 中这个段的内容|org.springframework.web.bind.annotation|
|@RequestParam|RequestMapping 方法的参数|标注这个的参数是绑定 Web 参数的，改参数的值就是请求的参数值|org.springframework.web.bind.annotation|
|@RequestMapping|Controller 的类或方法|标注这个类或者方法可以处理 Web 请求，其默认参数用来设置 Web 路径|org.springframework.web.bind.annotation|
|@DeleteMapping|Controller 的类或方法|相当于 @RequestMapping(method=DELETE)|org.springframework.web.bind.annotation|
|@GetMapping|Controller 的类或方法|相当于 @RequestMapping(method=GET)|org.springframework.web.bind.annotation|
|@PostMapping|Controller 的类或方法|相当于 @RequestMapping(method=POST)|org.springframework.web.bind.annotation|
|@PutMapping|Controller 的类或方法|相当于 @RequestMapping(method=PUT)|org.springframework.web.bind.annotation|

### 其他细节
#### 日志
日志记录器中定义了一些经典的日志打印方法，如 debug、info、warn、error 等
在任何位置调用下面的方法即可返回一个日志记录器：
```java
com.joindata.inf.common.util.log.Logger.get()
```

我们通常会把日志记录器定义在一个类的开头作为静态变量使用，请参考这个例子：
```java
package com.joindata.newproduct.foobar.biz.product.service.impl;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.newproduct.foobar.biz.product.entity.model.Product;
import com.joindata.newproduct.foobar.biz.product.service.ProductService;
import com.joindata.newproduct.foobar.orm.ProductMapper;
 
/**
 * 产品服务实现
 *
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 5, 2017 5:36:31 PM
 */
@Service // 该注解告诉 Spring 当前实现是 ProductService 的唯一实现
public class ProductServiceImpl implements ProductService
{
	private static final Logger log = Logger.get();

	@Autowired
	private ProductMapper productMapper;

	@Override
	public Product getProduct(String id)
	{
		log.debug("获取产品，ID: {}", id);
		return productMapper.getProduct(id);
	}
}
```
> Pangu 中使用的的日志框架是 log4j2，并对各种主流框架都做了适配，基本上覆盖了所有依赖库使用的日志框架。对于我们的应用开发来说，只需要使用 Pangu 封装的这个记录器来打印日志即可。
> Pangu 对日志记录器做了高度封装，可以在类的任何位置调用 Logger.get() 来获取当前类的日志记录器，并不需要传入当前类的 Class 对象，并且 Logger 会被创建为单例对象，不会每调用一次创建一次，但还是建议将其定义在类的开头作为该类的公共变量使用。

关于配置文件
> 日志的配置文件使用 log4j2 的格式，一般会放在工程的 src/main/resource/ 下并加入到 Build Path 中，下面是配置文件的样例：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="OFF">
	<appenders>
		<Console name="DefaultConsole" target="SYSTEM_OUT">
			<PatternLayout pattern="%-5level %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %logger{36} - %msg%n" />
		</Console>
	</appenders>
	<loggers>
		<root level="INFO">
			<appender-ref ref="DefaultConsole" />
		</root>
	</loggers>
</configuration>
```

## 启动应用
加载相关依赖库和其他资源文件到 Class Path 中并使用 Java 命令运行启动类即可！在 IDE 中一般就是 Run As > Java Application
	

# 实用工具包
工具包的用法相当简单，绝大部分是静态方法，想知道具体提供了哪些工具，请移步 API 文档。实用工具包所在构件：
```xml
<dependency>
	<groupId>com.joindata.inf.common</groupId>
	<artifactId>util</artifactId>
</dependency>
```
**这个依赖已经默认加载，无需自行加入**

# 重组件
重组件其实就是一组 Spring Bean 的集合，用来提供一些专门的功能，比如实现 ORM、FastDFS 文件操作、Redis 访问等。在 Pangu 中将其以插件形式来提供给用户使用。

## 原理 
- 通过 JavaConfig 来集中配置组件
- 封装相关的 Bean，暴露简单 API 给用户
- 提供注解，注解包含少量参数供用户设置
- 打包为 Maven 模块，发布到私服，用户导入后直接使用

## 用法
1. 添加依赖
> groupId 为 com.joindata.inf.common
> artifactId 一般是 xxx-support
2. 在启动类上加注解声明启用该组件，可在注解参数中做少量设置
> 注解一般是 `@EnableXXX` 这种格式
3. 调用方式各不相同，具体用法这里会提供组件使用文档

# 案例
请参阅 Demo 项目
