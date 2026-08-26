plugins {
    kotlin("jvm") version "2.2.21"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)

    sourceSets {
        main {
            kotlin.srcDirs(
                "Semana01",
                "Semana02",
                "Semana03",
                "Semana04",
                "Semana05",
                "Semana06",
                "Semana07",
                "Semana08",
                "Semana09",
                "Semana10",
                "Semana11",
                "Semana12",
                "Semana13",
                "Semana14",
                "Semana15",
                "Semana16"
            )
        }
    }
}
