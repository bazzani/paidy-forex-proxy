### Implementation steps

1. Add pom with artifact id etc. Spring boot starter parent, and Maven wrapper
2. Add Spring boot application (`ForexApplication`), MVP controller, Response class, and JaCoCo support
3. Add One-frame integration, local FX mock service, input validation, caching, and error handling
    1. Add request validation to ensure a valid fx pair exists in the request, returning errors if unsupported
       currencies detected
    2. Add a `FxRateLookup` interface to delegate the actual FX rate lookup, with a local dev impl that returns mocked
       rate pairs
    3. Add a new spring profile (`oneframe`) that will have a `@Primary` bean which will fetch exchange rates from the
       one-frame API (https://hub.docker.com/r/paidyinc/one-frame)
    4. Add Caching using a `@Component` that returns an `Optional<ForexResult>` based on the fx pair and `now()`
       being < 5 mins, `orElse()` call the `FxRateLookup.getRate()` method to get the data from the backend API.
