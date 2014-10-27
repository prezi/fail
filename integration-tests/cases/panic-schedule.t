Engage panic mode

  $ fail panic
  ??:??:??.??? I Panic engaged. Running injections are aborted, scheduled runs are cancelled, no future runs are being scheduled. (glob)

Schedule a failure. The first run is not scheduled.

  $ fail schedule bp-weekday-worktime-daily service_name=my-app noop 30 | tee schedule.out
  ??:??:??.??? I Failure to schedule: (glob)
  _____________________________________________________________________________________________________________
  | Period                   | Sapper| Target             | Duration (s)| Scheduled by*| Scheduled at           | (glob)
  |=======================================================================*=========================| (glob)
  | bp-weekday-worktime-daily| noop  | service_name=my-app| 30          | *| 1970-01-01 00:00:00 UTC| (glob)
  
  ??:??:??.??? W NOTE: Panic mode is engaged, no runs are being scheduled. (glob)
  ??:??:??.??? I Scheduled failure with ID * (glob)


List runs. No runs at all are being scheduled.

  $ fail list-runs --after P2W
  ??:??:??.??? W NOTE: Panic mode is engaged, no runs are actually started. (glob)
  ??:??:??.??? I Listing scheduled runs in interval 1970-01-01T00:00:00.000Z/1970-01-15T00:00:00.000Z (glob)
  ??:??:??.??? I ______________________________________________ (glob)
  | Id| At| Sapper| Target| Status| Duration (s)|
  |=============================================|

The list commands works normally, except it prints a warning telling the user we're in panic mode.

  $ fail list '.*my-app'
  ??:??:??.??? W NOTE: Panic mode is engaged, no runs are being scheduled. (glob)
  ??:??:??.??? I __*__________________________________________________________________________________________________ (glob)
  | Id*| Period                   | Sapper| Target             | Duration (s)| Scheduled by| Scheduled at           | (glob)
  |=*=============================================================================================================| (glob)
  | *| bp-weekday-worktime-daily| noop  | service_name=my-app| 30          | *| 1970-01-01 00:00:00 UTC| (glob)


The list commands works normally, except it prints a warning telling the user we're in panic mode.

  $ fail list no-matches
  ??:??:??.??? W NOTE: Panic mode is engaged, no runs are being scheduled. (glob)
  ??:??:??.??? I ______________________________________________________________________ (glob)
  | Id| Period| Sapper| Target| Duration (s)| Scheduled by| Scheduled at|
  |=====================================================================|


Stop the panic.

  $ fail panic-over
  ??:??:??.??? I Panic is over, continuing scheduling. (glob)


list-runs now schedules failure runs normally.

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


clean up the test DB

  $ fail unschedule $(tail -1 schedule.out | cut -f 7 -d' ' | tr -d ,)
  ??:??:??.??? I Deleted 9 runs (glob)
  ??:??:??.??? I Deleted schedule: * (glob)

