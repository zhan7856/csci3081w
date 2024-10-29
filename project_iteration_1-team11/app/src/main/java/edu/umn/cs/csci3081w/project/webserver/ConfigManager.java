package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.Counter;
import edu.umn.cs.csci3081w.project.model.Position;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Route;
import edu.umn.cs.csci3081w.project.model.Stop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class ConfigManager {
  private static final String LINE_START = "LINE_START";
  private static final String LINE_END = "LINE_END";
  private static final String ROUTE_START = "ROUTE_START";
  private static final String ROUTE_END = "ROUTE_END";
  private static final String BUS_LINE = "BUS_LINE";
  private static final String TRAIN_LINE = "TRAIN_LINE";

  private List<Route> routes = new ArrayList<Route>();

  public ConfigManager() {

  }

  /**
   * This method reads the configuration file, which contains information
   * about lines, routes, and stops.
   *
   * @param counter counter for identifiers
   * @param fileName the file name of the configuration file
   */
  public void readConfig(Counter counter, String fileName) {
    File configFile = FileUtils.getFile(fileName);
    try {
      String currLineName = "";
      String currLineType = "";
      String currRouteName = "";
      List<Stop> stops = new ArrayList<Stop>();
      List<Double> probabilities = new ArrayList<Double>();
      Scanner scanner = new Scanner(configFile);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] splits = line.split(",");
        String chunk = splits[0].trim();
        if (chunk.equals(LINE_START)) {
          currLineType = "";
          String lineTypeFromConfig = splits[1].trim();
          if (lineTypeFromConfig.equals(ConfigManager.BUS_LINE)) {
            currLineType = Route.BUS_LINE;
          } else if (lineTypeFromConfig.equals(ConfigManager.TRAIN_LINE)) {
            currLineType = Route.TRAIN_LINE;
          }
          currLineName = splits[2].trim();
        } else if (chunk.equals(LINE_END)) {
          currLineType = "";
          currLineName = "";
        } else if (chunk.equals(ROUTE_START)) {
          currRouteName = splits[1].trim();
        } else if (chunk.equals(ROUTE_END)) {
          if (stops.size() > 0) {
            List<Double> distances = new ArrayList<Double>();
            for (int stopIndex = 1; stopIndex < stops.size(); ++stopIndex) {
              Stop prevStop = stops.get(stopIndex - 1);
              Stop currStop = stops.get(stopIndex);
              // Need to turn latitude and longitude into real-world distances.
              // Going one speed in a simulation step moves 0.5 mile.
              // We multiply latitude and longitude by 2 so that a speed of one moves 0.5 mile
              double prevLatitude = prevStop.getPosition().getLatitude() * 69 * 2;
              double prevLongitude = prevStop.getPosition().getLongitude() * 55 * 2;
              double currLatitude = currStop.getPosition().getLatitude() * 69 * 2;
              double currLongitude = currStop.getPosition().getLongitude() * 55 * 2;
              double dist = Math.sqrt((currLatitude - prevLatitude) * (currLatitude - prevLatitude)
                  + (currLongitude - prevLongitude) * (currLongitude - prevLongitude));
              distances.add(dist);
            }
            routes.add(
                new Route(counter.getRouteIdCounterAndIncrement(), currLineName, currLineType,
                    currRouteName, stops, distances,
                    new RandomPassengerGenerator(stops, probabilities)));
            currRouteName = "";
            stops.clear();
            probabilities.clear();
          }
        } else if (chunk.equals("STOP")) {
          String currStopName = splits[1].trim();
          double currStopLatitude = Double.valueOf(splits[2].trim());
          double currStopLongitude = Double.valueOf(splits[3].trim());
          double probability = Double.valueOf(splits[4].trim());
          probabilities.add(probability);
          stops.add(new Stop(counter.getStopIdCounterAndIncrement(), currStopName,
              new Position(currStopLongitude, currStopLatitude)));
        }
      }
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Route> getRoutes() {
    return routes;
  }
}