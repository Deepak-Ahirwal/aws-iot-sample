package com.objectpartners.aws.iot.sample.mqtt;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample Runnable that publishes random speed data via MQTT.
 */
public class SamplePubisher implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SamplePubisher.class);

	private final String clientId;
	private final String speedTopic;
	private final AWSIotMqttClient awsIotMqttClient;
	private final Random random = new Random();
	private final int minSpeed;

	public SamplePubisher(String clientId, AWSIotMqttClient awsIotMqttClient, int minSpeed) {
		this.clientId = clientId;
		this.awsIotMqttClient = awsIotMqttClient;
		this.minSpeed = minSpeed;
		this.speedTopic = "speed/" + clientId;
	}

	@Override public void run() {

		try {
			int speed = random.nextInt(100);
			if (speed >= minSpeed) {
				String payload = "{\"speed\": " + speed + "}";
				awsIotMqttClient.publish(speedTopic, AWSIotQos.QOS1, payload);
				log.debug("Successfully published device speed: {} minSpeed: {}", speed, minSpeed);
			} else {
				log.debug("Skipping publish. Device speed: {} < minSpeed: {}", speed, minSpeed);
			}
		} catch (Exception e) {
			log.warn("Exception publishing device speed. " + e.getMessage());
		}
	}

}
