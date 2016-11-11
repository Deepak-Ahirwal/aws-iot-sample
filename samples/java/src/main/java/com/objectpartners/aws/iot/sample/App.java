package com.objectpartners.aws.iot.sample;

import com.objectpartners.aws.iot.sample.device.SampleDevice;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class.
 */
public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		log.info("Starting sample application");
		ClientConfig clientConfig = parseClientConfig(args);
		if (clientConfig != null) {
			try {
				SampleDevice sampleDevice = new SampleDevice(clientConfig);
				sampleDevice.init();
			} catch (Exception e) {
				log.error("Unable to initialize sample device.", e);
			}
		}
	}

	/**
	 * Parses a client config from the command line options. If help option is requested, a null config is returned.
	 */
	private static ClientConfig parseClientConfig(String[] args) {

		ClientConfig clientConfig = null;
		Options options = initCliOptions();
		// create the parser
		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("options", options);
			} else {
				clientConfig = new ClientConfig();
				clientConfig.certificateFile = line.getOptionValue("cert");
				clientConfig.clientEndpoint = line.getOptionValue("endpoint");
				clientConfig.clientId = line.getOptionValue("clientId");
				clientConfig.privateKeyFile = line.getOptionValue("key");
			}
		} catch (ParseException e) {
			log.error("Parsing failed.  Reason: " + e.getMessage(), e);
		}
		return clientConfig;
	}

	private static Options initCliOptions() {
		return new Options()
				.addOption(
						Option.builder("cert")
								.hasArg()
								.argName("cert")
								.desc("AWS IoT certificate file")
								.build()
				).addOption(
						Option.builder("key")
								.hasArg()
								.argName("key")
								.desc("AWS IoT private key file")
								.build()
				).addOption(
						Option.builder("endpoint")
								.hasArg()
								.argName("endpoint")
								.desc("AWS IoT MQTT endpoint")
								.build()
				).addOption(
						Option.builder("clientId")
								.hasArg()
								.argName("clientId")
								.desc("AWS IoT MQTT clientId")
								.build()
				).addOption(
						Option.builder("h")
								.longOpt("help")
								.desc("Prints this help message")
								.build()
				);
	}

}
