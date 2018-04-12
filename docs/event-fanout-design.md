# Data Analytics Pipeline Processing

## Event Types

There are two types of events:

1. Core system (Server side events which are specific to content/class/user etc and not to usage)
2. User actions resulting in events which can either be produced by user facing system or by core system in response to user action

## Event Processing Architecture

### Event Generators

There are two sources of events:

1. Internal system
2. User facing systems (Front End or partner systems)

The journey of events start from event generators where they are emitted. Note that generators may reside within firewall (SSE) or they may be coming from outside world.

The event generators generates event in pre defined format which are used for different actions (in case of usage events) or different CRD operations on core entities (in case of most SSE). The usage event format may be shared with partners. In some cases the partner facing event format may be different (e.g. EdMobile) and there would be a transformer which can do transformation from this format to global usage event format. In this case the transformer also acts as event generator.

### Global Event Processors (GEP)

GEP are second hop of the event life cycle. 

The event that are coming in from outside world are atomic events and they may not carry a lot of context information with them. Note that context in this case does not imply context of event attributes (e.g. CUL in case of collection or resource) but it implies context of metrics (e.g. original aggregate value of score at collection level).

To get the context information in the pipeline there could be two approaches:
1. We provide some kind of streaming capabilities if the data provided in event itself is sufficient to do that
2. We create temporary processing area where we utilize persistence and then use the context from persistent store to do further processing

Note that there could be single or multiple GEP per source or type of event living in same or different housing. We shall hammer out more details as we progress on implementation side. 

The current log writer module that we have, should also be able to qualify for GEP.

For every event that is handled by GEP, there is a change in state of the system and associated entities. This would require additional post processing capabilities on GEP part such that it should be able to provide some signal to outside world about the entities and metrics that have been impacted by the event processing. This is one major piece which needs to be built into log writer component, if we are planning to use it as is.

### Event Demultiplexers (demux)

The next hop on event processing pipeline is(are) demux. Demux are going to look at each information bit provided by GEP, and then emit one or more specific internal events called as Discrete Events (DE). These discrete event are created with each measure(metric) in mind. There would be cases where in one measure contributes to different dimensions, this may be handled in two ways:
1. Demux emits events for all the dimensions
2. Demux emits events for lower (or lowest) dimension, and then rely on discrete event processors(dep) to emit the next level of event(s)

We shall try and stick with option 2, however, in cases where it is more beneficial and simple to use option 1, we can use that with due diligence.

### Discrete Event Processors (DEP)

The dep are components which are going to handle the de. The result of dep processing can be one or both of:
1. Emit event(s) either after transformation or processing
2. Update state in some persistent store

Note that in case events are emitted, there would be another set of dep, which would be registered to handle those events

Thus it is a possibility that the cycle of DE->DEP->DE->DEP .... may continue for long. We need to be cautious so that we don't end up with long tail for processing.

### Architecture Summary

    [Events] -> GEP -> Demux -> [DE] -> DEP -> DE
                                            -> Store

The elements in square braces signify the data (event) while other signify processing components.

## System design to handle usage events

To fast track the current system implementation, we shall use the current Log Writer as our GEP component and we shall modify it to provide post processing capabilities. This post processing will result in DE being generated. 

### DE naming convention

#### Usage DE
All the usage DE will be named as:

usage.[object].[metrics]

The value for object would be what user has interacted with e.g. resource or collection. Not all objects will be used in generating DE as they are coming from demux. Later while internal fanout is happening we shall decide if we want to fan out event for whole object hierarchy.

Metrics would have values like timespent, score, reaction etc.

#### System DE
[entity].[action]

The entity could be class, user etc. The action could be updated, deleted etc.

**Note that a single event would be catering to a single metrics. We are not going to overload events with multiple metrices.**

### DE sink
All the DE will be dumped to Kafka to handle the back pressure. All the DE will be routed to a new Kafka infrastructure which will be scaled based on the need.

#### Kafka topic naming convention
All Kafka topics will be named as:

**org.gooru.da.sink.[event source].[event bucket]**

"da" stands for Data Analytics.

Here event source could be one of following:
1. "demux": In case event is coming directly from demux component
2. "dep": In case event is generated as part of processing by dep component

Event bucket could either simply be the event name (most probably in cases of usage demux generated events, after removing string "usage") or they could be entity in case of System DE.

### DE emitted by demux

Following are the DE that will be emitted by demux:
1. usage.resource.reaction
2. usage.resource.timspent
3. usage.question.reaction
4. usage.question.timespent
5. usage.collection.score
6. usage.assessment.score
7. usage.collection.reaction
8. usage.assessment.reaction
9. usage.collection.timespent
10. usage.assessment.timespent
11. usage.collection.view
12. usage.assessment.view

All the resource level DE will be produced by demux in response to resource.play.stop usage events. All assessment level DE will be produced in response to assessment.play.stop event. The collection level events can be fired at either resource level events or question level events when questions are contained in collection. 

We may have to chalk out life cycle for each DE to understand what functionality would be served by corresponding DEP and if there are other DE which are going to be emitted. 

## Constraints to be aware of

Since we are developing a pipeline processing, there are things which we may have to keep in mind. 

### Ordering issues

These types of issue may arise, when a teacher has just created a content and students are playing it, there may be cases when the content side if event may not be processed before usage events. In those cases, we need to understand as to how do we handle the cases. Not having correct metadata may result in different scenarios, e.g. the event fanning out for that assessment may not happen because there are no competencies found for that assessment (as a matter of fact the assessment itself won't be found).

 