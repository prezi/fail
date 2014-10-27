Schedule a failure. The first run is scheduled right away.

  $ fail schedule bp-weekday-worktime-daily service_name=my-app noop 30 | tee schedule.out
  ??:??:??.??? I Failure to schedule: (glob)
  _____________________________________________________________________________________________________________
  | Period                   | Sapper| Target             | Duration (s)| Scheduled by*| Scheduled at           | (glob)
  |=======================================================================*=========================| (glob)
  | bp-weekday-worktime-daily| noop  | service_name=my-app| 30          | *| 1970-01-01 00:00:00 UTC| (glob)
  
  ??:??:??.??? I Scheduled failure with ID *, first run will be at 1970-01-02T* (glob)


list-runs schedules runs into the future.

  $ fail list-runs --after P2W
  ??:??:??.??? I Listing scheduled runs in interval 1970-01-01T00:00:00.000Z/1970-01-15T00:00:00.000Z (glob)
  ??:??:??.??? I _____________*____________________________________________ (glob)
  | Id *| At         *| Sapper| Target             | Status| Duration (s)| (glob)
  |===============*===================================================| (glob)
  | *| 1970-01-02 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-05 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-06 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-07 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-08 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-09 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-12 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-13 *| noop  | service_name=my-app| FUTURE| 30          | (glob)
  | *| 1970-01-14 *| noop  | service_name=my-app| FUTURE| 30          | (glob)



list with a matching regex finds our scheduled failure by Target...

  $ fail list '.*my-app'
  ??:??:??.??? I _*_ (glob)
  | Id*| Period                   | Sapper| Target             | Duration (s)| Scheduled by*| Scheduled at           | (glob)
  |=*=| (glob)
  | *| bp-weekday-worktime-daily| noop  | service_name=my-app| 30          | *| 1970-01-01 00:00:00 UTC| (glob)


But it doesn't find the scheduled failure if the regex doesn't match

  $ fail list no-matches
  ??:??:??.??? I ______________________________________________________________________ (glob)
  | Id| Period| Sapper| Target| Duration (s)| Scheduled by| Scheduled at|
  |=====================================================================|



Unschedule deletes the schedule itself and the runs scheduled by list-runs.

  $ fail unschedule $(tail -1 schedule.out | cut -f 7 -d' ' | tr -d ,)
  ??:??:??.??? I Deleted 9 runs (glob)
  ??:??:??.??? I Deleted schedule: * (glob)


