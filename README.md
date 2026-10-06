# Server Test Tools

A small client-side Fabric mod for Minecraft Java 1.21.11. It provides a configurable in-game menu and ordinary HUD readouts for use in single-player or private-server testing.

## Included

- Open the menu with **Shift + Space**.
- Toggle player coordinates, FPS, and the coordinates of the player's current chunk.
- The chunk readout only reports the chunk the player is currently standing in. It does not search hidden or unloaded chunks.
- No combat automation, packet manipulation, or anti-cheat bypass is included.

## Requirements

- Java Development Kit 21.
- Gradle 8.14 or newer.
- Minecraft Java 1.21.11 with Fabric Loader and Fabric API 0.141.6 installed in the same game profile.

## Build

From this project folder, run:

```powershell
gradle build
```

The mod jar will be written to `build/libs/server-test-tools-1.0.0.jar`. Copy that jar into the `mods` folder for the Fabric game profile. The server does not need this client-only mod installed.

If `gradle` is not recognized, install Gradle 8.14+ and ensure Java 21 is selected with `java -version`.
