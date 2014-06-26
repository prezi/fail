statusListener(OnConsoleStatusListener)

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %msg%n"
    }
}

root(INFO, ["CONSOLE"])

