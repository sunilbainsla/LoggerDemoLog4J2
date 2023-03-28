Configuration:
		name: MyConfig
		status: warn
		Properties:
		property:
		applicationName: "My Application"
		Appenders:
		Console:
		name: ConsoleAppender
		target: SYSTEM_OUT
		layout:
		KeyValuePair:
		eventPrefix: "@"
		keyValueSeparator: "="
		eventSuffix: ""
		pairs:
		applicationName: "${sys:applicationName:-${property:applicationName}}"
		filters:
		- ThresholdFilter:
		level: debug
		Loggers:
		Root:
		level: debug
		AppenderRef:
		- ref: ConsoleAppender