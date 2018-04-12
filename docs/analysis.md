# Thought Process

## Complete system view

#### Dimensions
- Time 
- Content 
- Taxonomy 
- User

#### Measures
- Timespent
- Score
- Reaction
- Suggestion Acceptance
- Sharing

## EdMobile case

#### Applicable Dimensions
- Time
- Content
- Taxonomy
- User

#### Applicable Measures
- Timespent
- Score


#### Events Needed
- User Collection Timespent
- User Collection Score
- User Taxonomy Timespent
- User Taxonomy Score

#### Gooru's Event Structure

**Collection Play Event**

	[{
		"eventId": "b79bd974-ed93-48e4-bfb8-b25792d56556",
		"eventName": "collection.play",
		"session": {
			"apiKey": "33b9ad34-1a0c-43ba-bb9c-4784abe07110",
			"sessionId": "96dd1aed-3773-42ee-881b-87ea5ecedd62"
		},
		"startTime": 1488215156033,
		"endTime": 1488215156033,
		"user": {
			"gooruUId": "95a744e1-631e-4642-875d-8b07a5e3b421"
		},
		"context": {
			"type": "start",
			"contentGooruId": "cc8d6e77-4253-4bb2-ba31-e9b9f41d6b4d",
			"classGooruId": "aaa5e73a-2372-46a5-8344-4feeab1c9f1d",
			"courseGooruId": "4ba59ecd-8ca1-44ac-9ae7-980f55957de0",
			"unitGooruId": "0f7e5119-1c21-428a-a1f3-9959c39822d5",
			"lessonGooruId": "8bfa9d89-8c1d-47fa-9729-b27468c6866d",
			"collectionType": "assessment",
			"questionCount": 10,
			"clientSource": "web"
		},
		"version": {
			"logApi": "3.0"
		},
		"metrics": {},
		"payLoadObject": {
			"gradingType": "System",
			"isStudent": true
		}
	}]
	
## Data Processing Pipeline

### Workflow

1. Each event originating from client side will be handled by a separate component which should be able to fan out events. These fanned out events will be simple events with predefined schema. 
2. In EdMobile case, the following events will be generated 
	a. event.collection.timespent
	b. event.collection.score
	c. event.competency.timespent
	d. event.competency.score
3. The other events should also be produced but not as part of EdMobile scope. Some examples are
	a. event.resource.timespent
	b. event.resource.reaction
	c. event.collection.reaction
	d. event.suggestion.acceptance
4. There will be different event processors responsible to handle different events. Note that events need to be rolled up too. Hence all the event handlers at collection level will do their processing and then raise a corresponding event for the lesson handlers, if collection is part of lesson. Same thing will happen for unit/course. This we need to hammer out if we want to go this path.
5. For every event we should be having a separate handler which can keep on updating the aggregation. 
	a. Question we need to resolve is that if we are using JDBC we can use Kafka connect however, if the data model is going to be in something like Druid, then we need to understand if we are going to store base lattice in Druid and work off it or we are storing aggregations in Druid.
	b. Current thought process is to have the aggregations in Kafka as well. This would enable to change the backends as and when needed.


### Fanout Component
We would  be needing a component which can do the fanning out. This is going to be an interesting issue as the rest of Kafka pipleline is going to work off simple events in Kafka. This would mean two things:
1. If tomorrow it is decided to include stream processing in our system, and if we modify the event structure so as to enable stream processing, this component can do the in memory aggregation. Till that time we need to have this component do the log writer kind of processing and handle the assessment stop etc to actually generate the score. 


### Kafka Components 
1. How many partitions?
2. What would be replication factor?
3. What params need to setup at Network Layer for OS?
4. How are listeners going to do the lookup? Do we bring in rocks db / redis kind of model or we look up the core db?
5. When do we do event enrichment and how much enrichment is needed? Do we stored enriched data in store (Druid e.g.) or we keep them as separate lookup?

