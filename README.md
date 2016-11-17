# aws-iot-sample

The aws-iot-sample project includes a sample IoT device application to complement the presentation [Building Scalable IoT Applications with AWS IoT](http://com.objectpartners.presentations.s3-website-us-east-1.amazonaws.com/aws-iot).

## Java Sample
The sample is written in Java and uses the [Java AWS IoT Device SDK](https://github.com/aws/aws-iot-device-sdk-java).

### Build
To build the sample, execute the following from the directory `aws-iot-sample/samples/java`.

`gradlew shadowJar`

The build generates the file `aws-iot-sample/samples/java/build/libs/java-all.jar` containing the source and required dependencies.

### Running the Sample
Before running the sample, first setup a valid device within AWS IoT. For instructions on setting up a device see the AWS documentation [Getting Started with AWS IoT](http://docs.aws.amazon.com/iot/latest/developerguide/iot-gs.html).

In addition, setup a `minSpeed` attribute with an int value on the device shadow for the device. For example:
```
{
  "desired": {
    "minSpeed": 20
  },
  "reported": {
    "minSpeed": 20
  }
}
```
Once the device is setup, use the certificate, private key, clientId and AWS IoT endpoint to execute the sample as follows.
```
java -jar aws-iot-sample/samples/java/build/libs/java-all.jar \
 -cert <path-to-cert>
 -key <path-to-key> \
 -clientId <device-name> \
 -endpoint <aws-iot-endpoint>
```

The sample application will perform the following.
* Create and initialize a new [SampleDevice](samples/java/src/main/java/com/objectpartners/aws/iot/sample/device/SampleDevice.java).
* Retrieve the `minSpeed` value from the device shadow.
* Publish random speed values greater than the `minSpeed` to the topic `speed/<clientId>` at a rate of one per second.
* Subscribe to messages with the topicFilter `sample/<clientId>`.
* Subscribe to device shadow `minSpeed` updates via [SampleShadowDevice](samples/java/src/main/java/com/objectpartners/aws/iot/sample/shadow/SampleShadowDevice.java).
