# java-rpc

Java实现rpc远程调用

+ 服务端启动类（先启动服务端）：

> com.cxxwl96.server.ServerRun

```text
[2022-05-14 03:27:00.812] [INFO] com.cxxwl96.rpc.core.RpcServer [main]: Rpc server start in 5009
[2022-05-14 03:27:00.839] [INFO] com.cxxwl96.rpc.core.RpcServer [main]: Loaded RPC service => interface: com.cxxwl96.api.UserService, implement: com.cxxwl96.server.UserServiceImpl
[2022-05-14 03:27:42.236] [INFO] com.cxxwl96.server.UserServiceImpl [pool-1-thread-1]: save: User(name=cxxwl96, age=24)
```

+ 客户端启动类（再启动客户端）：

> com.cxxwl96.client.ClientRun

```text
[2022-05-14 03:27:42.248] [INFO] com.cxxwl96.client.ClientRun [main]: save user: true
[2022-05-14 03:27:42.254] [INFO] com.cxxwl96.client.ClientRun [main]: user list: [User(name=张三, age=20), User(name=李四, age=21), User(name=王五, age=22)]
```