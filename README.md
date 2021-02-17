# apache beam gradle sample

## Dataflowとは
Apache Beam がサポートする分散処理バックエンドのGCP版

データの並列処理パイプラインを構築するのがとても楽

https://beam.apache.org/

## Apache Beam
### Apache Beamの概要
* https://beam.apache.org/documentation/programming-guide/

### SDKの選定
Javaを使います。

他にGoやpythonもありますが、型があること、Javaが優先されてサポートされている雰囲気からJavaにしています。

Goは最近サポートされ始めたこともありなにか踏みそうな予感もしているので避けています


## Deploy to Dataflow

### Create Pipeline
```
./gradlew run \
  -Penv=foo \
  -PotherProject=bar \
  -Pgcp.project=baz \
  -PnumWorkers=1 \
  -PmaxNumWorkers=1 \
  -PserviceAccount=foobar@baz.iam.gserviceaccount.com \
  -PworkerMachineType=n1-standard-1
```
### Update Pipeline
```
./gradlew run \
  -Penv=foo \
  -PotherProject=bar \
  -Pgcp.project=baz \
  -PnumWorkers=1 \
  -PmaxNumWorkers=1 \
  -PserviceAccount=foobar@baz.iam.gserviceaccount.com \
  -PworkerMachineType=n1-standard-1
  -Pupdate=true
```

## Reference
* Gladle dependency locking
  * http://blog.64p.org/entry/2020/05/13/100039
* JSON to Java
  * http://www.jsonschema2pojo.org/       