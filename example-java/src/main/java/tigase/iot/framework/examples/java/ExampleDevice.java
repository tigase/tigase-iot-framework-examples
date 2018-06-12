/*
 * ExampleDevice.java
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
import tigase.iot.framework.devices.IExecutorDevice;
import tigase.iot.framework.devices.IValue;
import tigase.kernel.beans.config.ConfigField;

public class ExampleDevice<T extends IValue>
		extends AbstractSensor<T>
		implements IConfigurationAware, IExecutorDevice<T> {

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

	public ExampleDevice() {
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
	}

	@Override
	public void beforeUnregister() {
		super.beforeUnregister();
	}

	@Override
	public void setValue(T value) {
		// here you should react on the value you received (paramter `value`) and using this value modify state of the
		// physical device controlled by this driver
		//
		// If device state is changed properly call:
		updateValue(value);
		// to notify hub that value was changed
		//
		// If it was not possible to change value of the physical device, then do nothing.
		// If value was changed but adjustment of value was required, then call updateValue() and as a parameter pass
		// actual value representing current device state.
	}
}
