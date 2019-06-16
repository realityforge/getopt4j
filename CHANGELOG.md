# Change Log

### Unreleased

### [v1.3](https://github.com/realityforge/getopt4j/tree/v1.3) (2019-06-17)
[Full Changelog](https://github.com/realityforge/getopt4j/compare/v1.2...v1.3)

* Fixed the bug where the `ParserControl` would be invoked before the first option is parsed with a `lastOptionId` of `0`. Instead the `ParserControl` should only be invoked after an option has been parsed.

### [v1.2](https://github.com/realityforge/getopt4j/tree/v1.2) (2016-06-12)
[Full Changelog](https://github.com/realityforge/giggle/compare/v1.1...v1.2)

* Add support for the '-' character to appear in the second value of multi argument
  parameter. i.e. Add support for parsing parameters such as "-Dusername=myapp-admin"

### [v1.1](https://github.com/realityforge/getopt4j/tree/v1.1) (2013-12-28)
[Full Changelog](https://github.com/realityforge/giggle/compare/aa9e01d010595ef077d9bd2ceec64ef4da06e4f7...v1.1)

* Initial re-release
