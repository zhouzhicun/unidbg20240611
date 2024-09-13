## uname获取
我们可以在 adb shell 中获取除了 domainname 之外的信息。
```text

polaris:/ $ uname --help
usage: uname [-asnrvm]

Print system information.

-s      System name
-n      Network (domain) name
-r      Kernel Release number
-v      Kernel Version
-m      Machine (hardware) name
-a      All of the above

polaris:/ $ uname -s
Linux
polaris:/ $ uname -n
localhost
polaris:/ $ uname -r
4.9.186-perf-gd3d6708
polaris:/ $ uname -v
#1 SMP PREEMPT Wed Nov 4 01:05:59 CST 2020
polaris:/ $ uname -m
aarch64
polaris:/ $ uname -a
Linux localhost 4.9.186-perf-gd3d6708 #1 SMP PREEMPT Wed Nov 4 01:05:59 CST 2020 aarch64

```


