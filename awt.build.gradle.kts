plugins {
    id("java")
    id("application")
    id("org.graalvm.buildtools.native") version "0.11.3"
}

group = "org.example"
version = "1.0-SNAPSHOT"


val appIconRes = "$projectDir/icon/app.res"
val isWindows = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")


repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.formdev:flatlaf:3.7")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

graalvmNative {
    binaries {
        named("main") {
            mainClass = "org.example.Main"
            debug.set(true)
            verbose.set(true)
            fallback.set(false)
            sharedLibrary.set(false)
            richOutput.set(true)
            quickBuild.set(false)
            buildArgs.add("-Djava.awt.headless=false")
            if(isWindows){
                buildArgs.add("-H:NativeLinkerOption=/SUBSYSTEM:WINDOWS")
                buildArgs.add("-H:NativeLinkerOption=/ENTRY:mainCRTStartup")
                if(Objects.nonNull(appIconRes)){
                    buildArgs.add("-H:NativeLinkerOption=$appIconRes")
                }
            }
            useFatJar.set(false)
        }
        agent {
            enabled.set(false)
            defaultMode.set("standard")
            metadataCopy {
                inputTaskNames.add("run")
                outputDirectories.add("src/main/resources/META-INF/native-image/")
                mergeWithExisting.set(true)
            }
            builtinCallerFilter.set(true)
            builtinHeuristicFilter.set(true)
            enableExperimentalPredefinedClasses.set(true)
            enableExperimentalUnsafeAllocationTracing.set(true)
            trackReflectionMetadata.set(true)
            tasksToInstrumentPredicate.set({ true })
        }
        binaries.all {
            resources.autodetect()
        }
    }
}

application {
    mainClass.set("org.example.Main")
}
