package net.cfoster.saxonjing;

import org.xml.sax.InputSource;
import java.lang.reflect.Method;

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

/**
 * Try to produce error codes in future:
 *
 * RNGE0001 The document cannot be validated against the specified DTD or XML Schema.
 * RNGE0002 The validation cannot be started.
 * RNGE0003 No validator is available.
 * RNGE0004 No validator is found for the specified version.
 */
public class ValidateRng
{
  InputSource schemaInputSource;
  boolean compact = true;
  ErrorHandlerImpl handler = new ErrorHandlerImpl();

  private Method validateMethod;
  private Object vdInstance;

  ClassLoader classLoader() { // can be replaced with a separate class loader.
    return ClassLoader.getSystemClassLoader();
  }

  public ValidateRng(InputSource schemaInputSource) {
    this.schemaInputSource = schemaInputSource;
    loadSchema();
  }

  public ErrorHandlerImpl getErrorHandler() {
    return handler;
  }

  private final void loadSchema()
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
    }
    catch (final ClassNotFoundException ex) {
      throw new RuntimeException(ex);
      // throw BXVA_RELAXNG_X.get(info);
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
      // throw BXVA_FAIL_X.get(info, Util.rootException(ex));
    }
  }

  public void validate(InputSource in)
  {
    getErrorHandler().reset();

    try {
      validateMethod.invoke(vdInstance, in);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
