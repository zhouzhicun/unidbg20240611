
## DataSearch使用

DataSearch使用上非常简单，在 loadlibaray 前添加下面一行代码即可。
```text
new DataSearch(Emulator<?> emulator, String data);
```

其中 data 是你所需要搜索内容的 hexstring 形式，加不加空格都行：
```text
new DataSearch(emulator, "cd 8e ec");
new DataSearch(emulator, "cd8eec");
```

比如这里我们搜索 Key
new DataSearch(emulator, "cd8eec8a99167f9a03ce8f2df5e147f2b97d763f35d42126d193247fd67640a1");


DataSearch 默认的搜索频率是每 100我们这个样本的指令流只有 100w 行不到，所以速度挺快。
如果读者处理千万级或亿级的指令流，那么可以使用 DataSearch 的另外一个构造函数：
```text
new DataSearch(Emulator<?> emulator, String data, int interval);
```
interval 用于指定每隔多少个基本块做一次内存检索，建议千万级指令流选择 500 左右，亿级指令流选择 2000 左右。
检索频率越高，搜索的准确度越高，越可能发现数据的第一生成现场；检索越稀疏，搜索的耗时就越小，但可能会错过第一生成现场。因此，我们需要在性能和准确度之间找个均衡点。


除了搜索某个指定字节数组外，DataSearch 稍加修改，可以用于搜索 Key、字符串等等，
比如下面是 StringSearch，它会打印内存中出现过的所有可见字符串。


## StringSearch使用

StringSearch使用上也很简单，参数 1 是模拟器实例，参数 2 是可见字符串的最短长度，参数 3 是输出的文件路径。
```text
public StringSearch(Emulator<?> emulator, int minLength, String outputFile)
```
它的默认搜索频率是 100 个基本块，可以使用它的重载方法，修改这个值。
```text
public StringSearch(Emulator<?> emulator, int minLength, int interval, String outputFile)
```
具体使用上，比如下面这样
```text
new StringSearch(emulator, 5,"unidbg-android/src/test/resources/lamodo/trace/strings.log");
```
