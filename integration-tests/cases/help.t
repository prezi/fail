  $ fail --help
  Offline
  -------
  usage: fail [options] once tag sapper duration-seconds [sapper-arg ...]
              [options] api-test
   -a,--all                       Hurt all targets (no mercy mode)
      --after <arg>               list: Show scheduled jobs this far in the future
      --api <arg>                 URL prefix to the Fail API
      --at <arg>                  list: Show scheduled round this unix timestamp
      --before <arg>              list: Show scheduled jobs this far in the past
   -c,--use-changelog             Post events to changelog
      --context <arg>             list: Show scheduled jobs this far both in the future and the past
      --datetime-format <arg>
   -h,--help                      Display this help message
   -n,--dry-run                   Don't actually do any non-read-only actions
   -p,--sappers <arg>             Path to sappers tarball
   -s,--scout-type <arg>          How to find targets. DNS or TAG
   -v,--debug                     Set root logger to DEBUG level
   -vv,--trace                    Set root logger to TRACE level
   -Z,--availability-zone <arg>   Limit targets to an availability zone. Only works with TAG scout type.
  
  Online
  ------
  usage: fail [options] list-runs
              [options] log <run-id>
              [options] list [regex]
              [options] panic
              [options] schedule period tag sapper duration-seconds [sapper-arg ...]
              [options] unschedule id
              [options] list-periods
              [options] panic
              [options] panic-over
     --after <arg>          list: Show scheduled jobs this far in the future
     --api <arg>            URL prefix to the Fail API
     --at <arg>             list: Show scheduled round this unix timestamp
     --before <arg>         list: Show scheduled jobs this far in the past
     --context <arg>        list: Show scheduled jobs this far both in the future and the past
     --datetime-format <arg>
  -h,--help                 Display this help message
  -n,--dry-run              Don't actually do any non-read-only actions
  -v,--debug                Set root logger to DEBUG level
  -vv,--trace               Set root logger to TRACE level

