/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2018, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.integration.test;


import static schemacrawler.test.utility.TestUtility.flattenCommandlineArgs;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import schemacrawler.Main;
import schemacrawler.test.utility.BaseDatabaseTest;
import schemacrawler.test.utility.TestWriter;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.OutputOptionsBuilder;

public class TemplatingIntegrationTest
  extends BaseDatabaseTest
{

  @Test
  public void commandlineFreeMarker()
    throws Exception
  {
    executeCommandlineAndCheckForOutputFile("freemarker",
                                            "plaintextschema.ftl",
                                            "executableForFreeMarker");
  }

  @Test
  public void commandlineThymeleaf()
    throws Exception
  {
    executeCommandlineAndCheckForOutputFile("thymeleaf",
                                            "plaintextschema.thymeleaf",
                                            "executableForThymeleaf");
  }

  @Test
  public void commandlineVelocity()
    throws Exception
  {
    executeCommandlineAndCheckForOutputFile("velocity",
                                            "plaintextschema.vm",
                                            "executableForVelocity");
  }

  @Test
  public void executableFreeMarker()
    throws Exception
  {
    executeExecutableAndCheckForOutputFile("freemarker",
                                           "plaintextschema.ftl",
                                           "executableForFreeMarker");
  }

  @Test
  public void executableThymeleaf()
    throws Exception
  {
    executeExecutableAndCheckForOutputFile("thymeleaf",
                                           "plaintextschema.thymeleaf",
                                           "executableForThymeleaf");
  }

  @Test
  public void executableVelocity()
    throws Exception
  {
    executeExecutableAndCheckForOutputFile("velocity",
                                           "plaintextschema.vm",
                                           "executableForVelocity");
  }

  private void executeCommandlineAndCheckForOutputFile(final String command,
                                                       final String outputFormatValue,
                                                       final String referenceFileName)
    throws Exception
  {
    try (final TestWriter out = new TestWriter("text");)
    {
      final Map<String, String> argsMap = new HashMap<>();
      argsMap.put("url", "jdbc:hsqldb:hsql://localhost/schemacrawler");
      argsMap.put("user", "sa");
      argsMap.put("password", "");
      argsMap.put("infolevel", "standard");
      argsMap.put("command", command);
      argsMap.put("sortcolumns", "true");
      argsMap.put("outputformat", outputFormatValue);
      argsMap.put("outputfile", out.toString());

      Main.main(flattenCommandlineArgs(argsMap));

      out.assertEquals(referenceFileName + ".txt");
    }
  }

  private void executeExecutableAndCheckForOutputFile(final String command,
                                                      final String outputFormatValue,
                                                      final String referenceFileName)
    throws Exception
  {
    final SchemaCrawlerExecutable executable = new SchemaCrawlerExecutable(command);
    try (final TestWriter out = new TestWriter(outputFormatValue);)
    {
      final OutputOptions outputOptions = OutputOptionsBuilder
        .newOutputOptions(outputFormatValue, out);

      executable.setOutputOptions(outputOptions);
      executable.setConnection(getConnection());
      executable.execute();

      out.assertEquals(referenceFileName + ".txt");
    }
  }
}
