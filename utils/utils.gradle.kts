/*
 * Copyright 2021 ByteLegend Technologies and the original author or authors.
 *
 * Licensed under the GNU Affero General Public License v3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://github.com/ByteLegend/ByteLegend/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.bytelegend.buildsupport.OpenSourceLibrary

plugins {
    kotlin("jvm")
    id("configure-ktlint")
    id("json2Java")
    id("buildGameResources")
}

val libs: (String) -> String by rootProject.ext
val oss: List<OpenSourceLibrary> by rootProject.ext

dependencies {
    implementation(project(":shared"))
    implementation(platform(libs("libraries-bom")))
    implementation("com.google.cloud:google-cloud-translate")
    implementation(libs("core-kotlin"))
    implementation(libs("java-jwt"))
    implementation(libs("bcprov-jdk15on"))
    implementation(libs("jackson-dataformat-yaml"))
    implementation(libs("jackson-module-kotlin")) {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation(libs("spring-core"))
    implementation(libs("opencc4j"))
    implementation(libs("kotlin-reflect"))
    implementation(libs("kotlinx-serialization-json"))
    implementation(libs("batik-svggen"))
    implementation(libs("batik-dom"))
    implementation(libs("batik-swing"))
    implementation(libs("commonmark"))

    testImplementation(libs("junit-jupiter-api"))
    testImplementation(libs("junit-jupiter-engine"))
    testImplementation(libs("junit-jupiter-params"))
}

fun registerJavaExecInRootProject(name: String, mainClassName: String, action: JavaExec.() -> Unit): TaskProvider<JavaExec> = tasks.register<JavaExec>(name) {
    classpath = project.sourceSets["main"].runtimeClasspath
    workingDir = rootProject.rootDir
    mainClass.set(mainClassName)
    action()
}

registerJavaExecInRootProject("createNewMap", "com.bytelegend.utils.CreateNewMapKt") {
    jvmArgs(
        "-DmapId=${System.getProperty("mapId") ?: throw IllegalArgumentException("No mapId!")}",
        "-DmapGridWidth=${System.getProperty("mapGridWidth") ?: throw IllegalArgumentException("No mapGridWidth!")}",
        "-DmapGridHeight=${System.getProperty("mapGridHeight") ?: throw IllegalArgumentException("No mapGridHeight!")}",
        "-Dapple.awt.UIElement=true"
    )
}

registerJavaExecInRootProject("createEmptyMissionYamls", "com.bytelegend.utils.CreateEmptyMissionYamlsKt") {
    jvmArgs(
        "-DmapId=${System.getProperty("mapId") ?: throw IllegalArgumentException("No mapId!")}",
        "-Dapple.awt.UIElement=true"
    )
}

val checkLicenses = registerJavaExecInRootProject("checkLicenses", "com.bytelegend.utils.CheckLicensesKt") {}
registerJavaExecInRootProject("addLicenses", "com.bytelegend.utils.AddLicensesKt") {}

tasks.named("check") {
    dependsOn(checkLicenses)
}


