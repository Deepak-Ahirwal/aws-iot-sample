package com.objectpartners.aws.iot.sample.shadow;

/**
 * Invoked when shadow updates occur.
 */
public interface ShadowUpdateListener {

	void onUpdate();
}
