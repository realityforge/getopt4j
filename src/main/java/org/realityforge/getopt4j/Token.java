package org.realityforge.getopt4j;

/**
 * Token handles tokenizing the CLI arguments
 */
final class Token
{
  /**
   * Type for a separator token
   */
  static final int TOKEN_SEPARATOR = 0;
  /**
   * Type for a text token
   */
  static final int TOKEN_STRING = 1;

  private final int m_type;
  private final String m_value;

  /**
   * New Token object with a type and value
   */
  Token( final int type, final String value )
  {
    m_type = type;
    m_value = value;
  }

  /**
   * Get the value of the token
   */
  String getValue()
  {
    return m_value;
  }

  /**
   * Get the type of the token
   */
  int getType()
  {
    return m_type;
  }

  /**
   * Convert to a string
   */
  public String toString()
  {
    return m_type + ":" + m_value;
  }
}
