# Microservices Udemy

Open this folder in IntelliJ IDEA as one Gradle project. The root
`settings.gradle` includes all seven Spring Boot services and `product-configs`
as modules. Each service remains a separate runnable application.

## Setup

Install JDK 25 and select it as the Project SDK and Gradle JVM in IntelliJ
(Settings > Build, Execution, Deployment > Build Tools > Gradle). Reload the
root Gradle project after opening the folder. Use the included Gradle wrapper;
no separate Gradle installation is needed.

For terminal commands, set `JAVA_HOME` to your JDK 25 installation. For example,
with the JDK already installed on this computer, in PowerShell:

```powershell
$env:JAVA_HOME = "$env:USERPROFILE\.jdks\openjdk-25.0.2"
```

## Modules

| Module | Purpose |
| --- | --- |
| `accounts-service` | Accounts application |
| `cards-service` | Cards application |
| `loans-service` | Loans application |
| `config-server` | Spring Cloud Config server |
| `eureka-server` | Service discovery server |
| `gateway-server` | API gateway |
| `message` | Messaging application |
| `product-configs` | Shared configuration files; no runnable application |

## Build and run

Run these commands from this folder in PowerShell:

```powershell
# Show all modules
.\gradlew.bat projects

# Build every service and run its tests
.\gradlew.bat build

# Package every service without running integration-dependent context tests
.\gradlew.bat build -x test

# Build or test just one service
.\gradlew.bat :accounts-service:build
.\gradlew.bat :accounts-service:test

# Start a service (use a separate terminal for each service)
.\gradlew.bat :accounts-service:bootRun
.\gradlew.bat :config-server:bootRun
```

On macOS/Linux use `./gradlew` instead of `.\gradlew.bat`.
You can also run each application's main class directly from IntelliJ.
Use module-qualified `bootRun` tasks to choose which application to start.

Eureka and the gateway currently import configuration from the config server,
so start the config server before those applications. The config server uses
RabbitMQ at `localhost:5672` and reads configuration from its configured GitHub
repository. Existing application configuration and external infrastructure
requirements still apply; the parent build does not start these dependencies.
In particular, the existing Eureka and gateway context tests require a running
config server at `localhost:8071` (or the `CONFIG_SERVER_URL` you specify).

The `product-configs` module makes the local configuration files available in
the same IDE project. To serve these local files while learning, run the config
server with its native profile and an absolute path to that directory:

```powershell
.\gradlew.bat :config-server:bootRun --args="--spring.profiles.active=native --spring.cloud.config.server.native.search-locations=file:D:/Development/microservices-udemy/product-configs/"
```

## Existing projects

The service directories keep their own build files, wrappers, and settings files.
All modules are tracked by the single Git repository at this project's root.
Their settings files are used only when opening/building them independently;
opening this root folder uses the root settings instead.
Dependency and plugin versions remain defined in each service's build file.
The parent adds build, check, and clean tasks across all modules.

## Course checkpoints

See [the course branch guide](docs/course-branches.md) for matched section branches,
source commit mappings, and the section-13 work in progress.
