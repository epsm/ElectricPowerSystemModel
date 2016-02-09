#Electric power system model
###General data for project
This is the simple model of a dedicated electric power system. The model consist of two separate parts. There are model and dispatcher. In the model simulated working of power stations equipment, that participate in process of supporting the frequency in system.
The model gets daily generation schedules from dispatcher. Dispatcher gets data from power stations, consumers and calculates (for now gives stub) daily generation schedules.

Application consist of four packages, two models:

+ [epsm-core](https://github.com/epsm/epsm-core)
+ [epsd-core](https://github.com/epsm/epsd-core)

and two wrappers for models that helps intarcting models using JSON. Also wrappers have web interfaces.

+ [epsd-web](https://github.com/epsm/epsd-web)
+ [epsm-web](https://github.com/epsm/epsm-web)


Application launched on two servers on OpenShift:

+ [model](http://model-epsm.rhcloud.com/)
+ [dispatcher](http://dispatcher-epsm.rhcloud.com/app/history). 

The total project size is more than 16,000 source lines of code.

##epsm-core
#### subject area description

The frequency in the power system is a measure of the matching of generation and consumption in the current time. To maintain the frequency constant, it is necessary to maintain the balance of generation and consumption.

There are three type of frequency regulation:

1. Primary regulation is carried out by all units within the currently available reserves of primary regulation.
2. Secondary frequency control. Primary frequency control fundamentally can not provide constant frequency in power system with variations of load. Restore it to a specified value ensures the secondary frequency control by using a dedicated generators (power stations).
3. Tertiary regulation (not modeled).

Frequency in a power system is supported by turbines rotation regulators. Characteristics of the regulator in steady state is
the dependency load on frequency. The controller can be configured to astatic and static characteristic. In the first case a controller supports a constant rotation speed  regardless of a load, in the second case, the load depends on the frequency. When frequency decreases load decreases too and vice versa.


When generators works in parallel the load distributes is inversely proportional to coefficient of statism of regulating characteristics. If at least one unit in the system has a static characteristic, the characteristic of the power system will also be astatic. In this case (with any change in load) frequency in the power system will not change, and units which have static characteristics, will work under constant load. All load changes will get themselves generator with astatic characteristic of the regulation.

The load of almost all consumers depends on frequency. All consumers are divided into two groups:

1. power does not depend on the frequency;
2. power depends on the degree of frequency like Р=Рnom(Fcur/Fnom)^x.

#### realization
Here will be given class diagram with sufficient for the understanding of the program details. Also description of the program will be provided.

+ model class diagram
![simulation class diagramm](https://cloud.githubusercontent.com/assets/16285736/12733499/e2c67916-c943-11e5-8978-c8f4e34a8a89.jpg)

+ PowerObject hierarchy class diagram
![powerobject class diagram](https://cloud.githubusercontent.com/assets/16285736/12742632/2b6ec9e2-c990-11e5-809a-b8ca87e10bc7.jpg)
interface Dispatcher see realiaztion chapter in [epsd-core](https://github.com/epsm/epsd-core).

+ Message hierarchy class diagramm
![message class diagram](https://cloud.githubusercontent.com/assets/16285736/12732296/8d1cedac-c93d-11e5-93cc-159af9055fad.jpg)

There are three kind of the power objects in the model for now:

1. PowerStation - has one or more generators.
2. ScheduledLoadConsumer - a consumer which load set with daily generation schedule and random deviation in percent of this schedule.
3. ShockLoadConsumer - a consumer who is turns on for the preset time and power. Turn-on time, idle and load take a random value between half and full value of the assigned parameter.

All of these objects implements  SimulationObject interface. Object ElectricPowerSystemSimulation using method calculatePowerBalance() gets current power balance from objects. Balance is positive for stations and negative for consumers. Also using this method the objects computing their internal state, e.g. set the power generators of the power plant and create a State object that represents their current state. 

Through the method executeCommand(...) objects gets commands from dispatcher. For now implemented only a daily generation schedule for a power plant. 

Executing the doRealTimeOperation() method of interface RealTimeOperation, objects sends the accumulated by a PowerObjectMessageManager State objects to the dispatcher. 

While receiving and sending objects PowerObjectMessageManager checks their compliance with this subclass of class PowerObject. Validation of the received commands implemented in subclasses of class PowerObject objects, which have an instances of class, derived from the abstract class CommandValidator.

Modeling of the process of consumption and  generation is discrete with defined steps. SimulationRunner after a specified duration of time calls the calculateNextStep method() on a ElectricPowerSystemSimulation object. This method calls a method calculatePowerBalance() for each object in the simulation. 

Time in the simulation not associated with real and is calculated by adding a predetermined value to the previous at each step.

So there running two threads simultaneous, one for calculation balance and the other one for sending states to dispatcher.

PowerObject objects are thread safe. It is achieved by assigning a reference to an object State in thread that performs method calculatePowerBalance() only after State is created, The thread executing doRealTimeOperation() only gets a reference to the last created object.

The interaction between the dispatcher and the objects from the model performing through instances of classes derived from the abstract Message class.

Learn more about dispatcher [epsd-core](https://github.com/epsm/epsd-core).

For now it's impossible to add objects to the model. More precisely, it's impossible to add objects with the desired parameters as factory objects are implemented as stubs. They instantiates objects with predefined options instead of accept. As a result, the model is created with a pre-configured set of objects.

#### technologies
Java core, JSON, SLF4J, Logback, Junit, Mockito, PowerMockito.

Unit-test coverage according to EclEmma is 97%.