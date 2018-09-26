package junit.org.rapidpm.event.exentra.tinkerforge;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rapidpm.event.exentra.tinkerforge.Demo;

public class DemoTest {


  @Test
  @DisplayName("ich bin super wichtig !!")
  void test001() {
    String demo = new Demo().demo();
    Assertions.assertEquals("HelloWorld", demo);
  }
}
