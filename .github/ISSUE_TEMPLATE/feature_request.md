---
name: Feature request
about: Suggest an idea for this project
title: ''
labels: enhancement
assignees: ''

---

<persona> wants <feature> because of <benefit>

**Acceptance criteria:**
  * ...
  * ...

Example:
```
The passenger wants a departure board of trains for a specific train station because he wants to see if his next train connections are on time and depart at the scheduled platform.

**Acceptance criteria:**
- Departure boards display the next 7 trains at a station
- Station is defined by a URL parameter
- Departures are sorted by departure time with the next departure first
- Departures contain information about the train
  - scheduled departure time,
  - real departure time (if not same as scheduled time)
  - delay in minutes 
  - scheduled destination
  - real destination  (if not same as scheduled destination)
  - scheduled platform
  - real platform (if not same as scheduled platform)
  - additional disruption information (if present)
- Board content is fully translated
```
