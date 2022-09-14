# Changelog

All important changes of this project must be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased](https://github.com/bitPogo/kfixture/compare/v0.3.1...main)

### Added

* Support for KTX DateTime:
  - Instant
  - DayOfWeek
  - Month
  - TimeZone
  - DateTimePeriod
  - DatePeriod

### Changed

### Deprecated

### Removed

### Fixed

### Security

### Bumped

## [0.3.1](https://github.com/bitPogo/kfixture/compare/v0.3.0...v0.3.1)

### Fixed

* Dangled Signatures

## [0.3.0](https://github.com/bitPogo/kfixture/compare/v0.2.0...v0.3.0)

### Added

* SelectorGenerator
* RangedGenerator for:
  - UByte
  - Short
  - UShort
  - UInt
  - ULong
  - Float
  - Char
* SignedNumberGenerator for:
  - Byte
  - Short
  - Int
  - Long
  - Double
* RangedArrayGenerator for:
  - UByteArray
  - UShortArray
  - UIntArray
  - ULongArray
  - FloatArray
  - CharArray
  - String
* SignedArrayNumberGenerator for:
  - ByteArray
  - ShortArray
  - IntArray
  - LongArray
  - DoubleArray
* BooleanArrayGenerator is now a ArrayGenerator
* DependentGeneratorFactory in order to build Generators on top of other Generators
* Filter
* custom generator for Generics
* support for Enums

### Changed

* iterable of fixture is now called option

### Bumped

* Gradle 7.4.2 -> 7.5
* Kotlin 1.6.21 -> 1.7.0
* AtomicFu 0.17.3 -> 0.18.2
* Stately 1.2.1 -> 1.2.3
* Android Target SDK 31 -> 32

## [0.2.0](https://github.com/bitPogo/kfixture/compare/v0.1.0...v0.2.0)

### Bumped

* Kotlin 1.6.10 -> 1.6.21
* AtomicFu 0.17.1 -> 0.17.3

## [0.1.0](https://github.com/bitPogo/kfixture/compare/releases/tag/v0.1.0)

Initial release.
