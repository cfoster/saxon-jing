package net.cfoster.saxonjing;

public class Constants
{
  private Constants() {}

  /** Jing can not be found on Classpath **/
  public static String ERR_NO_JING = "RNGE0001";

  /** Syntax Error in Relax NG Schema **/
  public static String ERR_RNG_SYNTAX = "RNGE0002";

  /** Could not find Relax NG Schema **/
  public static String ERR_RNG_NOT_FOUND = "RNGE0003";

  /** Generic Exception whilst trying to load RelaxNG Schema **/
  public static String ERR_RNG_LOAD = "RNGE0004";

  /** Schema Validation Failed **/
  public static String ERR_INVALID = "RNGE0005";

  /** Unknown Exception **/
  public static String ERR_UNKNOWN = "RNGE0006";
}
