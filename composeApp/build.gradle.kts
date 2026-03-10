import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("org.eclipse.jgit:org.eclipse.jgit:6.10.0.202406032230-r")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.13.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            runtimeOnly("org.slf4j:slf4j-simple:2.0.13")
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.pt.MainKt"

        jvmArgs(
            "-Dorg.slf4j.simpleLogger.defaultLogLevel=warn"
        )

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Project Text"
            packageVersion = "1.0.0"
            description = "Utility for exporting selected project files into a single text output"
        }
    }
}
