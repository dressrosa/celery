# celery
出于对activemq的使用理解,对api进行了简单的二次封装.来方便使用.
1. 封装topic和queue俩种模式
2. 具体业务只需要指定destinationName和实现handleMessage方法即可,详细可见bizdemo和email包的例子.
3. 消费者跟随项目启动消费~~由于目前消费生产有些耦合,导致多线程情况下冲突, 分开处理即可~~(已解决)