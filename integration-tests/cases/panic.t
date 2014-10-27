  $ fail panic
  ??:??:??.??? I Panic engaged. Running injections are aborted, scheduled runs are cancelled, no future runs are being scheduled. (glob)

  $ fail panic
  ??:??:??.??? W We're already in panic mode. (glob)

  $ fail panic-over
  ??:??:??.??? I Panic is over, continuing scheduling. (glob)

  $ fail panic-over
  ??:??:??.??? W We're not in panic mode. (glob)
