Integration tests to be run as:

```
./gradlew test -Dspring.profiles.active=inttest -DOPENAI_API_KEY=<YOUR_API_KEY>
```

The rest of the tests to be run as usual:

```
./gradlew test
```
