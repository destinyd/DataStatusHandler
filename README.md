Android DataStatusHandler
=================
数据状态切换组件 

## 如何引用此组件：
### 安装
```
git clone https://github.com/mindpin/DataStatusHandler
cd DataStatusHandler
mvn clean install
```

### maven引用
在maven项目，pom.xml添加以下依赖引用：

```
<dependency>
  <groupId>com.mindpin.android.datastatushandler</groupId>
  <artifactId>datastatushandler</artifactId>
  <version>0.1.0-SNAPSHOT</version>
  <type>apklib</type>
</dependency>
```

### android权限设置
AndroidManifest.xml 添加如下权限
```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

## 使用说明
请参考示例

