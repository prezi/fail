appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %.-1level %msg%n"
    }
}

root(INFO, ["CONSOLE"])

logger("com.linkedin", WARN)
