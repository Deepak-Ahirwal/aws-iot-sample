package com.objectpartners.aws.iot.sample;

/**
 * Client connection config.
 */
public class ClientConfig {

	public String clientEndpoint;
	public String clientId;
	public String certificateFile;
	public String privateKeyFile;

	@Override public String toString() {
		return "ClientConfig{" +
				"clientEndpoint='" + clientEndpoint + '\'' +
				", clientId='" + clientId + '\'' +
				", certificateFile='" + certificateFile + '\'' +
				", privateKeyFile='" + privateKeyFile + '\'' +
				'}';
	}
}
