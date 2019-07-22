# may-starter

### 项目部署
#### 
**1.环境准备**
 - jdk 1.8
 - mysql
#### 
**2.部署配置**
 - 在mysql中创建数据库may-starter
1. tomcat运行
 - 将may-starter.war放在Tomcat的webapp下
 - may-starter.war，修改数据库配置，路径如下：
`` may-starter/WEB-INF/classes/application-prod.yml``
 - 运行tomcat
 
2. 命令行运行
 - 以jar包形式进行maven打包
 - 拷贝jar包和配置文件 application-prod.yml
 - 修改配置文件内容
    
    数据库地址、日志文件地址
    
 - 添加启动脚本 start.sh
    ```
      #!/bin/bash
      basePath="/home/may-starter"
      nohup java -jar -Xms512m -Xmx2048m  $basePath/may-starter.jar --spring.profiles.active=prod >/dev/null 2>&1 &
      echo $!>$basePath/stop.pid
    ```
 - 添加关闭脚本 shutdown.sh
    ```
    #!/bin/bash
    basePath="/home/may-starter"
    cat $basePath/stop.pid | while read pid
    do
        echo 'kill' $pid
        kill $pid
    done
    rm -f $basePath/stop.pid
    ```
 - 执行启动脚本
 
swagger地址：http://localhost:8081/may-starter/swagger-ui.html

说明：
 XxxCtrl: 提供返回页面的接口和formdata参数接口，供freemarker页面开发使用
 XxxRestCtrl：提供rest接口，接收和返回的数据均为JSON格式，可通过swagger查看接口文档、调试接口