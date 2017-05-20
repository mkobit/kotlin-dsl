/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.script.lang.kotlin

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Receiver for `dependencies` block providing convenient utilities for configuring dependencies.
 *
 * @see DependencyHandler
 */
class DependencyHandlerScope(val dependencies: DependencyHandler) : DependencyHandler by dependencies {

    /**
     * Adds a dependency to the given configuration.
     *
     * @param dependencyNotation notation for the dependency to be added.
     * @return The dependency.
     * @see DependencyHandler.add
     */
    operator fun String.invoke(dependencyNotation: Any): Dependency =
        dependencies.add(this, dependencyNotation)

    operator fun String.invoke(dependencyNotation: String, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency =
        dependencies.add(this, dependencyNotation, dependencyConfiguration)

    /**
     * Adds a dependency to the configuration with `this` name.
     *
     * @param group the group of the module to be added as a dependency.
     * @param name the name of the module to be added as a dependency.
     * @param version the optional version of the module to be added as a dependency.
     * @param configuration the optional configuration of the module to be added as a dependency.
     * @param classifier the optional classifier of the module artifact to be added as a dependency.
     * @param ext the optional extension of the module artifact to be added as a dependency.
     * @return The dependency.
     *
     * @see DependencyHandler.create
     * @see DependencyHandler.add
     */
    operator fun String.invoke(
        group: String,
        name: String,
        version: String? = null,
        classifier: String? = null,
        ext: String? = null
    ): ExternalModuleDependency = create(group, name, version, null, classifier, ext).also { add(this, it) }

    /**
     * Adds a dependency to the configuration with `this` name and configures it.
     *
     * @param group the group of the module to be added as a dependency.
     * @param name the name of the module to be added as a dependency.
     * @param version the optional version of the module to be added as a dependency.
     * @param classifier the optional classifier of the module artifact to be added as a dependency.
     * @param ext the optional extension of the module artifact to be added as a dependency.
     * @param dependencyConfiguration expression to use to configure the dependency.
     * @return The dependency.
     *
     * @see DependencyHandler.create
     * @see DependencyHandler.add
     */
    operator fun String.invoke(
        group: String,
        name: String,
        version: String? = null,
        classifier: String? = null,
        ext: String? = null,
        dependencyConfiguration: ExternalModuleDependency.() -> Unit
    ): ExternalModuleDependency = create(group, name, version, this, classifier, ext).apply(dependencyConfiguration)

    operator fun <T : ModuleDependency> String.invoke(dependency: T, dependencyConfiguration: T.() -> Unit): T =
        dependencies.add(this, dependency, dependencyConfiguration)

    /**
     * Adds a dependency to the given configuration.
     *
     * @param dependencyNotation notation for the dependency to be added.
     * @return The dependency.
     * @see DependencyHandler.add
     */
    operator fun Configuration.invoke(dependencyNotation: Any): Dependency =
        add(name, dependencyNotation)

    operator fun Configuration.invoke(dependencyNotation: String, dependencyConfiguration: ExternalModuleDependency.() -> Unit): ExternalModuleDependency =
        add(name, dependencyNotation, dependencyConfiguration)

    operator fun <T : ModuleDependency> Configuration.invoke(dependency: T, dependencyConfiguration: T.() -> Unit): T =
        add(name, dependency, dependencyConfiguration)

    inline operator fun invoke(configuration: DependencyHandlerScope.() -> Unit) =
        configuration()
}
