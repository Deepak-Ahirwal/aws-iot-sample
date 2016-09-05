package com.objectpartners.aws.iot.sample.shadow;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AwsIotDevice implementation demonstrating basic shadow functionality.
 */
public class SampleShadowDevice extends AWSIotDevice {

	private ShadowUpdateListener shadowUpdateListener;

	public SampleShadowDevice(String thingName, ShadowUpdateListener shadowUpdateListener) {
		super(thingName);
		this.shadowUpdateListener = shadowUpdateListener;
	}

	@AWSIotDeviceProperty
	private volatile int minSpeed;

	public int getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	@Override public void onShadowUpdate(String jsonState) {
		super.onShadowUpdate(jsonState);
		shadowUpdateListener.onUpdate();
	}
}
