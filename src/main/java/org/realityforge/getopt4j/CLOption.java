package org.realityforge.getopt4j;

import java.util.Arrays;

/**
 * Basic class describing an instance of option.
 */
public final class CLOption
{
  /**
   * Value of {@link #getId} when the option is a text argument.
   */
  public static final int TEXT_ARGUMENT = 0;

  private String[] _arguments;
  private CLOptionDescriptor _descriptor;

  /**
   * Retrieve argument to option if it takes arguments.
   *
   * @return the (first) argument
   */
  public String getArgument()
  {
    return getArgument( 0 );
  }

  /**
   * Retrieve indexed argument to option if it takes arguments.
   *
   * @param index The argument index, from 0 to
   *              {@link #getArgumentCount()}-1.
   * @return the argument
   */
  public String getArgument( final int index )
  {
    if ( null == _arguments || index < 0 || index >= _arguments.length )
    {
      return null;
    }
    else
    {
      return _arguments[ index ];
    }
  }

  /**
   * Retrieve id of option.
   *
   * The id is eqivalent to character code if it can be a single letter option.
   *
   * @return the id
   */
  public int getId()
  {
    return _descriptor == null ? TEXT_ARGUMENT : _descriptor.getId();
  }

  public CLOptionDescriptor getDescriptor()
  {
    return _descriptor;
  }

  /**
   * Constructor taking an descriptor
   *
   * @param descriptor the descriptor
   */
  public CLOption( final CLOptionDescriptor descriptor )
  {
    _descriptor = descriptor;
  }

  /**
   * Constructor taking argument for option.
   *
   * @param argument the argument
   */
  public CLOption( final String argument )
  {
    this( (CLOptionDescriptor) null );
    addArgument( argument );
  }

  /**
   * Mutator of Argument property.
   *
   * @param argument the argument
   */
  public void addArgument( final String argument )
  {
    if ( null == _arguments )
    {
      _arguments = new String[]{ argument };
    }
    else
    {
      final String[] arguments = new String[ _arguments.length + 1 ];
      System.arraycopy( _arguments, 0, arguments, 0, _arguments.length );
      arguments[ _arguments.length ] = argument;
      _arguments = arguments;
    }
  }

  /**
   * Get number of arguments.
   *
   * @return the number of arguments
   */
  public int getArgumentCount()
  {
    if ( null == _arguments )
    {
      return 0;
    }
    else
    {
      return _arguments.length;
    }
  }

  /**
   * Convert to String.
   *
   * @return the string value
   */
  public String toString()
  {
    return "[Option " +
           (char) _descriptor.getId() +
           ( null != _arguments ? ", " + Arrays.asList( _arguments ) : "" ) +
           " ]";
  }
}
