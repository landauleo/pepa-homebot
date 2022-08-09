# pepa-homebot

Для локального тестирования (**macOS**):
- запусти mongodb ```docker run -ti --rm -p 27017:27017 mongo:4.0```
- скачай и запусти [ngrok](https://dashboard.ngrok.com/get-started/setup) ```ngrok http --host-header=rewrite localhost:8088```
- узнай новый адрес и подставь его в application.properties -> homebot.webhook
- зарегистрируй вебхук с новым адресом от ngrok, вбив в адресную строку ```https://api.telegram.org/bot<bot-token>/setWebhook?url=<ngrok-url>```
- запусти приложение -> _Main_
- наблюдай за обработкой запросов по адресу ```http://127.0.0.1:4040/inspect/http```

## Диалог с Пепой:
- команда ```/start```
  ![start](/src/main/resources/static/1.png)
  
- невалидная ссылка на недвижимость
  ![start](/src/main/resources/static/2.png)

  
- валидная ссылка на недвижимость
  ![start](/src/main/resources/static/3.png)
  
  
- ~~токсичное отношение к Пепе~~ неожидаемое сообщение 
  ![start](/src/main/resources/static/4.png)
  

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/pepa-homebot-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
