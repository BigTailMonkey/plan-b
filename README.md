# plan-b

## 介绍
日常实用小工具，脑洞大开的想法实现。  
具体每个项目的使用可以参考demo模块下的使用示例，详细介绍参考每个分模块下的介绍文件。

## 模块介绍

##### demo
使用样例，所有模块想的使用示例。

##### diff-object-generate
差异对象生成工具。  
依赖于动态字节码编辑技术，javassist，动态生成差异对象。  
其必须依赖diff-object-generate-processor包。

##### diff-object-generate-processor
核心逻辑实现。  
必须配合diff-object-generate包使用。

##### export-excel
基于注解实现导出excel的功能。

##### export-pdf
导出pdf文件。

##### parallel-framework
并行任务执行框架。  
老版本，建议使用parallel-task-framework。

##### parallel-task-framework
并行任务执行框架。

##### spring-test-accelerator
spring项目测试加速器。

##### stream-condition
流式条件编程框架。

#### swagger-enum
将枚举内容直接输出到swagger文档中。  
若接口中字段使用了枚举定义的值，添加相应注解后，可直接将枚举值输出到swagger文档中。

##### time-util
时间处理工具

##### work-log-statistic
工作日志解析工具

##### xxljob-enhance
xxl-job批量参数执行工具  
需要将相关参数写入resources/Param.txt文件中，并在运行时指定配置文件的位置。  
若需要登陆验证则需要使用者自行继承LoginProxy接口实现登陆功能获取相关ticket，并且将配置文件中的needTicket指定为true；若不需要登陆则只需将配置文件中的needTicket指定为false即可。

##### stream-condition
流式条件编程框架  
具体请参考demo模块下的StreamConditionDemo类

##### xxljob-enhance
xxljob定时任务自动调用器。  
须单独部署xxljob服务。