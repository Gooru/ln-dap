# Requirements for Data Analytics

These requirements are categorised into two buckets
1. Metrices captured using the usage data
2. Metrices captured using the operations on static data

The first category is delved into detail below. The second category encompasses things like count of content items per competency (things which are taken care by other systems e.g. LM APIs)

## Requirements based on Usage data

### Dimensions
- Time 
- Content 
- Taxonomy 
- User

### Measures
- Timespent
- Score
- Reaction
- Suggestion Acceptance
- Sharing

### Questions that need to be answered

1. User distribution by Geo and Subject
2. User grade per subject 
3. User vectors
4. User competency status (counts with status)
5. User journeys status (counts)
6. User stats timespent (aggregate per resource type)
7. User course status (performance, timespent and completion) [Note : multiple measures]
8. User preference for content type
9. User preference for Curators
10. User preference for Providers
11. User views per resource type
12. User views per Curator
13. User views per Provider
14. User timespent per Curator
15. User timespent per Provider
15. User competency matrix
16. User timespent per named(unique) resource
17. User course level performance
18. User course, unit level performance
19. User course, unit, lesson level performance
20. User course, unit, lesson, collection level performance
21. User reaction for resource/question/collection
22. Original resource timespent for each non original resource
23. Original resource views based on resources in collections
24. Original resource reactions based on resources in collections
25. Others ...

There would be other measures/metrices as well like sharing and suggestions, but right now we are confined to this set only.

#### This can then be sub divided into two parts

1. Derived metrices: These are the metrices which need to be derived from data coming in from event and/or data already present. These may also require a job which can either run periodically or can be long running to do the calculations. Or these measure can be calculated outside of this processing pipeline and could be directly ingested. Examples could be profile vectors, prefs values etc
2. Data metrices: These are the metrices which are either coming directly from event or they can be obtained from metrics value stored already and incoming value. Examples could be timespent, score, completion etc.


### Metadata
Dimensions that are going to be part of reports will be supplied using static tables (technically these tables should be updated based on SSE coming from core systems) which are storing dimensions and their attributes. These tables would either be used in lookups or joins to filter out the reports from primary usage tables.

#### Metadata Classification

1. User
2. Content
3. Class
4. Taxonomy
5. Curators
6. Providers