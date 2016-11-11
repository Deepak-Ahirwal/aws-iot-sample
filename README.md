# aws-iot-sample

## Build
`gradlew shadowJar`

## Run

```
java -jar build/libs/java-all.jar \
 -cert <path-to-cert>
 -key <path-to-key> \
 -clientId <device-name> \
 -endpoint <aws-iot-endpoint>
```
