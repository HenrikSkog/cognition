# Integration tests

## Description

The `integration_tests` module tests and verifies that the client application can successfully communicate with a
running Spring Boot web server from the [`api`](../api) module.

The group reflected upon adding more test, i.e. one integration test per view. We deemed this redundant, compared to
simply testing with one view due to the following reasons:

- The integration tests should not **primarily** test actual UI functionality. This is handled in the [`ui`](../ui)
  module.
- The same instance of our [`RemoteCognitionAccess`](../ui/src/main/java/ui/RemoteCognitionAccess.java) - found in
  the `ui` module - is passed around when switching views and consequently presentation layer controllers.
