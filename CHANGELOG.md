## Unreleased

* Fixed the bug where the `ParserControl` would be invoked before the first option is parsed with a `lastOptionId` of `0`. Instead the `ParserControl` should only be invoked after an option has been parsed.   

## 1.2:

* Add support for the '-' character to appear in the second value of multi argument
  parameter. i.e. Add support for parsing parameters such as "-Dusername=myapp-admin"

## 1.1:

* Initial re-release
