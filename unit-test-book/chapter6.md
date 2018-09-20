# 单元测试覆盖率

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;就像刻意的刷分数一样，单元测试覆盖率也是一个我们追求的目标，当单元测试行覆盖率超过70%的时候，整个项目的质量会很不错。持续稳定的单元测试覆盖率，会保障一个应用一直处于较稳定的状态，后续投入维护的资源会降低。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在不少IDE中，如：IDEA，都内置了统计单元测试的工具，只需要按照`package`运行测试即可，在这里我们不依赖具体的IDE，而是用maven插件来做。

## jacoco

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该插件对java8的语法支持较好，在`pom`文件中增加配置。

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.1</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>post-unit-test</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
            <configuration>
                <dataFile>target/jacoco.exec</dataFile>
                <outputDirectory>target/jacoco-ut</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当运行`mvn test`时会生产单元测试覆盖率报告。

> 位置一般在项目的 `target/jacoco-ut` 目录下。

## 覆盖率

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;打开目录下的`index.html`可以看到各个类的覆盖率情况。

<center>
    <img src="" />
</center>

## 缺失路径

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;点击到对应的`package`中的类，可以查看缺失的测试路径，这样就可以指导哪些分支没有纳入单测。

<center>
    <img src="" />
</center>