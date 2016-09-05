package com.objectpartners.aws.iot.sample.mqtt;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample MQTT consumer.
 */
public class SampleConsumer {

	private static final Logger log = LoggerFactory.getLogger(SampleConsumer.class);

	private final String clientId;
	private final AWSIotMqttClient awsIotMqttClient;

	public SampleConsumer(String clientId, AWSIotMqttClient awsIotMqttClient) {
		this.clientId = clientId;
		this.awsIotMqttClient = awsIotMqttClient;
	}

	public void subscribe() throws AWSIotException {

		log.info("Subscribing to sample topic.");
		AWSIotTopic topic = new AWSIotTopic("sample/" + clientId, AWSIotQos.QOS1) {

			@Override public void onMessage(AWSIotMessage message) {

				log.debug("Received message: {}", message.getStringPayload());
			}
		};
		awsIotMqttClient.subscribe(topic);
	}

}
