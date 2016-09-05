package com.objectpartners.aws.iot.sample.device;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.objectpartners.aws.iot.sample.ClientConfig;
import com.objectpartners.aws.iot.sample.mqtt.SampleConsumer;
import com.objectpartners.aws.iot.sample.mqtt.SamplePubisher;
import com.objectpartners.aws.iot.sample.shadow.SampleShadowDevice;
import com.objectpartners.aws.iot.sample.utils.CertificateUtils;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample device demonstrating basic AWS IoT functionality.
 */
public class SampleDevice {

	private static final Logger log = LoggerFactory.getLogger(SampleDevice.class);

	private final ClientConfig clientConfig;

	private final ScheduledExecutorService executor;

	private AWSIotMqttClient awsIotMqttClient;

	private SampleShadowDevice sampleShadowDevice;

	private ScheduledFuture<?> publisherTask;

	public SampleDevice(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	public void init() throws AWSIotException {

		log.info("Initializing mqtt connection with config {}", clientConfig);
		CertificateUtils.KeyStorePasswordPair pair = CertificateUtils.getKeyStorePasswordPair(
				clientConfig.certificateFile, clientConfig.privateKeyFile
		);
		awsIotMqttClient = new AWSIotMqttClient(
				clientConfig.clientEndpoint, clientConfig.clientId, pair.keyStore, pair.keyPassword
		);

		// create and attach the shadow device
		this.sampleShadowDevice = new SampleShadowDevice(clientConfig.clientId, this::onShadowUpdate);
		awsIotMqttClient.attach(sampleShadowDevice);

		// connect
		awsIotMqttClient.connect();
		log.info("Successfully connected to AWS IoT");

		// start consumer
		new SampleConsumer(clientConfig.clientId, awsIotMqttClient).subscribe();
	}

	private synchronized void onShadowUpdate() {

		// cancel the existing task
		if (publisherTask != null) {
			log.info("Shadow update occurred. Cancelling existing publisher task.");
			publisherTask.cancel(true);
		}

		// start publishing with updated config
		int minSpeed = sampleShadowDevice.getMinSpeed();
		log.info("Received device shadow configuration update. Starting publisher with minSpeed:{}", minSpeed);
		publisherTask = executor.scheduleWithFixedDelay(new SamplePubisher(
				clientConfig.clientId,
				awsIotMqttClient,
				minSpeed
		), 0, 1000, TimeUnit.MILLISECONDS);
	}

	public void shutdown() throws AWSIotException {

		log.info("shutting down");
		awsIotMqttClient.disconnect();
		executor.shutdown();
	}

}
