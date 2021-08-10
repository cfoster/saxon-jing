/**
 * Copyright 2016 Charles Foster
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cfoster.saxonjing;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.Initializer;
import net.sf.saxon.s9api.Processor;

import javax.xml.transform.TransformerException;

public class JingInitializer implements Initializer
{
  public void initialize(Configuration config) throws TransformerException
  {
    // 2021-08-10 jkalvesmaki: commented out to work with Saxon 9.9+
    // Processor proc = (Processor)config.getProcessor();
    // Processor proc =
    // if(proc == null)
    // 2021-08:10 jkalvesmaki: converted the next line into a variable declaration
    Processor proc = new Processor(config);
    config.registerExtensionFunction(new SchemaFunction());
    config.registerExtensionFunction(new SchemaReportFunction(proc));
  }
}
