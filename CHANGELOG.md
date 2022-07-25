# Changelog

All important changes of this project must be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased](https://github.com/bitPogo/kfixture/compare/main)

### Added

* SelectorGenerator
* RangedGenerator for:
    - Byte
    - UByte
    - Short
    - UShort
    - Int
    - UInt
    - Long
    - ULong
    - Float
    - Double
    - Char
* SignedNumberGenerator for:
    - Byte
    - Short
    - Int
    - Long
    - Float
    - Double
* RangedArrayGenerator for:
    - UByteArray
    - UShortArray
* SignedArrayNumberGenerator for:
    - ByteArray
    - ShortArray
* DependentGeneratorFactory in order to build Generators on top of other Generators

### Changed

* iterable of fixture is now called option

### Deprecated

### Removed

### Fixed

### Security

### Bumped

* Gradle 7.4.2 -> 7.5
* Kotlin 1.6.21 -> 1.7.0
* Android Target SDK 31 -> 32

## [0.2.0](https://github.com/bitPogo/kfixture/compare/v0.1.0...v0.2.0)

### Bumped

* Kotlin 1.6.10 -> 1.6.21
* AtomicFu 0.17.1 -> 0.17.3

## [0.1.0](https://github.com/bitPogo/kfixture/compare/releases/tag/v0.1.0)

Initial release.
