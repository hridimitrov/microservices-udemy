# Course branches

Each `course/section-*` branch checks out matching versions of all modules introduced by that section.
The original remote branches were fetched before matching. Exact source commits are in
[course-branches.json](course-branches.json). No original remote was changed.

Section topics follow the [instructor repository](https://github.com/eazybytes/microservices).
These are your saved checkpoints, not a claim that each course section is complete.

| Section | Combined branch |
| --- | --- |
| 5 | `course/section-05-profiles` |
| 6 | `course/section-06-spring-cloud-config` |
| 7 | `course/section-07-mysql-database` |
| 8 | `course/section-08-service-discovery` |
| 9 | `course/section-09-gateway` |
| 10 | `course/section-10-circuit-breaker` |
| 11 | `course/section-11-observability-and-monitoring` |
| 12 | `course/section-12-security` |
| 13 | `course/section-13-event-driven` |

## Source matching

A dash means that the module had not been introduced yet and is omitted from that section branch.

| Module | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| accounts-service | `main` | `spring-cloud-config` | `mysql-database` | `service-discovery-and-registration` | `gateway-server` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| cards-service | `main` | `spring-cloud-config` | `mysql-database` | `service-discovery-and-registration` | `gateway-server` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| loans-service | `main` | `spring-cloud-config` | `mysql-database` | `service-discovery-and-registration` | `gateway-server` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| config-server | — | `spring-cloud-config` | `mysql-database` | `service-discovery-and-registration` | `dfe485c` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| eureka-server | — | — | — | `service-discovery-and-registration` | `circuit-breaker` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| gateway-server | — | — | — | — | `main` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |
| message | — | — | — | — | — | — | — | — | `main` |
| product-configs | — | `main` | `main` | `service-discovery-and-registration` | `gateway-server` | `circuit-breaker` | `observability-and-monitoring` | `security` | `security` |

## Matching decisions and limits

- Section 5: Profile baseline from the three original main branches; image tags already say s6.
- Section 5: Config server, discovery, gateway, and messaging are not introduced yet.
- Section 6: product-configs has no spring-cloud-config branch; main is the config-server target.
- Section 6: The config repository main already contains MySQL settings; this is the original saved state.
- Section 7: product-configs/main contains MySQL configuration; no mysql-database branch exists there.
- Section 9: Config Server dfe485c selects product-configs/gateway-server and precedes its circuit-breaker changes.
- Section 9: Eureka circuit-breaker only updates its image tag from s8 to s9; it is also the gateway-stage version.
- Section 9: gateway-server/main is the gateway checkpoint; its circuit-breaker branch adds resilience later.
- Section 10: Circuit-breaker sources still use s9 image tags; code and course topic identify section 10.
- Section 10: cards-service circuit-breaker and gateway-server point to the same commit.
- Section 10: product-configs circuit-breaker and gateway-server point to the same commit.
- Section 11: The sources include a later H2 migration; preserved as saved.
- Section 11: Some Docker Compose references still use s9 despite s11 build image tags.
- Section 12: product-configs/security and observability-and-monitoring point to the same commit.
- Section 12: Some Docker Compose references still use s9 despite s12 build image tags.
- Section 13: Work in progress: combines the section-12 services with message/main.
- Section 13: No section-13 branch exists in the other service repositories.
- Section 13: This does not invent accounts-to-message integration that has not been implemented.

## Working with a section

```powershell
git switch course/section-10-circuit-breaker
.\gradlew.bat projects
.\gradlew.bat build -x test
git switch course/section-13-event-driven
```

Reload the Gradle project in IntelliJ after switching sections because the module list changes.
The root wrapper and multi-project build are retained on every combined branch.
Source files and Docker image tags are preserved; starting containers may use older published images.
Use module-qualified `bootRun` tasks when you want to run the checked-out source.

For sections with a config server, serve the matched local product-configs module explicitly:

```powershell
.\gradlew.bat :config-server:bootRun --args="--spring.profiles.active=native --spring.cloud.config.server.native.search-locations=file:D:/Development/microservices-udemy/product-configs/"
```

Without that override, the config server uses the original branch configuration (GitHub or bundled native files).
MySQL, RabbitMQ, Redis, security providers, and monitoring requirements still depend on the selected section.
Context tests may require those services. Branch matching does not launch them.

## Original Git history

Every fetched original branch is retained locally under `refs/archive/<module>/<branch>`.
For example: `git log refs/archive/accounts-service/circuit-breaker`.
Combined snapshot commits also include their selected source tips as merge parents, preserving original
commit objects and authors when those combined branches are pushed. Original commits retain their original paths;
use `git log --first-parent` to view the combined section progression.
The source branch names and commit IDs remain recorded in the manifest.

Branches are created locally. Nothing is pushed automatically.
To publish the combined branches when ready: `git push -u origin "refs/heads/course/*:refs/heads/course/*"`.
