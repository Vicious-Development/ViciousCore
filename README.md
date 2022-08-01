# ViciousCore
Vicious Core is a powerful mod designed to eliminate the tediousness of many processes in modding Minecraft while keeping everything as server efficient as possible. 
Provides:
## Common
- Simple data syncing between client and server.
- Common keybindings which sync their state on the client and server.
- All features provided in ViciousLib

## Client
- Relativistic GUI system.
- Holoinv GUI

# Using as a Gradle Dependency
1. Put this in your build script
```gradle
repositories {
    maven {
        name = "ViciousCore"
        url = uri("https://maven.pkg.github.com/Vicious-MCModding/ViciousCore")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GPR_USER")
            password = project.findProperty("gpr.key") ?: System.getenv("GPR_API_KEY")
        }
    }
}
dependencies {
    compile "com.vicious:viciouscore:MCVER-VCVER"
}
```
2. If you don't have a GPR Key you need to make one. To do so, go here: [https://github.com/settings/tokens](https://github.com/settings/tokens)
* Click generate new token
* Click the read:packages checkbox
* Scroll down and generate that boyo. Copy the key, and in gradle.properties write
```
gpr.user=YOUR GITHUB USERNAME
gpr.key=THE KEY YOU JUST MADE.
```
* You should be good now BUT be warned. This key grants anyone with it special privileges (the ones you gave it in that checkbox section). Make sure that this key is kept private, you'll want your git system to ignore the gradle.properties file in this case. YOU SHOULD just use the System Environment variables as well.

Doing this will both provide you ALL dependencies for core and any of the dependencies for the dependencies (Wow its like magic).
