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
package tigase.iot.framework.examples.java;

import tigase.iot.framework.devices.AbstractSensor;
import tigase.iot.framework.devices.IConfigurationAware;
import tigase.iot.framework.devices.IValue;
import tigase.kernel.beans.config.ConfigField;

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
	@ConfigField(desc = "Some additional configuration option")
	private Integer value = 12;
	
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
	}

	@Override
	public void beforeUnregister() {
		// here you should deinitialize you mechanism for listening on the events from the sensor.
		super.beforeUnregister();
	}

	// This is a dummy method. It should be called by your code reacting on the call from the listener which you
	// initialized in the initialize() method.
	private void handleEvent() {
		// After you get value from the call please convert it to the class representing value returned by this sensor
		// (for class name please check documentation of this class at the top of the class definition) and call
		// updateValue() method passing newly read value.
		//
		// null is not an acceptable value!
		this.updateValue(null);
	}
}
