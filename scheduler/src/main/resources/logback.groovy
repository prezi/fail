appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} %.-1level %msg%n"
    }
}

logger("com.prezi", DEBUG)

root(INFO, ["CONSOLE"])
