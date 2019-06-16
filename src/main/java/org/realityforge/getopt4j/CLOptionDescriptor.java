package org.realityforge.getopt4j;

/**
 * Basic class describing an type of option.
 * Typically, one creates a static array of <code>CLOptionDescriptor</code>s,
 * and passes it to {@link CLArgsParser#CLArgsParser(String[], CLOptionDescriptor[])}.
 *
 * @see CLArgsParser
 * @see CLUtil
 */
public final class CLOptionDescriptor
{
  /**
   * Flag to say that one argument is required
   */
  public static final int ARGUMENT_REQUIRED = 1 << 1;
  /**
   * Flag to say that the argument is optional
   */
  public static final int ARGUMENT_OPTIONAL = 1 << 2;
  /**
   * Flag to say this option does not take arguments
   */
  public static final int ARGUMENT_DISALLOWED = 1 << 3;
  /**
   * Flag to say this option requires 2 arguments
   */
  public static final int ARGUMENTS_REQUIRED_2 = 1 << 4;
  /**
   * Flag to say this option may be repeated on the command line
   */
  public static final int DUPLICATES_ALLOWED = 1 << 5;

  private final int _id;
  private final int _flags;
  private final String _name;
  private final String _description;
  private final int[] _incompatible;

  /**
   * Constructor.
   *
   * @param name        the name/long option
   * @param flags       the flags
   * @param id          the id/character option
   * @param description description of option usage
   */
  public CLOptionDescriptor( final String name,
                             final int flags,
                             final int id,
                             final String description )
  {
    this( name, flags, id, description,
          ( flags & CLOptionDescriptor.DUPLICATES_ALLOWED ) > 0
          ? new int[ 0 ] : new int[]{ id } );
  }

  /**
   * Constructor.
   *
   * @param name         the name/long option
   * @param flags        the flags
   * @param id           the id/character option
   * @param description  description of option usage
   * @param incompatible an array listing the ids of all incompatible options
   */
  public CLOptionDescriptor( final String name,
                             final int flags,
                             final int id,
                             final String description,
                             final int[] incompatible )
  {
    _id = id;
    _name = name;
    _flags = flags;
    _description = description;
    _incompatible = incompatible;

    int modeCount = 0;
    if ( ( ARGUMENT_REQUIRED & flags ) == ARGUMENT_REQUIRED )
    {
      modeCount++;
    }
    if ( ( ARGUMENT_OPTIONAL & flags ) == ARGUMENT_OPTIONAL )
    {
      modeCount++;
    }
    if ( ( ARGUMENT_DISALLOWED & flags ) == ARGUMENT_DISALLOWED )
    {
      modeCount++;
    }
    if ( ( ARGUMENTS_REQUIRED_2 & flags ) == ARGUMENTS_REQUIRED_2 )
    {
      modeCount++;
    }

    if ( 0 == modeCount )
    {
      final String message = "No mode specified for option " + this;
      throw new IllegalStateException( message );
    }
    else if ( 1 != modeCount )
    {
      final String message = "Multiple modes specified for option " + this;
      throw new IllegalStateException( message );
    }
  }

  /**
   * Constructor.
   *
   * @param name         the name/long option
   * @param flags        the flags
   * @param id           the id/character option
   * @param description  description of option usage
   * @param incompatible the incompatible options.
   */
  public CLOptionDescriptor( final String name,
                             final int flags,
                             final int id,
                             final String description,
                             final CLOptionDescriptor[] incompatible )
  {
    _id = id;
    _name = name;
    _flags = flags;
    _description = description;

    _incompatible = new int[ incompatible.length ];
    for ( int i = 0; i < incompatible.length; i++ )
    {
      _incompatible[ i ] = incompatible[ i ].getId();
    }
  }

  /**
   * @return the array of incompatible option ids
   * @deprecated Use the correctly spelled {@link #getIncompatible} instead.
   */
  protected int[] getIncompatble()
  {
    return getIncompatible();
  }

  /**
   * Get the array of incompatible option ids.
   *
   * @return the array of incompatible option ids
   */
  int[] getIncompatible()
  {
    return _incompatible;
  }

  /**
   * Retrieve textual description.
   *
   * @return the description
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Retrieve flags about option.
   * Flags include details such as whether it allows parameters etc.
   *
   * @return the flags
   */
  public int getFlags()
  {
    return _flags;
  }

  /**
   * Retrieve the id for option.
   * The id is also the character if using single character options.
   *
   * @return the id
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Retrieve name of option which is also text for long option.
   *
   * @return name/long option
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Convert to String.
   *
   * @return the converted value to string.
   */
  public String toString()
  {
    return "[OptionDescriptor " + _name + ", " + _id + ", " + _flags + ", " + _description + " ]";
  }
}
