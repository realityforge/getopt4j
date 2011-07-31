package org.realityforge.getopt4j;

/**
 * Token handles tokenizing the CLI arguments
 */
class Token
{
  /**
   * Type for a separator token
   */
  public static final int TOKEN_SEPARATOR = 0;
  /**
   * Type for a text token
   */
  public static final int TOKEN_STRING = 1;

  private final int m_type;
  private final String m_value;

  /**
   * New Token object with a type and value
   */
  Token(final int type, final String value)
  {
    m_type = type;
    m_value = value;
  }

  /**
   * Get the value of the token
   */
  final String getValue()
  {
    return m_value;
  }

  /**
   * Get the type of the token
   */
  final int getType()
  {
    return m_type;
  }

  /**
   * Convert to a string
   */
  public final String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append(m_type);
    sb.append(":");
    sb.append(m_value);
    return sb.toString();
  }
}
