

文件访问最主要服务于环境检测和信息收集，举例如下。
● 访问 /proc/self/maps 检测 frida/xposed 等模块
● 访问 /proc/net/tcp 检测 frida/ida server 默认端口
● 访问 /proc/self/cmdline 确认在自身进程内
● 访问 apkfile 做签名校验，解析资源文件等等
● 访问 /xbin/su 检测 root
● 访问 /data/data/package/xxx 读取自身相关文件
Unidbg 通过文件处理器处理文件。默认的文件处理器是 AndroidResolver，我们可以在它的 resolve 方法里处理样本所发起的文件访问。



当样本做环境检测相关的文件访问时，主要检测这些文件是否存在，以及是否有权限
● Root检测（检测 su、Magisk、Riru，检测市面上的 Root 工具）
● 模拟器检测（检测 Qemu，检测各家模拟器，比如夜神、雷电、Mumu 等模拟器的文件特征、驱动特征等）
● 危险应用检测（各类多开助手、按键精灵、接码平台等）
● 云手机检测 （以各种云手机产品为主）
● Hook框架（以 Xposed、Substrate、Frida 为主）
● 脱壳机（以 Fart、DexHunter、Youpk 三者为主）





获取 uname信息：（我们可以在 adb shell 中获取除了 domainname 之外的信息。）
uname --help
usage: uname [-asnrvm]
Print system information.
-s      System name
-n      Network (domain) name
-r      Kernel Release number
-v      Kernel Version
-m      Machine (hardware) name
-a      All of the above



