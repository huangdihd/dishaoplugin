# dishaoplugin

#### 介绍
dishao插件,轻量级的基于spigotapi1.16+的基础插件

#### 安装教程

1.  将插件放在支持spigotapi服务端根目录下的/plugins文件夹中
2.  重启服务端
3.  将一张64x64像素的图片放在服务端根目录/plugins/dishao/中(不能是ico)

#### 使用说明

1.  默认配置文件及解释:  
\#开启后输出的文字(没有其他意义)  
say: 在插件目录下的config.yml中的say:后面更改要输出的内容  
\#是否开启dishao插件权限管理模块  
permissions: true  
\#给予非op玩家的权限  
permissions-list:  
  \- dishao.help  
  \- dishao.tp  
  \- dishao.tell  
  \- dishao.out  
  \- dishao.zisha  
  \- bukkit.command.tps  
\#非op玩家关闭的权限  
off-permissions-list:  
  \- minecraft.command.seed
\#插件启动时检测更新  
detect-updates: true  
\#服务器标题  
server-motd:  
  \#服务器标题列表("%nl%"代表换行)  
  motd-list:  
      \- 第一行%nl%第二行  
  \#每个标题等待时间  
  motd-time: 3000L  
  \#加载标题输出  
  load-print: true  
  \#图标  
  motd-icon: favicon.png  
2.  权限列表:
  dishao.tp: 传送命令的权限   
  dishao.tell:  私聊命令的权限  
  dishao.help:  帮助命令的权限  
  dishao.out:  退出服务器命令的权限  
  dishao.zisha:  自杀命令的权限  
  dishao.kick:  踢出玩家的权限  
  dishao.main_command.info:  查看详情的权限  
  dishao.main_command:  主命令的权限  
  dishao.main_command.config:  命令更改配置文件的权限  
  dishao.main_command.reload:  重载插件的权限  
3.  命令列表  
/514:
    需要权限: "dishao.zisha"  
    命令名字:  
      \- "514"  
    命令详情: 自杀
/help:  
    需要权限: "dishao.help"  
    命令名字:  
      \- "help" 
    命令详情: 帮助  
/tell:  
    需要权限: "dishao.tell"  
    命令名字:  
      \- "w"  
      \- "tell"  
      \- "msg" 
    命令详情:私聊,/tell [玩家名字] [内容]  
/out:  
    需要权限: "dishao.out"  
    命令名字:  
     \- "out"  
     命令
    命令详情:退出服务器
/tp:  
    需要权限: "dishao.tp"  
    命令名字:
      \- "tp"  
    命令详情:传送,/tp [玩家名字]或/tp [x] [y] [z]   
/kick:  
    需要权限: "dishao.kick"  
    命令名字:
      \- "kick"  
    命令详情:踢出玩家,/kick [玩家名字] [内容]  
/dishao:  
    需要权限: "dishao.main_command"  
    命令名字:  
      \- "dishao"  
      \- "ds"  
    命令详情: 主命令,若没有参数或只有一个参数"info"则输出插件详情(需要权限"dishao.main_command.info"),若第一个参数是"reload"则重载插件(需要权限"dishao.main_command.reload"),若第一个参数是"config"更改配置文件,若第二个参数是数组,第三个参数输入"add"或者"remove",然后输入值来修改,若第二个参数是布尔类型变量,则第三个参数输入"true"或"false"(改完后不会立刻重载,需要权限"dishao.main_command.reload",所有主命令只要有权限"dishao.main_command都可以执行")

#### 参与贡献

DicloudStudio

