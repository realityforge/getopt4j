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

  private final int _type;
  private final String _value;

  /**
   * New Token object with a type and value
   */
  Token( final int type, final String value )
  {
    _type = type;
    _value = value;
  }

  /**
   * Get the value of the token
   */
  String getValue()
  {
    return _value;
  }

  /**
   * Get the type of the token
   */
  int getType()
  {
    return _type;
  }

  /**
   * Convert to a string
   */
  public String toString()
  {
    return _type + ":" + _value;
  }
}
