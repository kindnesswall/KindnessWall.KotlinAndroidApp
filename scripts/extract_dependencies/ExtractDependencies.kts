import java.io.File
import java.lang.ProcessBuilder.Redirect
import java.util.concurrent.TimeUnit

val requiredConfigurations = listOf(
    "kapt",
    "stagingDebugRuntimeClasspath"
)

requiredConfigurations
    .map { configuration ->
        "./gradlew :app:dependencies --configuration $configuration > scripts/extract_dependencies/$configuration.txt"
    }.forEach { command ->
        command.runCommand()
    }

// https://github.com/ProtonMail/proton-mail-android/blob/977215963d52c989a93a58d3e637b5e8a6124682/scripts/extract_dependencies/ExtractDeps.kts#L217
fun String.runCommand(workingDir: File = File("./")) {
    val parts = split(" > ")
    ProcessBuilder(parts[0].split("\\s".toRegex()))
        .directory(workingDir).apply {
            if (parts.size > 1) redirectOutput(File(parts[1]))
            else redirectOutput(Redirect.INHERIT)
        }
        .redirectError(Redirect.INHERIT)
        .start()
        .waitFor(10, TimeUnit.MINUTES)
}