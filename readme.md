Integration tests to be run as:

```
./gradlew test -Dspring.profiles.active=inttest -DOPENAI_API_KEY=<YOUR_API_KEY> -DMATTERMOST_BASE_URL=https://mattermost.com -DMATTERMOST_BOT_TOKEN=<YOUR_MM_BOT_TOKEN> -DMATTERMOST_TEAM=<YOUR_TEAM_NAME>
```

The rest of the tests to be run as usual:

```
./gradlew test
```
