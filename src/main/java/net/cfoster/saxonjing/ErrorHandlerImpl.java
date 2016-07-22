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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlerImpl implements ErrorHandler
{
  List<SAXParseException> warning;
  List<SAXParseException> error;
  List<SAXParseException> fatal;

  public void warning(SAXParseException e) throws SAXException {
    if(warning == null) warning = new ArrayList<SAXParseException>();
    warning.add(e);
  }

  public void error(SAXParseException e) throws SAXException {
    if(error == null) error = new ArrayList<SAXParseException>();
    error.add(e);
  }

  public void fatalError(SAXParseException e) throws SAXException {
    if(fatal == null) fatal = new ArrayList<SAXParseException>();
    fatal.add(e);
  }

  public void reset() {
    warning = null;
    error = null;
    fatal = null;
  }

}
