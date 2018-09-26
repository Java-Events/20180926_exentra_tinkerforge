/**
 * Copyright © 2018 Sven Ruppert (sven.ruppert@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rapidpm.event.exentra.tinkerforge.vaadin;

import com.tinkerforge.*;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.Command;

import static java.lang.System.out;

//@Route(NAV_LOGIN_VIEW)
//@Theme(value = Lumo.class, variant = Lumo.DARK)
public class ConnectView extends Composite<HorizontalLayout> {

  public static final String NAV_CONNECT_VIEW = "connect";
  public static final String DEFAULT_HOST     = "localhost";
  public static final String DEFAULT_PORT     = "4223";

  private final TextField host       = new TextField();
  private final TextField port       = new TextField();
  private final Button    btnConnect = new Button();
  private final Button    btnCancel  = new Button();

  private final Chart chart = new Chart();


//  private static final String UID = "i2J";
  private static final String UID = "mbL";

  //TOD refactoring
  private IPConnection         ipcon;
  private BrickletPTC          ptc;
  private BrickletAmbientLight al;

  public ConnectView() {


    final Configuration configuration = chart.getConfiguration();
    configuration.getChart().setType(ChartType.GAUGE);
    configuration.getChart().setAlignTicks(false);
    configuration.setTitle("Lux");
    configuration.getChart().setWidth(500);

    configuration.getPane().setStartAngle(-150);
    configuration.getPane().setEndAngle(150);

    YAxis yAxis = new YAxis();
    yAxis.setClassName("Celsius");
    yAxis.setMin(40);
    yAxis.setMax(130);
    yAxis.setOffset(-25);
    Labels labels = new Labels();
    labels.setDistance(-20);
    labels.setRotationPerpendicular();
    yAxis.setLabels(labels);
    yAxis.setTickLength(5);
    yAxis.setMinorTickLength(5);
    yAxis.setEndOnTick(false);

    configuration.addyAxis(yAxis);

    final ListSeries series = new ListSeries("Lx", 100);

    PlotOptionsGauge plotOptionsGauge = new PlotOptionsGauge();
    plotOptionsGauge.setDataLabels(new DataLabels());
    plotOptionsGauge.setTooltip(new SeriesTooltip());
    plotOptionsGauge.getTooltip().setValueSuffix(" LUX");
    series.setPlotOptions(plotOptionsGauge);

    configuration.setSeries(series);

    HorizontalLayout input   = new HorizontalLayout(host, port);
    HorizontalLayout buttons = new HorizontalLayout(btnConnect, btnCancel);

    VerticalLayout groupV = new VerticalLayout(input, buttons, chart);
    groupV.setDefaultHorizontalComponentAlignment(Alignment.START);
    groupV.setSizeUndefined();
    chart.drawChart();


    host.setPlaceholder(DEFAULT_HOST);
    port.setPlaceholder(DEFAULT_PORT);

    btnCancel.setText("cancel");
    btnCancel.addClickListener(e -> {
      host.clear();
      port.clear();
    });


    btnConnect.setText("connect");
    btnConnect.addClickListener(e -> {
      String hostValue = (host.isEmpty()) ? host.getPlaceholder() : host.getValue();
      String portValue = (port.isEmpty()) ? port.getPlaceholder() : port.getValue();
      ipcon = new IPConnection();
      al = new BrickletAmbientLight(UID, ipcon);


      try {
        ipcon.connect(hostValue, Integer.valueOf(portValue)); // Connect to brickd
//        ptc.addTemperatureListener(temperature -> {
//          double temp = temperature / 100.0;
//          out.println("Temperature: " + temp + " °C");
//
//        });
//        ptc.setTemperatureCallbackPeriod(1000);


        al.addIlluminanceListener(illuminance -> {
          double lx = illuminance / 10.0;
          out.println("Illuminance: " + lx + " lx");
          chart.getUI().ifPresent(u -> u.access((Command) () -> series.updatePoint(0, lx)));
        });

        al.setIlluminanceCallbackPeriod(250);

      } catch (NetworkException | AlreadyConnectedException | TimeoutException | NotConnectedException e1) {
        e1.printStackTrace();
      }


    });


    HorizontalLayout content = getContent();
    content.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    content.setSizeFull();
    content.add(groupV);

  }

}
