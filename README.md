# Banking Java
[![CodeFactor](https://www.codefactor.io/repository/github/danielr1996/banking-java/badge)](https://www.codefactor.io/repository/github/danielr1996/banking-java)

## Available Profiles
- db (Active by default, setup common db parameters)
- db-h2 (Active by default, setup h2 specific parameters)
- db-pg (Setup postgresql specific parameters)
- fints-mock: Mock calls to FinTS API
- fints-prod: Use Live FinTS API
- hbcicallback-wamp: Use WAMP to get user data from user
- hbcicallback-console: Get user data from console manually

## Available Test Tags
### By Test Depth
- @Tag("integration"),
- @Tag("unit")

### By Infrastracture
- @Tag("graphql"),

### By Layer
- @Tag("application"),
- @Tag("authentication"),
- @Tag("authorization")

### Etc
- @Tag("arch")
