# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iGoCFJf0EFWkGh1VMKbN+Vq7q6QupndqV2OW1AA1KZa7d+IQvuHhEaFAkIDzESxv3eQvqevl9DV0z3l8fsa5zManUYAAxKx4gAWWNJ9sTdLd0XEQ8UAAHndE9DiSKA9BQPJYOPF1T2ONBKCVC1fXQUYlwg18IjZDlfkzcM-UqO97TFGio26KDz3A2V3WCccERooj-RHBDAh4iI+OzFiuP8eMk1TGB0zEv1czQfNtF0AxjB0FA7UrLR9GYWtvF8TApKbXpWz4T43iSN40nSLsJB7PIFMjEc2PYCJJ10lVRmctB1h4p4kLlY4wGVLM-R8wjs1InVyPfb5qKiuiYAAKgAoDQOYoKgxEmjbgwgKsI3U8YE8vTIvC9AYtdXUmWsN5gPiAA1N4w349KQLDDiaq4kSYBTZNYJy7cZMGkywCEkcIgGjC8wLNTi0GGQK2GGAAHEeUeAz62MxtmA9LoomiNarNsrQeScpKXO3Yaugk7jPQREKNrKCQKtoqrNEK4IgoiEKwo+mdfOql9gheeLP18yo0sAzqsuw+lese0SLXygTHqK59DmezaSO60G9Qh35ztejrQJJyRYKE36KekHikcO2mWKDcbRvTWn2kwObVKLYxsD0KBsAQVQ4HxXx1p5DxDIbAJ9ubQ6YgSZIzou0wrt7AoKdZHlh23NyYNch7oIRBUCRelBRi1nlKmBobMYgv6TgB-j3v4kHEbBt9LMh9XobJrrsoZ+EUdVNHMJ+hG5Tw9xzctnltdNCnmT4d23U9iiDSNK3E55ZPGJgJmeKEmAYF+xw0MwtjIiT-MRxZvaYGm1N2dzmQUk0bnC3UoxzBQFEIHcGAACkIGnCWymMBQEFAa1dtlhkpsV04O3SCnLsqjX8nGuAIEnKBKiTvhda6fWEQAK1HtBY5APad73g-c74cpbbtiPiuxp31dd6L8Y98HvcShvP2sNMpOkDqfEOngw70zfljaQgdngaCLohSO4caYnFjofVOtVXgAILo-f2hdHoIRpjyZmXEzI7hgTxNBkcIgGDkL4WI09e5QDehTVO5FTixD4MIayVQWGUDvtAR4IoxREONtTOh5cOD0yrlACIMj7qs36qmLmyl5q8yMDoYAlhECKlgMAbAQtCDOFcFLHa40F4jRiBZL41lbJrkNiJGQfcTgoFuHjGBv1XHC18J47BPQ9RWAas1VqaVgQQFBClKmKD35yiUXIo254m6DTrpJPaqSO7KSAA