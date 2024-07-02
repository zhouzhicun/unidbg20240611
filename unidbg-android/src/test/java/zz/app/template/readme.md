
1.构造函数调用：

```
//构造参数，需要转换为unidbg DvmObject类型, 例如StringObject, ByteArray等。
StringObject param1 = new StringObject(vm, "12345");
ByteArray param2 = new ByteArray(vm, "r0ysue".getBytes(StandardCharsets.UTF_8));

//添加到list时，需要调用vm.addLocalObject()， 相当于将DvmObject类型转换为对象句柄，
List<Object> params = createParams();
params.add(vm.addLocalObject(param1));
params.add(vm.addLocalObject(param2));
Number number = callFunc(0x4a28D, params);

```


2.补环境
直接返回 unidbg DvmObject类型即可, 例如StringObject, ByteArray等。

