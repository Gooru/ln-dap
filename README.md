# Data Analytics Pipeline

## Overview

This component is responsible for housing the producers and listeners off the Kafka topics which are acting as consumers (listeners) for events. These consumers in turn may take action based on type of event received. These actions could be:

- Update the state in datastore
- Produce another event as part of fan out
- Either one of above
- Both of above

## Code Organization

### Guidelines

Here is the basic idea:

- All the listeners will be housed in same source code repository
- All the listeners should belong to their own package. This does not imply that there won't be any shared code (infra code and utility classes would be shared), but this is to ascertain that there is no functional sharing. This means that even if same tables are going to be updated as part of two different listeners, the code would be duplicated in both listeners. This would help migrate the changes to datastore in incremental fashion without too much of dependencies. Also it would enable to do path specific event processing w/o polluting code with if-else blocks
- The listeners to deploy and the number of instances to deploy should be controllable by configuration passed to program
- In most cases single listener should bind to single topic as a consumer (unless there is a justification which warrants to do otherwise)

### Package Structure

This section outlines the top level packages which are bound to specific functionality. Not every package will appear here.

#### Global (org.gooru.dap) 

This package is home for whole application. It contains only one class named AppRunner. AppRunner is main class for this binary.

### Adding a new DEP
- Define the name in JSON config for key "deps.to.deploy"
- Provide detailed configuration under key named "deps.config" with the name as defined above. Note that instances ideally should be one per partition and should be derived from topic definition
- Provide specific appender in logback-dap.xml to enable separate logging for this dep
- Create a new package under org.gooru.dep for the new dep
- Define a kafka consumer class by inheriting KafkaConsumerTemplate and provide implementation for abstract methods
- Define a processor for processing of the record

### Build 

The build uses gradle 4.4. Best way is to use the gradlew binary (on windows OS gradlew.bat) to do the build. 

The default target would be a far jar and thus to create the binary one can just run
    
    ./gradlew
    
Or if gradle 4.4 is installed then

    gradle
    
This would create a uber jar in build/libs directory.

### Run binary

The binary can be run like any other java runnable jar. 

    java -jar dap.jar
    
To provide a custom configuration, run it as

    java -jar dap.jar -Dconfig.file=src/main/resources/dap.json
    
