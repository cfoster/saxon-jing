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
