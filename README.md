# KFixture
A tool to generate randomized test values for Kotlin Multiplatform.

[![Latest release](https://raw.githubusercontent.com/bitPogo/kfixture/main/docs/src/assets/badge-release-latest.svg)](https://github.com/bitPogo/kfixture/releases)
[![License](https://raw.githubusercontent.com/bitPogo/kfixture/main/docs/src/assets/badge-license.svg)](https://github.com/bitPogo/kfixture/blob/main/LICENSE)
[![Platforms](https://raw.githubusercontent.com/bitPogo/kfixture/main/docs/src/assets/badge-platform-support.svg)](https://github.com/bitPogo/kfixture/blob/main/docs/src/assets/badge-platform-support.svg)
[![CI - Build Snapshot Version](https://github.com/bitPogo/kfixture/actions/workflows/ci-latest-version.yml/badge.svg)](https://github.com/bitPogo/kfixture/actions/workflows/ci-latest-version.yml/badge.svg)

## About The Project
Tired of writing potato or 42 for your test values? - KFixture is a (limited) extendable generator test value generator for Kotlin Multiplatform which can help you out there.
The project is heavily influenced by [appmattus kotlinfixture](https://github.com/appmattus/kotlinfixture) and was started to ease a bit testing on KMP.
If you want to know more take a look into the [Documentation](https://bitpogo.github.io/kfixture/).

## Dependencies

KMock has the following dependencies:

* [AndroidGradlePlugin (AGP) 7.2.1](https://developer.android.com/studio/releases/gradle-plugin)
* [Kotlin 1.6.10](https://kotlinlang.org/docs/releases.html)
* [AtomicFu 0.17.1](https://github.com/Kotlin/kotlinx.atomicfu)
* [Touchlab's Stately 1.2.1](https://github.com/touchlab/Stately)
* [Gradle 7.4.2](https://gradle.org/)

## Additional Requirements

* Android 6.0 (API 21) to Android 12 (API 31)
* [Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)

## Changelog

See [changelog](https://github.com/bitPogo/kmock/blob/main/CHANGELOG.md).

## Versioning

This project uses [Semantic Versioning](http://semver.org/) as a guideline for our versioning.

## Contributing

You want to help or share a proposal? You have a specific problem? Read the following:

* [Code of Conduct](https://github.com/bitPogo/kfixture/blob/main/CODE_OF_CONDUCT.md) for details on our code of conduct.
* [Contribution Guide](https://github.com/bitPogo/kfixture/blob/main/CONTRIBUTING.md) for details about how to report bugs and propose features.

## Releasing

Please take a look [here](https://github.com/bitPogo/kfixture/tree/main/docs/src/development/releasing.md).

## Copyright and License

Copyright (c) 2022 Matthias Geisler / All rights reserved.

Please refer to the [License](https://github.com/bitPogo/kfixture/blob/main/LICENSE) for further details.