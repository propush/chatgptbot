# OpenAI Mattermost bot

It only does one function: when mentioned, it responds to messages in a channel with a response from OpenAI chat completion API.

By default, the bot has name `@chatgptbot` and it responds to mentions in the channel called `chatgpt`.
To set up the bot name and the channel, set the following environment variables accordingly:
 - `bot.mattermost.mentions_1`, `bot.mattermost.mentions_N` - the names of the bot or other triggering words
 - `bot.mattermost.channels_1`, `bot.mattermost.channels_N` - the names of the channels the bot is allowed to respond

The other environment variables are:

**Mandatory:**

`YOUR_MATTERMOST_HOST` - the base url of the mattermost server, e.g. `https://mattermost.example.com`

`MATTERMOST_BOT_TOKEN` - the bot token to be used to authenticate with the mattermost server

`OPENAI_API_KEY` - the api key to be used to authenticate with the openai api. One can be obtained from https://platform.openai.com/account/api-keys

**Optional:**

`CHAT_MAX_TOKENS` - the maximum number of tokens to be generated by the openai api. Defaults to 500.

## Limitations:
 - No thread support
 - No direct message support
 - Text only i/o

## Building
```
./gradlew bootJar
```

## Running
```
CHAT_MAX_TOKENS=500 MATTERMOST_BASE_URL=<YOUR_MATTERMOST_HOST> MATTERMOST_BOT_TOKEN=<YOUR_MM_BOT_TOKEN> OPENAI_API_KEY=<YOUR_OPENAI_API_KEY> docker-compose up -d
```
or
```
CHAT_MAX_TOKENS=500 MATTERMOST_BASE_URL=<YOUR_MATTERMOST_HOST> MATTERMOST_BOT_TOKEN=<YOUR_MM_BOT_TOKEN> OPENAI_API_KEY=<YOUR_OPENAI_API_KEY> java -jar ./build/libs/openaibot-x.y.z-SNAPSHOT.jar
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](https://choosealicense.com/licenses/mit/)
