package edu.umn.cs.csci3081w.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BusTest {

  @BeforeEach
  void setUp() {
    Stop stop1 = new Stop(1, "Blegen Hall", new Position(44.972392, -93.243774));
    Stop stop2 = new Stop(2, "Coffman", new Position(44.973580, -93.235071));
    Stop stop3 = new Stop(3, "Oak Street at University Avenue", new Position(44.975392, -93.226632));
    Stop stop4 = new Stop(4, "Transitway at 23rd Avenue SE", new Position(44.975837, -93.222174));

    ArrayList<Stop> outboundRouteStops = new ArrayList<>();
    outboundRouteStops.add(stop1);
    outboundRouteStops.add(stop2);

    Route outboundRoute = new Route(1, "Outbound Campus Connector",
        "outbound", "Campus Connector", outboundRouteStops, distances, )

    ArrayList<Stop> inboundRouteStops = new ArrayList<>();
    inboundRouteStops.add(stop3);
    inboundRouteStops.add(stop4);

    Bus bus1 = new Bus(1, , )

  }

  @Test
  void report() {
  }

  @Test
  void isTripComplete() {
  }

  @Test
  void loadPassenger() {
  }

  @Test
  void move() {
  }

  @Test
  void update() {
  }

  @Test
  void getNextStop() {
  }
}