# web3
## 环境设置
### .env文件
在项目根目录下创建```.env```文件。\
设置助记词，比如：\
```DEFAULT_MNEMONIC=a b c d e f g h i g k l```

### 公链rpc代理
okc的rpc链接需要设置代理才能访问，在host文件中进行如下配置：\
测试网：```170.33.1.92	exchaintestrpc.okex.org``` \
正式网：```170.33.1.92	exchainrpc.okex.org```

## 工具
### XEN批量mint
使用EOA账号：xen.contract.xin.yukino.web3.util.XEN \
使用合约账号：xen.contract.xin.yukino.web3.util.XENFT

### 创建代理合约
EIP1167：eip.contract.xin.yukino.web3.util.EIP1167
