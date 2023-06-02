# Java-SeckillSystem
+ 使用工具：idea2021.3,mysql5.7

+ 技术栈：springboot,rabbitmq,redis

+ 项目介绍（后端）：
+ 解决活动期间的需求量过大对系统造成的问题
+ 解决问题：
+ 超卖：通过redis和乐观锁（增加一个版本号解决系统超卖问题）
+ 减少数据库压力：通过使用redis作为缓存减少数据库的压力，同时通过消息中间件来异步处理定单

