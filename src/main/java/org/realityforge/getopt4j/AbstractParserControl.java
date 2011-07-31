package org.realityforge.getopt4j;

/**
 * Class to inherit from so when in future when new controls are added
 * clients will no have to implement them.
 *
 * @see ParserControl
 */
public abstract class AbstractParserControl
    implements ParserControl
{
  /**
   * By default always continue parsing by returning false.
   *
   * @param lastOptionCode the code of last option parsed
   * @return return true to halt, false to continue parsing
   * @see ParserControl#isFinished(int)
   */
  public boolean isFinished(int lastOptionCode)
  {
    return false;
  }
}
