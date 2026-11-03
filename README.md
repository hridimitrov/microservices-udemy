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
so start the config server before those applications. On the current branch,
the config server serves its bundled native H2 configuration. Accounts and
message use Kafka at `localhost:9092` when run directly from the IDE.
Existing application configuration and external infrastructure
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

### Rebuild the Docker stack

All seven services use `s14` image tags. Accounts,
cards, and loans use H2. Compose includes Kafka, Keycloak, and Redis.
Accounts and message connect to Kafka at `kafka:29092` inside Docker.
Kafka exposes port 9092 for IDE clients; Redis exposes port 6379
and persists data with append-only storage. Keep standalone containers using
these ports stopped before starting the Compose stack.
Kafka runs as a single-node KRaft broker with plaintext listeners for local development.
Kafka data currently lives inside the container and is lost when it is recreated.
Redis is available at `redis:6379` within the network; the gateway currently
has no Redis client or rate limiter configured.

Build their images into your local Docker daemon from the current source:

On Windows, run `.\build-docker-images.cmd` to build all seven service images
using their configured Jib tags. Start Docker Desktop first and ensure JDK 25
is available (see Setup above). The script can be launched from any directory;
it builds images locally without starting containers or pushing to a registry.

```powershell
.\gradlew.bat :config-server:jibDockerBuild :eureka-server:jibDockerBuild :accounts-service:jibDockerBuild :cards-service:jibDockerBuild :loans-service:jibDockerBuild :gateway-server:jibDockerBuild :message:jibDockerBuild
docker compose -f accounts-service/docker-compose.yaml pull kafka keycloak redis
docker compose -f accounts-service/docker-compose.yaml up -d --pull never
```

The config-server image includes the native H2 configuration. Rebuild that
image too when changing its bundled configuration files. H2 data is held in
memory and is lost when its service restarts.

Account creation publishes JSON to `send-communication`; message consumes it
through the `email|sms` function composition with consumer group `message`.
Kafka stores notifications in topics, and consumer groups track their offsets.
The functions currently log simulated email/SMS delivery.
Check delivery with `docker logs message-ms` after creating an account.

Compose also starts the upstream Keycloak service at `http://localhost:7080`.
Its local admin defaults to `admin`/`admin`; override `KEYCLOAK_ADMIN_USERNAME`
and `KEYCLOAK_ADMIN_PASSWORD` before the first start to change these.
Compose explicitly reuses the existing external volume
`accounts-service_keycloak-data` mounted at `/opt/keycloak/data`, preserving
existing realms, users, and clients. This volume must already exist and is
retained even by `docker compose down -v`. Bootstrap credentials do not change
the existing administrator's password.
Gateway writes require the matching `ACCOUNTS`, `CARDS`, or `LOANS` realm role.
The Docker gateway connects to Keycloak using its Compose service name.

See [the course branch guide](docs/course-branches.md) for matched section branches,
source commit mappings, and the section-13 work in progress.
