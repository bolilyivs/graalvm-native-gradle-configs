# Create native awt/swing application

## Create icon

1. Create app.rs from app.ico:

app.rs:
```
IDI_ICON_1 ICON "app.ico"
```
2. Create app.res from app.rs:
```cmd
C:\'Program Files (x86)'\Windows Kits\10\bin\10.0.26100.0\x64\rc.exe app.rc
```
3. Result:
```
app_name.res
```
4. add to "-H:NativeLinkerOption=...app_name.res"

## Use app with graalvmNative agent
1. Check:
```kotlin
graalvmNative {
    binaries {
        agent {
            enabled.set(true)
        }
    }
}
```
2. Run agent
```cmd
./gradlew -Pagent run
```
3. Copy reachability-metadata.json to /src/main/resources/META-INF
