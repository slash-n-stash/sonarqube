/*
 * SonarQube
 * Copyright (C) 2009-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarqube.tests.source;

import com.sonar.orchestrator.Orchestrator;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static util.ItUtils.xooPlugin;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  EncodingTest.class,
  ScmTest.class,
  NoScmTest.class,
  ScmThenNoScmTest.class,
  SourceViewerTest.class
})
public class SourceSuite {


  @ClassRule
  public static final Orchestrator ORCHESTRATOR = Orchestrator
    .builderEnv()
    .setServerProperty("sonar.search.javaOpts", "-Xms128m -Xmx128m")
    .setSonarVersion("7.0-SNAPSHOT")
    .setOrchestratorProperty("maven.localRepository", "/tooling/maven-repository")
    .setOrchestratorProperty("sonar-scm-git-plugin-version", "1.3.0.869")
    .addPlugin(xooPlugin())
    .addMavenPlugin("org.sonarsource.scm.git", "sonar-scm-git-plugin", "sonar-scm-git-plugin-version")
    .build();

}
