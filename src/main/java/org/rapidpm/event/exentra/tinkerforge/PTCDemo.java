package org.rapidpm.event.exentra.tinkerforge;

import com.tinkerforge.BrickletPTC;
import com.tinkerforge.BrickletPTCV2;
import com.tinkerforge.IPConnection;

public class PTCDemo {

  private static final String HOST = "localhost";
  private static final int PORT = 4223;

  private static final String UID = "i2J";

  // Note: To make the example code cleaner we do not handle exceptions. Exceptions
  //       you might normally want to catch are described in the documentation
  public static void main(String args[]) throws Exception {
    IPConnection ipcon = new IPConnection(); // Create IP connection
    BrickletPTC  ptc   = new BrickletPTC(UID, ipcon); // Create device object

    ipcon.connect(HOST, PORT); // Connect to brickd
    // Don't use device before ipcon is connected

    // Add temperature listener
    ptc.addTemperatureListener(new BrickletPTC.TemperatureListener() {
      public void temperature(int temperature) {
        System.out.println("Temperature: " + temperature/100.0 + " °C");
      }
    });

    // Set period for temperature callback to 1s (1000ms) without a threshold
    ptc.setTemperatureCallbackPeriod(1000);

    System.out.println("Press key to exit");
    System.in.read();
    ipcon.disconnect();
  }
}
