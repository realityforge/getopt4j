package org.realityforge.getopt4j;

/**
 * ParserControl is used to control particular behaviour of the parser.
 *
 * @see AbstractParserControl
 */
public interface ParserControl
{
  /**
   * Called by the parser to determine whether it should stop
   * after last option parsed.
   *
   * @param lastOptionCode the code of last option parsed
   * @return return true to halt, false to continue parsing
   */
  boolean isFinished( int lastOptionCode );
}
