// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {

    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    dependencies {

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.49")
        classpath ("com.google.gms:google-services:4.4.0")
        classpath("com.android.tools.build:gradle:8.1.4")
    }

}




