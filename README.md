# unidbg



简介：

该仓库于2024.6.11 fork自如下仓库地址：https://github.com/zhkl0228/unidbg

目的：便于更好的定制，从而提高工作效率。



##  项目规范：

1.所有项目放在unidbg-android/src/test/java目录的com.zz.app包下，每个项目的代码文件以及apk,so等资源文件放在同一个包下，方便管理；

2.所有项目继承BaseAbstractJni基类，复用初始化代码，简化开发提高效率。



## 新增基础功能：

* DataSearch： 用于内存中搜索指定数据；来源：https://www.yuque.com/lilac-2hqvv/glw3nr/kwdrl60zsxaxo4gh

* AesKeyFinder: 用于内存中搜索AES加密算法的key；来源：https://github.com/Pr0214/Unidbg_FindKey/ 

* 新增ZZFixConfig配置文件，用于通过开关固定随机因子，默认全部固定；文件位于 unidbg-api/src/main/java目录的 com.github.unidbg.zz 包下；



## 修改点：

修改点有两处，分别位于：

1.unidbg-api/src/main/java/com/github.unidbg/zz

2.unidbg-android/src/main/test/java/zz.app/base







