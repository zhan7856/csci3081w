[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/GkN0PmRU)
# Visual Transit Simulator: Project Iteration 1

## The Visual Transit Simulator Software

In the project iterations, you will be working on a visual transit simulator (VTS) software. The current VTS software models vehicle transit around the University of Minnesota campus. Specifically, the software simulates the behavior of vehicles and passengers on campus. The VTS software currently supports two types of vehicles: buses and trains. Vehicles provide service for a line. A line is made by two routes: an outbound and an inbound route. Vehicles go along a route, make stops, and pick up/drop off passengers. The simulation operates over a certain number of time units. In each time unit, the VTS software updates the state of the simulation by creating passengers at stops, moving vehicles along the routes, allowing a vehicle to pick up passengers at a stop, etc. The simulation is configured using a *configuration* file that specifies the simulated lines, the stops of the routes, and how likely it is that a passenger will show up at a certain stop at each time unit. Routes must be defined in pairs, that is, there should be both an outbound and inbound route and the routes should be specified one after the other. The ending stop of the outbound route should be at the same location as the starting stop of the inbound route and the ending stop of the inbound route should be at the same location as the starting stop of the outbound route. However, stops between the starting and ending stops of outbound and inbound routes can be at different locations. After a vehicle has passed a stop, it is possible for passengers to show up at stops that the vehicle has already passed. For this reason, the simulator supports the creation of multiple vehicles and these vehicles will go and pick up the new passengers. Each vehicle has its own understanding of its own route, but the stops have relationships with multiple vehicles serving the same line. Vehicles do not make more than one trip in the line they serve. When a vehicle finishes both of its routes (outbound and inbound), the vehicle exits the simulation.

The VTS software is divided into two main modules: the *visualization module* and the *simulator module*. The visualization module displays the state of the simulation in a browser, while the simulator module performs the simulation. The visualization module is a web client application that runs in a browser and it is written in Javascript and HTML. The visualization module code is inside the `<dir>/app/src/main/webapp/web_graphics` directory of this repo (where `<dir>` is the root directory of the repo). The simulator module is a web server application written in Java. The simulator module code is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project` directory. The simulator module is divided into two parts: *model classes* and the *webserver classes*. The model classes model real-world entities (e.g., the concept of a vehicle) and the code is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/model` directory. The webserver classes include the code that orchestrates the simulation and is inside the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory. The visualization module and the simulator module communicate with each other using [websockets](https://www.baeldung.com/java-websockets).

The user of the VTS software interacts with the visualization module using the browser and can specific how long the simulation will run (i.e., how many time units) and how often new vehicles will be added to a route in the simulation. The users also specifies when to start the simulation. The image below depicts the graphical user interface (GUI) of the VTS software.

![GUI of the VTS Software](/images/vts_iteration_1.png)

### VTS Software Details

#### Simulation Configuration
The simulation is based on the `<dir>/app/src/main/resources/config.txt` configuration file. The following excerpt of the configuration file defines a bus line.

```
LINE_START, BUS_LINE, Campus Connector

ROUTE_START, East Bound

STOP, Blegen Hall, 44.972392, -93.243774, .15
STOP, Coffman, 44.973580, -93.235071, .3
STOP, Oak Street at University Avenue, 44.975392, -93.226632, .025
STOP, Transitway at 23rd Avenue SE, 44.975837, -93.222174, .05
STOP, Transitway at Commonwealth Avenue, 44.980753, -93.180669, .05
STOP, State Fairgrounds Lot S-108, 44.983375, -93.178810, .01
STOP, Buford at Gortner Avenue, 44.984540, -93.181692, .01
STOP, St. Paul Student Center, 44.984630, -93.186352, 0

ROUTE_END

ROUTE_START, West Bound

STOP, St. Paul Student Center, 44.984630, -93.186352, .35
STOP, Buford at Gortner Avenue, 44.984482, -93.181657, .05
STOP, State Fairgrounds Lot S-108, 44.983703, -93.178846, .01
STOP, Transitway at Commonwealth Avenue, 44.980663, -93.180808, .01
STOP, Thompson Center & 23rd Avenue SE, 44.976397, -93.221801, .025
STOP, Ridder Arena, 44.978058, -93.229176, .05
STOP, Pleasant Street at Jones-Eddy Circle, 44.978366, -93.236038, .1
STOP, Bruininks Hall, 44.974549, -93.236927, .3
STOP, Blegen Hall, 44.972638, -93.243591, 0

ROUTE_END

LINE_END
```

The configuration line `LINE_START, BUS_LINE, Campus Connector` defines the beginning of the information belonging to a simulated line. The configuration line `ROUTE_START, East Bound` defines a the beginning of the information defining the outbound route. (The outbound route is always defined before the inbound route). The subsequent configuration lines are the stops in the route. Each stop has a name, a latitude, a longitude, and the probability to generate a passenger at the stop. For example, for `STOP, Blegen Hall, 44.972392, -93.243774, .15`, `Blegen Hall` is the name of the stop, `44.972392` is the latitude, `-93.243774` is the longitude, and `.15` (i.e., `0.15`) is the probability to generate a passenger at the stop. The last stop in a route has a probability to generate a passenger always equal to zero.

#### Running the VTS Software
To run the VTS software, you have to **first start the simulator module** and **then start the visualization module**. To start the simulator module, go to `<dir>` and run `./gradlew appRun` (or `./gradlew clean appRun`). To start the visualization module, open a browser and paste this link `http://localhost:7777/project/web_graphics/project.html` in its address bar. To stop the simulator module, press the enter/return key in the terminal where you started the module. To stop the visualization module, close the tab of browser where you started the module. In rare occasions, you might experience some issues in starting the simulator module because a previous instance of the module is still running. To kill old instances, run `ps aux | grep gretty | awk '{print $2}' | xargs -L 1 kill` (un Unix-like operating systems) and this command will terminate previous instances. (The command is killing the process of the web server container running the simulator module.) The command works on CSE lab machines.

#### Simulation Workflow
Because the VTS software is a web application, the software does not have a `main` method. When you load the visualization module in the browser, the visualization module opens a connection to the simulator module (using a websocket). The opening of the connection triggers the execution of the `WebServerSession.onOpen` method in the simulator module. When you click `Start` in the GUI of the visualization module, the module starts sending messages/commands to the simulator module. The messages/commands exchanged by the two modules are [JSON objects](https://www.w3schools.com/js/js_json_objects.asp). You can see the messages/commands created by the visualization module insdie `<dir>/app/src/main/webapp/web_graphics/sketch.js`. The simulator module processes messages received by the visualization model inside the `WebServerSession.onMessage` method. The simulator module sends messages to the visualization module using the `WebServerSession.sendJson` method. Finally, once you start the simulation you can restart it only by reloading the visualization module in the browser (i.e., reloading the web page of the visualization module).

## Tasks and Deliverables
In this project iteration, you will need to understand, extend, and test the VTS software. The tasks of this project iteration can be grouped into three types of activities: creating the software documentation, making software changes, and testing. The following table provides a summary of the tasks you need to perform in this project iteration. For each task, the table reports the task ID, the activity associated with the task, a summary description of the task, the deliverable associated with the task, and the major deliverable that includes the task deliverable.

| ID | Activity | Task Summary Description | Task Deliverable | Deliverable |
|---------|----------|--------------------------|------------------|----------------------|
| Task 1 | Software documentation | Create a summary description for the VTS software | Text | HTML Javadoc |
| Task 2 | Software documentation | Create a UML class diagram for the model classes | UML Class Diagram | HTML Javadoc |
| Task 3 | Software documentation | Create a UML class diagram for the webserver classes | UML Class Diagram | HTML Javadoc |
| Task 4 | Software documentation | Create a Javadoc documentation for the code in the simulator module | HTML Javadoc | HTML Javadoc|
| Task 5 | Software documentation | Make sure that the code conforms to the Google Java code style guidelines | Source Code | Source Code |
| Task 6 | Software changes | Refactoring 1 - Rename fields in `Route` | Source Code | Source Code |
| Task 7 | Software changes | Refactoring 2 - Create a `Line` class to wrap inbound and outbound routes | Source Code | Source Code |
| Task 8 | Software changes | Feature 1 - Read storage facility information | Source Code | Source Code |
| Task 9 | Software changes | Feature 2 - Create vehicles according to storage facility information | Source Code | Source Code |
| Task 10 | Software changes | Feature 3 - Extend the simulation to provide CO2 consumption information  | Source Code | Source Code |
| Task 11 | Software changes | Feature 4 - Add pausing/resume capabilities to the simulator  | Source Code | Source Code |
| Task 12 | Testing | Create unit tests for the `Bus`, `Train`, `Stop`, `Passenger`, and `Route` classes | Test Code | Test Code |


### Suggested Timeline for the Tasks

We suggest you define a tasks-oriented timeline for your team so that you can make progress throughout this project iteration. The schedule for the project iteration is very tight, and it is important that the team keeps up with the project. We suggest the following timeline. However, you are free to define your own timeline. All the major deliverables are due at the end of the project iteration.

| Date | Milestone Description | Tasks |
|-----------------|-----|-----------------|
| 10/15/2024 at 1:00 pm | Software documentation and testing | [Task 1], [Task 2], [Task 3], [Task 12] |
| 10/22/2024 at 1:00 pm | Software changes | [Task 6],[Task 7],[Task 8],[Task 9],[Task 10],[Task 4],[Task 5] |
| 10/29/2024 at 1:00 pm | Software changes, testing, and revision of the software documentation | [Task 11],[Task 12],[Task 1],[Task 2],[Task 3],[Task 4],[Task 5] |

### Tasks Detailed Description
This section details the tasks that your team needs to perform in this project iteration.

#### Task 1: Create a summary description of how to use the VTS software
Code documentation comes in many forms for many audiences. For this project, your audience is other programmers who need to understand the codebase. To this end, your team needs to produce a textual description providing an introduction to the codebase to other programmers who might be new to the project and need to know how to configure it, compile it, and execute it. (Feel free to reuse parts of the information provided in this document.) The team needs to include this textual description in the `<dir>/app/doc/overview.html` file (you can open this file using a text editor). This file will be automatically added to the HTML Javadoc documentation of the project. You can generate the HTML Javadoc documentation, which includes this description, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 2: Create a UML class diagram for the model classes
In this task, you should produce a UML class diagram of the model classes (e.g., classes in the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/model` directory). The diagram should include all the classes and their relationships. The diagram should also include all the attributes and operations for each class. Keep in mind the following guidelines while creating the diagram:

* Make the most important classes prominent in the layout (i.e. your eyes tend to focus in that general area when you first look at it).
* Lay out the classes so that the connections have as few crossovers as possible.
* Do not include setters and getters unless there is something special about them that your team needs to communicate.
* Include cardinality where appropriate.
* Include visibility, type, name for attributes.
* Include visibility, name, types of parameters, and return type for operations.
* If necessary, provide a description inside `<dir>/app/doc/overview.html` to clarify the subtleties that are essential to understand the diagram.

You need to place the diagram as am image file in `<dir>/app/doc/diagrams` and suitably update the `<dir>/app/doc/overview.html` file to include the image in the generated HTML Javadoc documentation. The file should be called `model_diagram` with the suitable extension for the type of image file your team created. (The codebase provides an example for including an image in the `<dir>/app/doc/overview.html` file and your team should remove the example `umn.jpg` from the file.) You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 3: Create a UML class diagram for the webserver classes
In this task, you should produce a UML class diagram of the webserver classes (e.g., classes in the `<dir>/app/src/main/java/edu/umn/cs/csci3081w/project/webserver` directory). The diagram should include all the classes and their relationships. The diagram should also include all the attributes and operations for each class. Keep in mind the following guidelines while creating the diagram:

* Make the most important classes prominent in the layout (i.e. your eye tends to focus in that general area when you first look at it).
* Lay out the classes so that the connections have as few crossovers as possible.
* Do not include setters and getters in your methods list unless there is something special about them that your team needs to communicate.
* Include cardinality where appropriate.
* Include visibility, type, name for attributes.
* Include visibility, name, types of parameters, and return type for operations.
* If necessary, provide a description inside `<dir>/app/doc/overview.html` to clarify the subtleties that are essential to understand the diagram.

You need to place the diagram as am image file in `<dir>/app/doc/diagrams` and suitably update the `<dir>/app/doc/overview.html` file to include the image in the generated HTML Javadoc documentation. The file should be called `webserver_diagram` with the suitable extension for the type of image file your team created. (The codebase provides an example for including an image in the `<dir>/app/doc/overview.html` file and your team should remove the example `umn.jpg` from the file.) You can generate the HTML Javadoc documentation, which includes this diagram, by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 4: Create a Javadoc documentation for the code in the simulator module
You should create Javadoc comments according to the Google Java code style guidelines we provided. In other words, add comments where the Google Java code style guidelines require your team to do so. You can generate the HTML Javadoc documentation by running `./gradlew javadoc` (or `./gradlew clean javadoc`).

#### Task 5: Make sure that the code conforms to the Google Java code style guidelines
Consistency in code organization, naming conventions, file structure, and formatting makes code easier to read and integrate. In this project, the team will follow the Google Java code style guidelines. These guidelines are provided in the `<dir>/app/config/checkstyle/google_checks.xml` code style file. The team needs to make sure that the code produced in this project iteration (both source and test code) complies with the rules in `<dir>/app/config/checkstyle/google_checks.xml`. Both source and test code should conform to the rules. You can check if the code conforms to the code style rules by running `./gradlew check` or (`./gradlew clean check`).

#### Task 6: Refactoring 1 - Rename fields in `Route`
Rename the field `destinationStopIndex` in `Route` to `nextStopIndex`. Rename the field `destinationStop` in `Route` to `nextStop`. Make sure to also adapt the codebase (both source and possibly test code). You should use the test cases you will create in Task 12 to ensure that this refactoring operation does not introduce errors in the codebase. Finally, this task should be performed in a branch called `Refactoring1`. Once the team is happy with the solution to this task, the team should merge the `Refactoring1` branch into the `main` branch.

#### Task 7: Refactoring 2 - Create a `Line` class to wrap inbound and outbound routes
In this code refactoring task you should create a `Line` class that contains two fields: `outboundRoute` and `inboundRoute`. Both fields are of type `Route`. You should then replace the fields `outboundRoute` and `inboundRoute` inside the `Bus` and `Train` classes with a field called `line` of type `Line`. While performing this refactoring, you should suitably adapt accesses to existing fields with accesses to the new fields. In this refactoring, you should also replace the `out` and `in` parameters from the `Bus` and `Train` constructors with a parameter of type `Line`. The `Line` class needs to belong to the model classes. You should use the test cases you will create in Task 12 to ensure that this refactoring operation does not introduce errors in the codebase. Finally, this task should be performed in a branch called `Refactoring2`. Once the team is happy with the solution to this task, the team should merge the `Refactoring2` branch into the `main` branch.

#### Task 8: Feature 1 - Read storage facility information

The current version of the VTS software can create an unlimited number of vehicles. In the real-world, we do not have unlimited resources. In this task, you should extend the VTS software to read additional information from the configuration file (`<dir>/app/src/main/resources/config.txt`). The format of the additional information that should be read from the configuration file is the following:

```
STORAGE_FACILITY_START

BUSES, 10
TRAINS, 11

STORAGE_FACILITY_END

```

The information appears after the information of all the simulation lines. This information provides the number of buses and trains available for the simulation. In the case of the example above, the simulation will have 10 buses and 11 trains. The information should be stored in a `StorageFacility` class using two fields: `busesNum` and `trainsNum`. The `StorageFacility` class needs to belong to the model classes. This task should be performed in a branch called `Feature1`. Once the team is happy with the solution to this task, the team should merge the `Feature1` branch into the `main` branch. 

#### Task 9: Feature 2 - Create vehicles according to storage facility information

In this task, you will need to change the simulation so that it creates a vehicle only if a vehicle is available based on the storage facility information. For example, when a bus is created, `busesNum` will decrease by one. If `busesNum` becomes zero, then no bus can be created unless a bus terminates its service (which increases `busesNum` by one). A bus can be created only at the right time unit cycle between buses (i.e., if a line has a time-between-buses value equal to five then a bus can be created only at every five time units). This task should be performed in a branch called `Feature2`. The same example applies to trains. Once the team is happy with the solution to this task, the team should merge the `Feature2` branch into the `main` branch.

#### Task 10: Feature 3 - Extend the simulation to provide CO2 consumption information 

The VTS software should be extended to provide CO2 consumption information. Buses consume two CO2 unit for ever passenger present on the bus at a certain time unit plus a constant value of four CO2 units. Trains consume three CO2 units for every passenger present on the train at a certain time unit plus a constant value of six CO2 units. For example, if at simulation step number six there a three people on a bus, the bus is consuming (3 * 2) + 4 = 10 CO2 units at that time unit. The simulator module should also provide this information to the visualization module. To do so, you should change the code of a class among the webserver classes (**hint: the code is in a class representing a simulation command**). You can see the CO2 consumption of a vehicle by hovering the mouse on a vehicle as the visualization module is running. Finally, this task should be performed in a branch called `Feature3`. Once the team is happy with the solution to this task, the team should merge the `Feature3` branch into the `main` branch.

#### Task 11: Feature 4 - Add pausing/resume capabilities to the simulator
If a user presses the pause/resume button, the simulation should pause/resume. When the user presses the resume button, the simulation should resume from where it left off. **This task needs to be implemented in the simulator module**. You can see the messages/commands created by the visualization module insdie `<dir>/app/src/main/webapp/web_graphics/sketch.js`. This requires the addition of a command to the set of commands already implemented in the web server. For example, the class `UpdateCommand` implements the command used to updated the simulation. Finally, this task should be performed in a branch called `Feature4`. Once the team is happy with the solution to this task, the team should merge the `Feature4` branch into the `main` branch.

#### Task 12: Create unit tests for the `Bus`, `Train`, `Stop`, `Passenger`, and `Route` classes
Unit tests are essential in a large-scale project because the entire code base can be tested regularly and automatically as it is being developed. In this project iteration, your team has to create unit tests for all the public methods in the `Bus`, `Train`, `Stop`, `Passenger`, and `Route` classes. We are interested in seeing at least one test cases per method and the team does not need to create test cases for getter and setter methods. The team has to document what each test is supposed to do by adding a Javadoc comment to the test. A sample set of test cases is provided in the `<dir>/app/src/test/java/edu/umn/cs/csci3081w/project/model/StopTest.java` test class. We encourage your team to write the test cases before making any change to the codebase. These tests should all pass. After creating the test cases, you can perform the refactoring tasks and use the tests to ensure that your team did not introduce errors in the code. (Some test cases might need refactoring as well.) When you add the new features, you should also add new test cases for the features (when applicable). All the test cases you create should pass. Your team can create test cases in any branch but the final set of test cases should be in the `main` branch. You can run tests with the command `./gradlew test` or (`./gradlew clean test`).

## Submission
**The team members should submit the commit ID of the solution to this project iteration on Gradescope**. The commit ID should be from the `main` branch of this repo.

### General Submission Reminders
* Use the branches we listed to produce your team solution.
* Make sure the files of your solution are in the repo.
* Do no add the content of the `<dir>/app/build` directory to the repo.
* Make sure your code compiles.
* Make sure the code conforms to the Google Java code style guidelines we provided.
* Make sure the HTML Javadoc documentation can be generated using `./gradlew clean javadoc`
* Make sure all test cases pass.

## Assessment
The following list provides a breakdown of how this project iteration will be graded.

* Software documentation: 36 points
* Software changes: 36 points
* Testing: 28 points

## Resources

* [A Guide to the Java API for WebSocket](https://www.baeldung.com/java-websockets)
* [JSON objects](https://www.w3schools.com/js/js_json_objects.asp)
