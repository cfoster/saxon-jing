package net.cfoster.saxonjing;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * RelaxNG Validation, large portion of the code has been lifted from the
 * BaseX Project, specifically the ValidateRng class.
 *
 * Thank you to the original author Christian Gruen.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 * @author Charles Foster
 */

public class ValidateRng
{
  InputSource schemaInputSource;
  boolean compact = true;
  ErrorHandlerImpl handler = new ErrorHandlerImpl();

  private Method validateMethod;
  private Object vdInstance;

  ClassLoader classLoader() { // can be replaced with a separate class loader.
    return Thread.currentThread().getContextClassLoader();
  }

  public ValidateRng(InputSource schemaInputSource) throws ValidateRngException
  {
    this.schemaInputSource = schemaInputSource;
    loadSchema();
  }

  public ErrorHandlerImpl getErrorHandler() {
    return handler;
  }

  private final void loadSchema() throws ValidateRngException
  {
    ClassLoader fcl = classLoader();

    try
    {
      final Class<?>
        pmb = Class.forName("com.thaiopensource.util.PropertyMapBuilder", false, fcl),
        vd = Class.forName("com.thaiopensource.validate.ValidationDriver", false, fcl),
        vp = Class.forName("com.thaiopensource.validate.ValidateProperty", false, fcl),
        pi = Class.forName("com.thaiopensource.util.PropertyId", false, fcl),
        pm = Class.forName("com.thaiopensource.util.PropertyMap", false, fcl),
        sr = Class.forName("com.thaiopensource.validate.SchemaReader", false, fcl),
        csr = Class.forName("com.thaiopensource.validate.rng.CompactSchemaReader", false, fcl);

      final Object ehInstance = vp.getField("ERROR_HANDLER").get(null);
      final Object pmbInstance = pmb.newInstance();
      pi.getMethod("put", pmb, Object.class).invoke(ehInstance, pmbInstance, handler);

      final Object srInstance = compact ? csr.getMethod("getInstance").invoke(null) : null;
      final Object pmInstance = pmb.getMethod("toPropertyMap").invoke(pmbInstance);
      vdInstance = vd.getConstructor(pm, sr).newInstance(pmInstance, srInstance);

      final Method vdLs = vd.getMethod("loadSchema", InputSource.class);
      final Object loaded = vdLs.invoke(vdInstance, schemaInputSource);
      if (Boolean.TRUE.equals(loaded)) {
        validateMethod = vd.getMethod("validate", InputSource.class);
      }

      if(handler.error != null)
      {
        Exception e = handler.error.get(0);
        if(e instanceof SAXParseException) {
          SAXParseException se = (SAXParseException)e;
          throw new ValidateRngException(
          MessageFormat.format(
            "Found Invalid RelaxNG Syntax. In {0} at line {1}, column {2}.",
            se.getSystemId() != null ? se.getSystemId() : se.getPublicId(),
            se.getLineNumber(),
            se.getColumnNumber()
          ),
          Constants.ERR_RNG_SYNTAX);
        }
      }

    }
    catch (ValidateRngException e)
    {
      throw e; // rethrow
    }
    catch (final ClassNotFoundException ex)
    {
      throw new ValidateRngException(
        "Can not find Jing library on classpath.", Constants.ERR_NO_JING, ex);
    }
    catch(InvocationTargetException ex)
    {
      if(ex.getTargetException() instanceof FileNotFoundException)
      {
        throw new ValidateRngException(
          MessageFormat.format(
          "Unable to find Schema file ''{0}''", schemaInputSource.getSystemId()),
          Constants.ERR_RNG_NOT_FOUND, ex);
      }

      throw new ValidateRngException(
        ex.getMessage(),
        Constants.ERR_RNG_LOAD,
        ex);
    }
    catch (final Exception e)
    {
      throw new ValidateRngException(e.getMessage(), Constants.ERR_UNKNOWN, e);
    }
  }

  public void validate(InputSource in) throws ValidateRngException
  {
    getErrorHandler().reset();

    try {
      validateMethod.invoke(vdInstance, in);
    }
    catch (Exception e) {
      throw new ValidateRngException(e.getMessage(), Constants.ERR_INVALID, e);
    }
  }
}
