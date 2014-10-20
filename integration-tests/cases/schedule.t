  $ fail schedule bp-weekday-worktime-daily service_name=my-app noop 30 | tee schedule.out
  Scheduling failure: {duration=30, sapperArgs=[], period=bp-weekday-worktime-daily, searchTerm=service_name=my-app, sapper=noop, configuration={fail.test.fixedUnitTimestamp=0, fail.db.dynamoDBEndpoint=http://localhost:8000}, scheduledBy=*, scheduledAt=0} (glob)
  Scheduled failure with ID *, first run will be at 1970-01-02T* (glob)

  $ fail list-runs --after P2W
  Listing scheduled runs in interval 1970-01-01T01:00:00.000+01:00/1970-01-15T01:00:00.000+01:00
  _____________*____________________________________________ (glob)
  | At         *| Sapper| Target             | Duration (s)| (glob)
  |============*===========================================| (glob)
  | 1970-01-02 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-05 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-06 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-07 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-08 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-09 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-12 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-13 *| noop  | service_name=my-app| 30          | (glob)
  | 1970-01-14 *| noop  | service_name=my-app| 30          | (glob)
  


  $ fail unschedule $(tail -1 schedule.out | cut -f 5 -d' ' | tr -d ,)
  Deleted 9 runs
  Deleted schedule: * (glob)


