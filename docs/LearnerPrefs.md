# Learner Preferences

## Types Possible

* Content type preferences
* Publisher preferences
* Library preferences

## Criteria Possible
* Time spent
* Reaction
* Search

## Algorithmic averages possible
* Moving average
* Weighted average
* Exponential weighted average

## Phase 1: Scope

### Types supported
* Content type preferences

### Criteria supported
* Time spent

### Algorithmic average supported
* Exponential weighted average, because
	* Provides greater value to recent data points (like weighted average)
	* Basis of calculation is previous value of exponential weighted average instead of original non averaged values
	* Is more accurate if there is rapid degree of variation in data over time
	
#### Definition of Time Spent Average Preference Value
Because of selection of exponential weighted average, the definition of prefs is no longer simply  time spent by user on specific type of activity to total time spent on all types of activities. Rather the definition would become as to how user is evolving each prefs individually over time. Though initial seeding may be done with Simple Average of time spent on that type of activity. 
	
#### Definition of Preference Value
Preference Value will be calculated using proportion of Exponential Weighted Average for specific content type to Exponential Weighted Average across different types of content types

## Current Status

* Event for resource play is already being relayed to DAP
* Infra is present to aggregate these values. Following table is present 
	* <code>userstat_resource_content_type_timespent_ts</code>

<code>
    Column     |            Type             | Collation | Nullable |           Default            
    
---------------+-----------------------------+-----------+----------+------------------------------

 activity_date | date                        |           | not null | 
 
 user_id       | text                        |           | not null | 

 content_type  | text                        |           | not null | 
 
 time_spent    | bigint                      |           |          | 
 
 updated_at    | timestamp without time zone |           | not null | timezone('UTC'::text, now())
</code>

## To be done
* Create a new table to keep track of Exponential Weighted Average for users
* Create a new table to keep track of Preference Value
* Create DAP jobs so that both new tables can be updated
* Need new APIs in ds-user to spit out user's prefs values for content types

## Task List and Estimate
* DB Design : 1 day
* DAP job modification: 3 days
* Read API for prefs for user: 2 days
* Unit Testing: 2 days

## Assumptions
* Content type preference are confined to resources and not questions
* Initial seeding will be done with the proportion of time spent by user on specific type of activity to total time spent on all types of activities. Thus initial seed might reflect plain average
* There is no consideration given to number of attempts on a particular resource(one video played thousand times versus 1000 videos played once) or expected size of resource (e.g. a 100 pages long PDF may take more time than 100 videos in which case data will be skewed)
* The definition of Time spent Preference Average Value is understood and acceptable

## Open Questions
* We have to select Smoothing Constant (2/1+n). This "n" would correspond to period on time series for measurement, and for our case would boil down to number of resource plays as window. Recommended value would be between 3 to 9. But we need to select one, so which one?

