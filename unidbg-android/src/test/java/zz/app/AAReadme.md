
补环境规范：
● 基本类型直接传递
● 字符串、字节数组等基本对象直接传递，其内部会做封装，也可以自己调用new StringObject(vm, str)、new ByteArray(vm, value)等。
● JDK 标准库对象，如 HashMap、JSONObject 等，使用ProxyDvmObject.createObject(vm, value)处理。
● 非 JDK 标准库对象，如 Android Context、SharedPreference 等，使用vm.resolveClass(vm，className).newObject(value)处理。


问题1：
NativeA方法里调用另一个 NativeB方法，这属于嵌套调用，Unidbg 暂不支持这么做， 解决方法：
1.首先调用NativeB方法，得到结果；
2.调用NativeA方法时，提示需要补NativeB方法，这时候直接返回先前调用NativeB得到的结果即可。