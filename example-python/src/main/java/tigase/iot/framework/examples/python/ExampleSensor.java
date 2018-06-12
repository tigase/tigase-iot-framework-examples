/*
 * ExampleSensor.java
 *
 * Tigase IoT Framework - Examples
 * Copyright (C) 2011-2018 "Tigase, Inc." <office@tigase.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 */
package tigase.iot.framework.examples.python;

import tigase.iot.framework.devices.AbstractSensor;
import tigase.iot.framework.devices.IConfigurationAware;
import tigase.iot.framework.devices.IValue;
import tigase.kernel.beans.config.ConfigField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// T needs to be a class representing a value returned by this sensor. If should be one of the following list of classes
// (available in tigase.iot.framework.values package) for supported value types:
// Humidity
// Light
// Movement
// Pressure
// Temperature
public class ExampleSensor<T extends IValue>
		extends AbstractSensor<T>
		implements IConfigurationAware {

	// This is a configuration option supported by the driver. Its value may be changed during runtime by the user
	// using remote client. You may have more than one configuration field. There is a support for fields of type:
	// long/Long, int/Integer and String.
	//
	// If value of the configuration field is changed and there is a setter for this field it will be called but it
	// is then a developers responsibility to update configuration field value.
	//
	// If your listener needs to react on change of this field value, please create a setter for this field.
	@ConfigField(desc = "Some parameter which can be configured and passed to the python script")
	private String parameter = "12";

	// Path to the python script which should be periodically executed.
	private String script = "/home/pi/script.py";
	private Thread thread = null;
	private Process process = null;

	public ExampleSensor() {
		// Type and name variables need to be filled with device type id and corresponding device type name. Those can
		// be one of the pairs from the following list:
		//
		// type = "humidity-sensor"; name = "Humidity sensor";
		// type = "light-sensor"; name = "Light sensor";
		// type = "movement-sensor"; name = "Motion sensor";
		// type = "pressure-sensor"; name = "Pressure sensor";
		// type = "temperature-sensor"; name = "Temperature sensor";
		//
		// Label should be a name of the actual sensors used to read data, ie. BH1750
		super(null, null, null);
	}

	@Override
	public void initialize() {
		super.initialize();
		// here you should initialize your mechanism for listening on the events from the sensor.
		startPythonProcess();
	}

	@Override
	public void beforeUnregister() {
		stopPythonProcess();
		super.beforeUnregister();
	}

	// This method will be called by the separate thread whenever python process will provide any line to the output stream.
	private void handleEvent(String line) {
		// Please convert provided value to the class representing value returned by this sensor
		// (for class name please check documentation of this class at the top of the class definition) and call
		// updateValue() method passing newly read value.
		//
		// null is not an acceptable value!
		this.updateValue(null);
	}

	private void startPythonProcess() {
		synchronized (this) {
			thread = new Thread(this::startProcess);
			thread.start();
		}
	}

	private void stopPythonProcess() {
		synchronized (this) {
			if (process != null) {
				process.destroy();
			}
			if (thread != null) {
				thread.interrupt();
			}
		}
	}

	private void startProcess() {
		// Start python script with provided parameters
		execute(script, parameter);
	}

	// Execute python script and wait for the result
	protected void execute(String script, String... args) {
		try {
			// In case of Python you need to spawn a python process and read its output and convert it to instance of IValue class.
			Stream<String> command = Stream.of("python", script);
			if (args != null) {
				command = Stream.concat(command, Arrays.stream(args));
			}
			synchronized (this) {
				process = new ProcessBuilder(command.collect(Collectors.toList())).start();
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					handleEvent(line);
				}
			}
			int result = process.waitFor();
		} catch (Exception ex) {
			throw new RuntimeException("Python process stopped", ex);
		}
	}
}
