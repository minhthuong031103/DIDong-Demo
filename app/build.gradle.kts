plugins {
    alias(libs.plugins.androidApplication)
    id("com.apollographql.apollo3").version("4.0.0-beta.6")
}
apollo {
    service("service") {
        generateKotlinModels.set(false)
        packageName.set("com.example.rocketreserver")
    }
}

android {
    namespace = "com.mobile.moviebooking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobile.moviebooking"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.fragment:fragment:1.6.2")
    implementation("com.github.FlyingPumba:SimpleRatingBar:v0.1.5")
    implementation("com.borjabravo:readmoretextview:2.1.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.otaliastudios:zoomlayout:1.9.0")
    implementation ("com.ashokvarma.android:bottom-navigation-bar:2.2.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.apollographql.apollo3:apollo-runtime-java:4.0.0-beta.6")
    implementation ("net.danlew:android.joda:2.10.9.1")

}