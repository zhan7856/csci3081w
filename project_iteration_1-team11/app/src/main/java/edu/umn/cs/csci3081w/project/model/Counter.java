package edu.umn.cs.csci3081w.project.model;

public class Counter {

  public int routeIdCounter = 10;
  public int stopIdCounter = 100;
  public int busIdCounter = 1000;
  public int trainIdCounter = 2000;

  public Counter() {

  }

  public int getRouteIdCounterAndIncrement() {
    return routeIdCounter++;
  }

  public int getStopIdCounterAndIncrement() {
    return stopIdCounter++;
  }

  public int getBusIdCounterAndIncrement() {
    return busIdCounter++;
  }

  public int getTrainIdCounterAndIncrement() {
    return trainIdCounter++;
  }

}
