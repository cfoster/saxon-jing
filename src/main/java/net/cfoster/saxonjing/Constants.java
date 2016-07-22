package net.cfoster.saxonjing;

public class Constants
{
  private Constants() {}

  /** Jing can not be found on Classpath **/
  public static String ERR_NO_JING = "SXJG0001";

  /** Ssyntax Error in Relax NG Schema **/
  public static String ERR_RNG_SYNTAX = "SXJG0002";

  /** Could not find Relax NG Schema **/
  public static String ERR_RNG_NOT_FOUND = "SXJG0004";

  /** Generic Exception whilst trying to load RelaxNG Schema **/
  public static String ERR_RNG_LOAD = "SXJG0005";

  /** Schema Validation Failed **/
  public static String ERR_INVALID = "SXJG0006";
}
