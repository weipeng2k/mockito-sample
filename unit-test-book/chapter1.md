# 前言
## 引言

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Java单元测试框架在业界非常多，以JUnit为事实上的标准，而JUnit只是解决了单元测试的基本骨干，而对于Mock的支持却没有。而同样，在Mock方面，Java也有很多开源的选择，诸如`JMock`、`EasyMock`和`Mockito`，而`Mockito`也同样为其中的翘楚，二者能够很好的完成单元测试的工作。本文就是介绍如何使用二者来完成单元测试。

## 存在的问题

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果公司自己搞一个单元测试框架，维护将成为一个大问题，而使用业界成熟的解决方案，将会是一个很好的方式。因为会有一组非常专业的人替你维护，而且不断地有新的Feature可以使用，同样你熟悉这些之后你可以不断的复用这些知识，而不会由于局限在某个特定的框架下（其实这些特定的框架也只是封装了业界的开源方案）。

## 解决方案

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;使用`JUnit`做单元测试的主体框架，如果有`Spring`的支持，可以使用`spring-test`进行支持，对于层与层之间的Mock，则使用`Mockito`来完成。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在通过测试