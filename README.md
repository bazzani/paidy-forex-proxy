# Paidy Forex Proxy

A local proxy for getting Currency Exchange Rates so you don't have to care about the specifics of third-party
providers.

### _Requirements_

An internal user of the application should be able to ask for an exchange rate:

1. between 2 given currencies
2. and get back a rate that is not older than 5 minutes.
3. The application should at least support 10,000 requests per day.

### Implementation steps

1. Add pom with artifact id etc. with spring boot starter parent
2. Add ForexApplication, MVP controller and Response class
3. Add spring web unit testing dep and use mockMVC to test the GET with mock data, and jacoco plugin to get code
   coverage
4. Add request validation to ensure a valid fx pair exists in the request, returning errors, unit test
5. Add a `FxRateLookup` interface to delegate the actual FX rate lookup, with a dev impl that returns mocked rate pairs,
   with unit tests
6. Include the dev service only in a dev spring profile.
7. Add a new spring profile (prod) that will have a service which will fetch exchange rates from the one-frame
   API (https://hub.docker.com/r/paidyinc/one-frame), using a Spring RestTemplate, handling timeout errors etc.
9. Add Caching using a `fxCache` DI service, that returns an `Optional<ForexResult>` based on the fx pair and
   the `now()` being < 5 mins, `orElse()` call the `fxRateLookup.getRate()` method
10. Add spring integration test that would make multiple GET calls and get at least 10,000 requests per day
    1. 10000 / 24 / 60 = 6.9 tpm

### Enhancements

1. Externalise the endpoint using properties and/or ENV variables
2. Externalise valid FX pairs to properties
3. Add _cache-busting_ querystring param to bypass `fxCache` Service
4. Run the app in a Docker container
