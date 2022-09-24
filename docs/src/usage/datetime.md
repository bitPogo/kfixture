# kotlinx-datetime Extension
## Setup
To make the extension available in your code add:

```kotlin
testImplementation("tech.antibytes.kfixture:ktx-datetime:$KFixtureVersion")
```

Please not that this Extension is dependent on [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime).

If all requirements are fulfilled you can hook in the Extension via `useDatTimeWithOnlyUtc` or `useDateTimeWithTimeZones` like:

```kotlin
val fixture = kotlinFixture {
    useDateTimeWithOnlyUtc()
}
```

The difference between `useDatTimeWithOnlyUtc` and `useDateTimeWithTimeZones` depends on Javascript as described [here](https://github.com/Kotlin/kotlinx-datetime#note-about-time-zones-in-js).
In opposite to kotlinx-datetime KFixture will either only support UTC or all [(joda) timezones](https://www.npmjs.com/package/@js-joda/timezone).

!!!note:
    The timezones from platform to platform differ

## Supported Types
The Extension brings the following additional types:

| Type           |
| -------------- |
|  Instant  |
|  DayOfWeek  |
|  Month  |
|  TimeZone  |
|  DateTimePeriod  |
|  DatePeriod  |
|  UtcOffset  |
|  FixedOffsetTimeZone  |
|  LocalTime  |
|  LocalDate  |
|  LocalDateTime  |

## Additional Extension Functions

While all types are implemented as Generators; `LocalTime`, `LocalDate` and `LocalDateTime` have a additional Extension Function, which allows nested Generators for its Instant and TimeZone:

```kotlin
val dateTime: LocalDateTime = fixture.fixture(
    instantGenerator = { Instant.fromEpochMilliseconds(502131) },
    timeZoneGenerator = { TimeZone.of("Europe/Berlin") },
)
```

Both nested generators are optional.


## Extension Overview

| Type                |  Range  | Filterable | Nested DateTime Generator |
|---------------------| ------- |------------|---------------------------|
| Instant             |   ✅    | ✅          | ❌                        |
| DayOfWeek           |   ❌    | ✅          | ❌                        |
| DateTimePeriod      |   ❌    | ✅          | ❌                        |
| DatePeriod          |   ❌    | ✅          | ❌                        |
| UtcOffset           |   ❌    | ✅          | ❌                        |
| FixedOffsetTimeZone |   ❌    | ✅          | ❌                        |
| LocalTime           |   ❌    | ❌          | ✅                        |
| LocalDate           |   ❌    | ❌          | ✅                        |
| LocalDateTime       |   ❌    | ❌          | ✅                        |
