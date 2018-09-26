package org.rapidpm.event.exentra.tinkerforge;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NetworkException;
import com.tinkerforge.NotConnectedException;

import java.io.IOException;

public class GenericDemo {

  private static final String HOST = "localhost";
  private static final int    PORT = 4223;


  public static void main(String[] args) {

    IPConnection ipcon = new IPConnection(); // Create IP connection
    ipcon.addEnumerateListener((String uid, String connectedUid, char position, short[] hardwareVersion, short[] firmwareVersion, int deviceIdentifier, short enumerationType) -> {

      System.out.println("uid = " + uid);

    });

    try {
      ipcon.connect(HOST, PORT);

      ipcon.enumerate();

      System.out.println("Press key to exit");
      System.in.read();
      ipcon.disconnect();

    } catch (NetworkException | AlreadyConnectedException | NotConnectedException | IOException e) {
      e.printStackTrace();
    }

  }


}
