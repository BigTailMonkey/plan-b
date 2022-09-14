# plan-b

#### 介绍
日常实用小工具

#### 模块介绍

##### demo
使用样例

##### export-excel
导出excel

##### export-pdf
导出pdf

##### parallel-task-framework
并行任务执行框架  
通过实现AbstractTaskThread抽象类，生命具体的处理流程  
通过TaskGroupManager管理全部的任务线程实现，管理器会自动根据TaskThread命名进行分组，并可以指定使用线程组使用传入的参数进行处理

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

##### template-object
差异对象生成工具  
依赖于动态字节码编辑技术，javassist，动态生成差异对象