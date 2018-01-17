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
package org.sonar.server.computation.task.projectanalysis.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SourceLinesDiffFinderTest {

  @Test
  public void shouldFindNothingWhenContentAreIdentical() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");
    database.add("line - 4");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2");
    report.add("line - 3");
    report.add("line - 4");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).isEmpty();

  }

  @Test
  public void withCodeSample() {

    List<String> database = new ArrayList<>();
    database.add("package sample;\n");
    database.add("\n");
    database.add("public class Sample {\n");
    database.add("\n");
    database.add("    private String myMethod() {\n");
    database.add("    }\n");
    database.add("}\n");

    List<String> report = new ArrayList<>();
    report.add("package sample;\n");
    report.add("\n");
    report.add("public class Sample {\n");
    report.add("\n");
    report.add("    private String attr;\n");
    report.add("\n");
    report.add("    public Sample(String attr) {\n");
    report.add("        this.attr = attr;\n");
    report.add("    }\n");
    report.add("\n");
    report.add("    private String myMethod() {\n");
    report.add("    }\n");
    report.add("}\n");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(5, 6, 7, 8, 10, 11, 12);

  }

  @Test
  public void withHashSample() {

    List<String> database = new ArrayList<>();
    database.add("ab5b7c2a665690e27c843b6626b903fd");
    database.add("");
    database.add("85ae3e3528f8b5d047b5a873ec1720a8");
    database.add("");
    database.add("d5d98abc7611352085067faf7aa09dd2");
    database.add("cbb184dd8e05c9709e5dcaedaa0495cf");
    database.add("cbb184dd8e05c9709e5dcaedaa0495cf");

    List<String> report = new ArrayList<>();
    report.add("ab5b7c2a665690e27c843b6626b903fd");
    report.add("");
    report.add("85ae3e3528f8b5d047b5a873ec1720a8");
    report.add("");
    report.add("7e833fcde2dd6fe69d35a9bd85386454");
    report.add("");
    report.add("3778499626e706f3343fd47012209d53");
    report.add("059b2bbe4a2d47622d9637660b5cbd79");
    report.add("cbb184dd8e05c9709e5dcaedaa0495cf");
    report.add("");
    report.add("d5d98abc7611352085067faf7aa09dd2");
    report.add("cbb184dd8e05c9709e5dcaedaa0495cf");
    report.add("cbb184dd8e05c9709e5dcaedaa0495cf");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(5, 6, 7, 8, 10, 11, 12);

  }

  @Test
  public void shouldDetectWhenStartingWithModifiedLines() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");

    List<String> report = new ArrayList<>();
    report.add("line - 0 - modified");
    report.add("line - 1 - modified");
    report.add("line - 2");
    report.add("line - 3");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(1, 2);

  }

  @Test
  public void shouldDetectWhenEndingWithModifiedLines() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2 - modified");
    report.add("line - 3 - modified");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(3, 4);

  }

  @Test
  public void shouldDetectModifiedLinesInMiddleOfTheFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");
    database.add("line - 4");
    database.add("line - 5");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2 - modified");
    report.add("line - 3 - modified");
    report.add("line - 4");
    report.add("line - 5");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(3, 4);

  }

  @Test
  public void shouldDetectNewLinesAtBeginningOfFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");

    List<String> report = new ArrayList<>();
    report.add("line - new");
    report.add("line - new");
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(1, 2);

  }

  @Test
  public void shouldDetectNewLinesInMiddleOfFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - new");
    report.add("line - new");
    report.add("line - 2");
    report.add("line - 3");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(3, 4);

  }

  @Test
  public void shouldDetectNewLinesAtEndOfFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2");
    report.add("line - new");
    report.add("line - new");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).containsExactlyInAnyOrder(4, 5);

  }

  @Test
  public void shouldIgnoreDeletedLinesAtEndOfFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");
    database.add("line - 4");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 2");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).isEmpty();

  }

  @Test
  public void shouldIgnoreDeletedLinesInTheMiddleOfFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");
    database.add("line - 4");
    database.add("line - 5");

    List<String> report = new ArrayList<>();
    report.add("line - 0");
    report.add("line - 1");
    report.add("line - 4");
    report.add("line - 5");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).isEmpty();

  }

  @Test
  public void shouldIgnoreDeletedLinesAtTheStartOfTheFile() {

    List<String> database = new ArrayList<>();
    database.add("line - 0");
    database.add("line - 1");
    database.add("line - 2");
    database.add("line - 3");

    List<String> report = new ArrayList<>();
    report.add("line - 2");
    report.add("line - 3");

    Set<Integer> diff = new SourceLinesDiffFinder(database, report).findNewOrChangedLines();

    assertThat(diff).isEmpty();

  }

}
