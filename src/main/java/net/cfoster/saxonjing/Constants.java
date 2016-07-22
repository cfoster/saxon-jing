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
