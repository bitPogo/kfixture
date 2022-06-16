package tech.antibytes.gradle.kfixture.script


/**
 * Precompiled [quality-spotless.gradle.kts][tech.antibytes.gradle.kfixture.script.Quality_spotless_gradle] script plugin.
 *
 * @see tech.antibytes.gradle.kfixture.script.Quality_spotless_gradle
 */
class QualitySpotlessPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("tech.antibytes.gradle.kfixture.script.Quality_spotless_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
