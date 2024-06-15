## 项目介绍

#### Emos是一个前后端分离的在线办公系统，项目采用 SpringBoot+Uniapp 开发，项目加入常见的企业级应用所涉及到的技术点，例如 Redis、RabbitMQ 等。前端使用Uniapp开发微信小程序，员工可以在微信中通过小程序登录，实现在线办公。

### 项目技术栈

#### 后端技术栈

```text
1. SpringBoot
2. Shiro
3. MyBatis
4. MySQL
5. Redis
6. RabbitMQ
7. MongoDB
8. JWT
9. 腾讯云API(人脸识别 对象存储)
10. ...
```

#### 前端技术栈

```text
1. Uniapp
2. 微信小程序
3. ...
```

### 小程序已上线

![小程序](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/wecatapp_qr_2.jpg)

### 项目效果图

![主页tab1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_tab1.jpeg)
![主页tab2](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_tab2.jpeg) 
![主页tab3](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_tab3.jpeg)

![主页tab4](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_tab4.jpeg)
![主页tab5](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_tab5.jpeg) 
![签到1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_check1.jpeg)

![签到2](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_check2.jpeg) 
![签到3](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_check3.jpeg)
![会议1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_meet1.jpeg)

![会议1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_meet2.jpeg) 
![会议1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_meet3.jpeg) 
![会议1](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/emos_meet4.jpeg)

# 配置手册

## 开发环境的安装

### 0.brew 安装

```shell
/bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"
```

### 1.MySQL

#### 安装

#### 1.去下载[MySQL](https://dev.mysql.com/downloads/mysql/) 的安装包，点击安装

#### 2.安装过程中 (1) 安装类型为:ServerOnly (2) 选择账户密码验证方式:选择传统模式(Legacy)

#### 3.使用Navicat连接mysql,新建数据库为"emos"，运行emos.sql导入表和数据。

```text
  "name": "mysql",
  "port": 3306,
  "user_name": "root",
  "pwd": "安装时候设置的密码"
```

### 2.MongoDB

#### 安装

```shell
brew tap mongodb/brew
brew install mongodb-community
```

#### 启动

```shell
brew services start mongodb/brew/mongodb-community
```

#### 终端中使用输入

```shell
mongo
```

#### 输入以下命令，创建用户

```text
use admin
db.createUser({
    user:"xxx",
    pwd:"xxx",
    roles:[{
        role:"xxx",
        db:"xxx"
    }]

})
```

### 3.Redis

#### 安装

```shell
brew install redis
```

##### 安装目录

```text
/usr/local/Cellar/redis/6.2.1
```

#### 配置文件目录

```text
/usr/local/etc/redis.conf
```

#### 修改密码

打开redis.conf文件，搜索 "requirepass" ,修改密码

#### 启动

```shell
redis-server
```

#### or

```shell
brew services start redis
```

### 4.RabbitMq

#### 安装

```shell
brew install rabbitmq
```

#### 启动

#### 方式1(退出终端界面,rabbitmq就终止服务了)

```shell
rabbitmq-server
```

#### 全路径为

```shell
/usr/local/Cellar/rabbitmq/3.8.14/sbin/rabbitmq-server
```

#### 方式2(后台运行)

```shell
brew services start rabbitmq
```

```text
  "name": "rabbitmq",
   "url":"http://localhost:15672",
  "port": 15672,
  "user_name": "guest",
  "pwd": "guest"
```

### 5.Maven配置

#### 1.下载需要的 [maven](http://maven.apache.org/download.cgi) 压缩包,解压进入目录

#### 2.新建一个文件夹为"repository",

#### 3.打开conf/settings.xml,查找"<localRepository></localRepository>",将目录设置为刚创建的repository文件夹的地址

#### 4.在conf/settings.xml中配置阿里云的镜像，在<mirrors></mirrors>插入如下：

```xml
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
    </mirror>
    <mirror>
       <id>alimaven</id>
       <mirrorOf>central</mirrorOf>
       <name>aliyun maven</name>
       <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
    </mirror>
```

### 6.下载IDEA安装并配置

#### 1.maven配置:打开Preferences界面，找到Build Tools-Maven,设置如下三个地方

```text
Maven home directory: maven目录
User settings file:  maven中conf/setting.xml的位置，勾选Override
Local repository:  自定义repository文件夹位置，勾选Override
```

#### 2.插件安装：打开Preferences界面，找到Plugins,选择MarketPlace,搜索如下插件安装

```text
Lombok                 主要用于生成SET/GET方法
Free MyBatis plugin    主要用于生成MyBatis相关映射文件
```

### 7.微信开发者工具安装

#### 1.下载[微信开发者工具](https://q.qq.com/wiki/tools/devtool/)

#### 2.打开微信开发者工具，找到 "工具-安全"，将"服务端口"打开，这里是为了HBuilder打开微信开发者工具

### 8.HBuilder安装

#### 下载[HBuilder](https://www.dcloud.io/hbuilderx.html)

## 服务器部署(CenOS)

### 1.Docker

#### 安装

```shell
yum install docker -y
```

#### 配置镜像

```text
vim /ect/docker/daemon.json
```

#### 配置内容

```json
{
  "registry-mirrors": ["https://mirror.ccs.tencentyun.com"]
}
```

#### 启动

```shell
service start docker
```

### 2.安装MySQL

#### 安装

```shell
docker pull mysql:8.0.22
```

#### 启动

```shell
 docker run -it -d --name mysql --net=host -m 500m -v /root/mysql/data:/var/lib/mysql -v /root/mysql/config:/etc/mysql/conf.d  -e MYSQL_ROOT_PASSWORD=xxx -e TZ=Asia/Shanghai mysql:8.0.22 
```

### 2.安装Mongo

#### 安装

```shell
brew pull mongo
```

#### 配置:创建 /root/mongo/mongod.conf,在文件中配置如下内容

```shell
mkdir -p /root/mongo
vim /root/mongo/mongod.conf
```

```text
net: 
    port: 27017
    bindIp: "0.0.0.0"
storage: 
    dbPath: "/data/db"
security:  
    authorization: enabled
```

#### 启动

```shell
docker run -it -d --name mongo --net=host -v /root/mongo:/etc/mongo -m 500m -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=xxx mongo --config /etc/mongo/mongod.conf
```

### 3.安装Redis

#### 安装

```shell
brew pull redis:6.0.10
```

#### 配置:创建 /root/redis/redis.conf,在文件中配置如下内容

```shell
mkdir -p /root/redis/conf/
vim  mkdir -p /root/redis/conf/redis.conf
```

```text
bind 0.0.0.0
protected-mode yes
port 6379
tcp-backlog 511
timeout 0 6. tcp-keepalive 0 7. loglevel notice
logfile ""
databases 4
save 900 1
save 300 10
save 60 10000
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump.rdb
dir ./
requirepass xxx
```

#### 启动

```shell
docker run -it -d --name redis -m 300m  --net=host -v /root/redis/conf:/usr/local/etc/redis redis:6.0.10 redis-server /usr/local/etc/redis/redis.conf
```

### 4.安装RabbitMQ

#### 安装

```shell
brew pull rabbitmq
```

#### 启动

```shell
 docker run -it -d --name mq -m 300m --net=host rabbitmq
```

### 5.JDK安装

#### 去 https://hub.docker.com/_/openjdk?tab=tags 选择与开发环境对应的jdk版本进行安装

```shell
docker pull oopenjdk:11.0.9.1-oraclelinux7
#创建新的镜像引用
docker tag openjdk:11.0.9.1-oraclelinux7 jdk11
#删除原有镜像引用
docker rmi openjdk:11.0.9.1-oraclelinux7
```

### 6.SpringBoot项目打包上传

#### 打包

```text
点击右侧 Maven 面板中 Lifecycle-Install 进行打包,生成的jar包位置在 target 文件夹下，将jar文件重命名为 emos-wx-api.jar
```

### 上传jar包到服务器

```shell
scp emos-wx-api.jar  name@ip:/root/emos/
```

#### 运行jar

```shell
nohup  java -jar emos-wx-api.jar  >> out.log 2>&1 &
```

### 7.Nginx安装

#### 安装

```shell
rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7- 0.el7.ngx.noarch.rpm
#执行安装
yum install nginx -y
 #启动Nginx
service nginx start
```

#### 配置配置https证书

#### 下载域名证书文件

```text
1_域名_bundle.crt
2_域名_key
```

#### 上传到 /ect/nginx目录下

```shell
scp 1_域名_bundle.crt  name@ip:/etc/nginx
scp 2_域名_key name@ip:/etc/nginx
```

#### 配置nginx.conf,插入以下内容

```shell
vim /etc/nginx/nginx.conf
```

#### 注意 root用来配置主页的目录，两个server里面都需要配置root,这里设置主页地址为 /home目录

```text
 server {
        #SSL 访问端口号为 443
        listen 443 ssl;
        #填写绑定证书的域名
        server_name 域名;
         #证书文件名称
        ssl_certificate 1_域名.crt;
        #私钥文件名称
        ssl_certificate_key 2_域名.key;
        ssl_session_timeout 5m;
        #请按照以下协议配置
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        #请按照以下套件配置，配置加密套件，写法遵循 openssl 标准。
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
        ssl_prefer_server_ciphers on;
        root    /home ;
        location / {
            root  /home;
            index  index.html index.htm;
        }
        location /emos-wx-api {
            proxy_pass http://localhost:端口号/emos-wx-api;
        }

    }
```

#### 重启生效

```shell
service nginx start
```

#### 如果您觉得我帮你节省了大量的开发时间，请扫描下方的二维码随意打赏，要是能打赏个 10.24 🐵就太👍了。您的支持将鼓励我继续进行分享。
![](https://raw.githubusercontent.com/VincentTung/emos_online/master/art/wechat.png)
