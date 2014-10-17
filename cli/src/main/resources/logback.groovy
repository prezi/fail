def USER_HOME = System.getProperty("user.home")

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %.-1level %msg%n"
    }
}

appender("DEBUGLOG", FileAppender) {
    file = "${USER_HOME}/.fail.debuglog"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %.-1level %logger %msg%n"
    }
}

root(INFO, ["CONSOLE", "DEBUGLOG"])

logger("com.linkedin", INFO, ["DEBUGLOG"], false)
