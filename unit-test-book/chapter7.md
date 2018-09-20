# 测试驱动开发简介

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;测试驱动开发的基本思想就是在开发功能代码之前，先编写测试代码，然后只编写使测试通过的功能代码，从而以测试来驱动整个开发过程的进行。这有助于编写简洁可用和高质量的代码，有很高的灵活性和健壮性，能快速响应变化，并加速开发过程。

## 测试开发驱动模式

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;测试驱动开发的基本过程如下：

* 快速新增一个测试
* 运行所有的测试（有时候只需要运行一个或一部分），发现新增的测试不能通过
* 做一些小小的改动，尽快地让测试程序可运行，为此可以在程序中使用一些不合情理的方法
* 运行所有的测试，并且全部通过
* 重构代码，以消除重复设计，优化设计结构

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;简单来说，就是不可运行/可运行/重构——这正是测试驱动开发的口号。

## 可取之处

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;测试驱动开发能够让代码上生产环境之前，能够以使用者的角度审视编写的代码：

* 如果代码难测，那就是对问题的分析还没有到位
* 如果大量的Mock，那就是依赖过于复杂

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;除了能够通过反向刺激让我们看到代码的不足，它还能以使用者的角度去看：

* 这个方法命名是否够妥帖
* 别人用这个函数会误用吗
* 这个类是不是承担了过多的职责