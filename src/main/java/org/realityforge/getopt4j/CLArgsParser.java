package org.realityforge.getopt4j;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parser for command line arguments.
 *
 * This parses command lines according to the standard (?) of
 * GNU utilities. Note that CLArgs uses a backing hash table for the options index and
 * so duplicate arguments are only returned by getArguments().
 *
 * @see ParserControl
 * @see CLOption
 * @see CLOptionDescriptor
 */
public final class CLArgsParser
{
  //cached character == Integer.MAX_VALUE when invalid
  private static final int INVALID = Integer.MAX_VALUE;

  private static final int STATE_NORMAL = 0;
  private static final int STATE_REQUIRE_2ARGS = 1;
  private static final int STATE_REQUIRE_ARG = 2;
  private static final int STATE_OPTIONAL_ARG = 3;
  private static final int STATE_NO_OPTIONS = 4;
  private static final int STATE_OPTION_MODE = 5;

  private static final char[] ARG_SEPARATORS = new char[]{ (char) 0, '=' };
  private static final char[] NULL_SEPARATORS = new char[]{ (char) 0 };

  private CLOptionDescriptor[] _optionDescriptors;
  private List<CLOption> _options;
  private HashMap<Integer, CLOption> _id2Option;
  private HashMap<String, CLOption> _name2Option;
  private ParserControl _control;

  private String _errorMessage;
  private String[] _unParsedArgs = new String[]{};

  //variables used while parsing options.
  private char _ch;
  private String[] _args;
  private boolean _isLong;
  private int _argIndex;
  private int _stringIndex;
  private int _stringLength;

  private int _lastChar = INVALID;

  private int _lastOptionId;
  private CLOption _option;
  private int _state = STATE_NORMAL;
  private int _argIndexForMultiArg;

  /**
   * Create a parser that can deal with options and parses certain args.
   *
   * @param args              the args, typically that passed to the
   *                          <code>public static void main(String[] args)</code> method.
   * @param optionDescriptors the option descriptors
   * @param control           the parser control used determine behaviour of parser
   */
  public CLArgsParser( final String[] args,
                       final CLOptionDescriptor[] optionDescriptors,
                       final ParserControl control )
  {
    _optionDescriptors = optionDescriptors;
    _control = control;
    _options = new ArrayList<CLOption>();
    _args = args;

    try
    {
      parse();
      checkIncompatibilities( _options );
      buildOptionIndex();
    }
    catch ( final ParseException pe )
    {
      _errorMessage = pe.getMessage();
    }
  }

  /**
   * Create a parser that deals with options and parses certain args.
   *
   * @param args              the args
   * @param optionDescriptors the option descriptors
   */
  public CLArgsParser( final String[] args,
                       final CLOptionDescriptor[] optionDescriptors )
  {
    this( args, optionDescriptors, null );
  }

  /**
   * Retrieve an array of arguments that have not been parsed
   * due to the parser halting.
   *
   * @return an array of unparsed args
   */
  public String[] getUnParsedArgs()
  {
    return _unParsedArgs;
  }

  /**
   * Retrieve a list of options that were parsed from command list.
   *
   * @return the list of options
   */
  public List<CLOption> getArguments()
  {
    return _options;
  }

  /**
   * Retrieve the {@link CLOption} with specified id, or
   * <code>null</code> if no command line option is found.
   *
   * @param id the command line option id
   * @return the {@link CLOption} with the specified id, or <code>null</code> if no CLOption is found.
   * @see CLOption
   */
  public CLOption getArgumentById( final int id )
  {
    return _id2Option.get( id );
  }

  /**
   * Retrieve the {@link CLOption} with specified name, or
   * <code>null</code> if no command line option is found.
   *
   * @param name the command line option name
   * @return the {@link CLOption} with the specified name, or
   * <code>null</code> if no CLOption is found.
   * @see CLOption
   */
  public CLOption getArgumentByName( final String name )
  {
    return _name2Option.get( name );
  }

  /**
   * Retrieve an error message that occured during parsing if one existed.
   *
   * @return the error string
   */
  public String getErrorString()
  {
    return _errorMessage;
  }

  /**
   * Get Descriptor for option id.
   *
   * @param id the id
   * @return the descriptor
   */
  private CLOptionDescriptor getDescriptorFor( final int id )
  {
    for ( final CLOptionDescriptor descriptor : _optionDescriptors )
    {
      if ( descriptor.getId() == id )
      {
        return descriptor;
      }
    }

    return null;
  }

  /**
   * Retrieve a descriptor by name.
   *
   * @param name the name
   * @return the descriptor
   */
  private CLOptionDescriptor getDescriptorFor( final String name )
  {
    for ( final CLOptionDescriptor optionDescriptor : _optionDescriptors )
    {
      if ( optionDescriptor.getName().equals( name ) )
      {
        return optionDescriptor;
      }
    }

    return null;
  }

  /**
   * Check for duplicates of an option.
   * It is an error to have duplicates unless appropriate flags is set in descriptor.
   *
   * @param arguments the arguments
   */
  private void checkIncompatibilities( final List<CLOption> arguments )
    throws ParseException
  {
    final int size = arguments.size();

    for ( int i = 0; i < size; i++ )
    {
      final CLOption option = arguments.get( i );
      final int id = option.getId();
      final CLOptionDescriptor descriptor = getDescriptorFor( id );

      //this occurs when id == 0 and user has not supplied a descriptor
      //for arguments
      if ( null == descriptor )
      {
        continue;
      }

      final int[] incompatible = descriptor.getIncompatible();

      checkIncompatible( arguments, incompatible, i );
    }
  }

  private void checkIncompatible( final List<CLOption> arguments,
                                  final int[] incompatible,
                                  final int original )
    throws ParseException
  {
    final int size = arguments.size();

    for ( int i = 0; i < size; i++ )
    {
      if ( original == i )
      {
        continue;
      }

      final CLOption option = arguments.get( i );
      final int id = option.getId();

      for ( final int anIncompatible : incompatible )
      {
        if ( id == anIncompatible )
        {
          final CLOption originalOption = arguments.get( original );
          final int originalId = originalOption.getId();

          final String message;
          if ( id == originalId )
          {
            message =
              "Duplicate options for " + describeDualOption( originalId ) + " found.";
          }
          else
          {
            message = "Incompatible options -" + describeDualOption( id ) + " and " +
                      describeDualOption( originalId ) + " found.";
          }
          throw new ParseException( message, 0 );
        }
      }
    }
  }

  /**
   * Require state to be placed in for option.
   *
   * @param descriptor the Option Descriptor
   * @return the state
   */
  private int getStateFor( final CLOptionDescriptor descriptor )
  {
    final int flags = descriptor.getFlags();
    if ( ( flags & CLOptionDescriptor.ARGUMENTS_REQUIRED_2 ) == CLOptionDescriptor.ARGUMENTS_REQUIRED_2 )
    {
      return STATE_REQUIRE_2ARGS;
    }
    else if ( ( flags & CLOptionDescriptor.ARGUMENT_REQUIRED ) == CLOptionDescriptor.ARGUMENT_REQUIRED )
    {
      return STATE_REQUIRE_ARG;
    }
    else if ( ( flags & CLOptionDescriptor.ARGUMENT_OPTIONAL ) == CLOptionDescriptor.ARGUMENT_OPTIONAL )
    {
      return STATE_OPTIONAL_ARG;
    }
    else
    {
      return STATE_NORMAL;
    }
  }

  private String describeDualOption( final int id )
  {
    final CLOptionDescriptor descriptor = getDescriptorFor( id );
    if ( null == descriptor )
    {
      return "<parameter>";
    }
    else
    {
      final StringBuilder sb = new StringBuilder();
      boolean hasCharOption = false;

      if ( Character.isLetter( (char) id ) )
      {
        sb.append( '-' );
        sb.append( (char) id );
        hasCharOption = true;
      }

      final String longOption = descriptor.getName();
      if ( null != longOption )
      {
        if ( hasCharOption )
        {
          sb.append( '/' );
        }
        sb.append( "--" );
        sb.append( longOption );
      }

      return sb.toString();
    }
  }

  /**
   * Create a string array that is subset of input array.
   * The sub-array should start at array entry indicated by index. That array element
   * should only include characters from charIndex onwards.
   *
   * @param array     the original array
   * @param index     the cut-point in array
   * @param charIndex the cut-point in element of array
   * @return the result array
   */
  private String[] subArray( final String[] array, final int index, final int charIndex )
  {
    final int remaining = array.length - index;
    final String[] result = new String[ remaining ];

    if ( remaining > 1 )
    {
      System.arraycopy( array, index + 1, result, 1, remaining - 1 );
    }

    result[ 0 ] = array[ index ].substring( charIndex - 1 );

    return result;
  }

  /**
   * Actually parse arguments
   */
  private void parse()
    throws ParseException
  {
    if ( 0 == _args.length )
    {
      return;
    }

    _stringLength = _args[ _argIndex ].length();

    while ( true )
    {
      _ch = peekAtChar();

      if ( _argIndex >= _args.length )
      {
        break;
      }

      if ( null != _control && _control.isFinished( _lastOptionId ) )
      {
        //this may need mangling due to peeks
        _unParsedArgs = subArray( _args, _argIndex, _stringIndex );
        return;
      }

      if ( STATE_OPTION_MODE == _state )
      {
        //if get to an arg barrier then return to normal mode
        //else continue accumulating options
        if ( 0 == _ch )
        {
          getChar(); //strip the null
          _state = STATE_NORMAL;
        }
        else
        {
          parseShortOption();
        }
      }
      else if ( STATE_NORMAL == _state )
      {
        parseNormal();
      }
      else if ( STATE_NO_OPTIONS == _state )
      {
        //should never get to here when stringIndex != 0
        addOption( new CLOption( _args[ _argIndex++ ] ) );
      }
      else if ( STATE_OPTIONAL_ARG == _state && '-' == _ch )
      {
        _state = STATE_NORMAL;
        addOption( _option );
      }
      else
      {
        parseArguments();
      }
    }

    if ( _option != null )
    {
      if ( STATE_OPTIONAL_ARG == _state )
      {
        _options.add( _option );
      }
      else if ( STATE_REQUIRE_ARG == _state )
      {
        final CLOptionDescriptor descriptor = getDescriptorFor( _option.getId() );
        final String message =
          "Missing argument to option " + getOptionDescription( descriptor );
        throw new ParseException( message, 0 );
      }
      else if ( STATE_REQUIRE_2ARGS == _state )
      {
        if ( 1 == _option.getArgumentCount() )
        {
          _option.addArgument( "" );
          _options.add( _option );
        }
        else
        {
          final CLOptionDescriptor descriptor = getDescriptorFor( _option.getId() );
          final String message =
            "Missing argument to option " + getOptionDescription( descriptor );
          throw new ParseException( message, 0 );
        }
      }
      else
      {
        throw new ParseException( "IllegalState " + _state + ": " + _option, 0 );
      }
    }
  }

  private String getOptionDescription( final CLOptionDescriptor descriptor )
  {
    if ( _isLong )
    {
      return "--" + descriptor.getName();
    }
    else
    {
      return "-" + (char) descriptor.getId();
    }
  }

  private char peekAtChar()
  {
    if ( INVALID == _lastChar )
    {
      _lastChar = readChar();
    }
    return (char) _lastChar;
  }

  private char getChar()
  {
    if ( INVALID != _lastChar )
    {
      final char result = (char) _lastChar;
      _lastChar = INVALID;
      return result;
    }
    else
    {
      return readChar();
    }
  }

  private char readChar()
  {
    if ( _stringIndex >= _stringLength )
    {
      _argIndex++;
      _stringIndex = 0;

      if ( _argIndex < _args.length )
      {
        _stringLength = _args[ _argIndex ].length();
      }
      else
      {
        _stringLength = 0;
      }

      return 0;
    }

    if ( _argIndex >= _args.length )
    {
      return 0;
    }

    return _args[ _argIndex ].charAt( _stringIndex++ );
  }

  private Token nextToken( final char[] separators )
  {
    _ch = getChar();

    if ( isSeparator( _ch, separators ) )
    {
      _ch = getChar();
      return new Token( Token.TOKEN_SEPARATOR, null );
    }

    final StringBuilder sb = new StringBuilder();

    do
    {
      sb.append( _ch );
      _ch = getChar();
    }
    while ( !isSeparator( _ch, separators ) );

    return new Token( Token.TOKEN_STRING, sb.toString() );
  }

  private boolean isSeparator( final char ch, final char[] separators )
  {
    for ( final char separator : separators )
    {
      if ( ch == separator )
      {
        return true;
      }
    }

    return false;
  }

  private void addOption( final CLOption option )
  {
    _options.add( option );
    _lastOptionId = option.getId();
    _option = null;
  }

  private void parseOption( final CLOptionDescriptor descriptor,
                            final String optionString )
    throws ParseException
  {
    if ( null == descriptor )
    {
      throw new ParseException( "Unknown option " + optionString, 0 );
    }

    _state = getStateFor( descriptor );
    _option = new CLOption( descriptor );

    if ( STATE_NORMAL == _state )
    {
      addOption( _option );
    }
  }

  private void parseShortOption()
    throws ParseException
  {
    _ch = getChar();
    final CLOptionDescriptor descriptor = getDescriptorFor( _ch );
    _isLong = false;
    parseOption( descriptor, "-" + _ch );

    if ( STATE_NORMAL == _state )
    {
      _state = STATE_OPTION_MODE;
    }
  }

  private void parseArguments()
    throws ParseException
  {
    if ( STATE_REQUIRE_ARG == _state )
    {
      if ( '=' == _ch || 0 == _ch )
      {
        getChar();
      }

      final Token token = nextToken( NULL_SEPARATORS );
      _option.addArgument( token.getValue() );

      addOption( _option );
      _state = STATE_NORMAL;
    }
    else if ( STATE_OPTIONAL_ARG == _state )
    {
      if ( '-' == _ch || 0 == _ch )
      {
        getChar(); //consume stray character
        addOption( _option );
        _state = STATE_NORMAL;
        return;
      }

      if ( '=' == _ch )
      {
        getChar();
      }

      final Token token = nextToken( NULL_SEPARATORS );
      _option.addArgument( token.getValue() );

      addOption( _option );
      _state = STATE_NORMAL;
    }
    else if ( STATE_REQUIRE_2ARGS == _state )
    {
      if ( 0 == _option.getArgumentCount() )
      {
        _argIndexForMultiArg = _argIndex;
        final Token token = nextToken( ARG_SEPARATORS );

        if ( Token.TOKEN_SEPARATOR == token.getType() )
        {
          final CLOptionDescriptor descriptor = getDescriptorFor( _option.getId() );
          final String message =
            "Unable to parse first argument for option " +
            getOptionDescription( descriptor );
          throw new ParseException( message, 0 );
        }
        else
        {
          _option.addArgument( token.getValue() );
        }
      }
      else //2nd argument
      {
        final StringBuilder sb = new StringBuilder();

        _ch = getChar();
        if ( _argIndex != _argIndexForMultiArg && _ch != 0 )
        {
          _lastChar = _ch;
        }

        while ( _argIndex == _argIndexForMultiArg )
        {
          sb.append( _ch );
          _ch = getChar();
        }

        final String argument = sb.toString();

        //System.out.println( "Argument:" + argument );

        _option.addArgument( argument );
        addOption( _option );
        _option = null;
        _state = STATE_NORMAL;
      }
    }
  }

  /**
   * Parse Options from Normal mode.
   */
  private void parseNormal()
    throws ParseException
  {
    if ( '-' != _ch )
    {
      //Parse the arguments that are not options
      final String argument = nextToken( NULL_SEPARATORS ).getValue();
      addOption( new CLOption( argument ) );
      _state = STATE_NORMAL;
    }
    else
    {
      getChar(); // strip the -

      if ( 0 == peekAtChar() )
      {
        throw new ParseException( "Malformed option -", 0 );
      }
      else
      {
        _ch = peekAtChar();

        //if it is a short option then parse it else ...
        if ( '-' != _ch )
        {
          parseShortOption();
        }
        else
        {
          getChar(); // strip the -
          //-- sequence .. it can either mean a change of state
          //to STATE_NO_OPTIONS or else a long option

          if ( 0 == peekAtChar() )
          {
            getChar();
            _state = STATE_NO_OPTIONS;
          }
          else
          {
            //its a long option
            final String optionName = nextToken( ARG_SEPARATORS ).getValue();
            final CLOptionDescriptor descriptor = getDescriptorFor( optionName );
            _isLong = true;
            parseOption( descriptor, "--" + optionName );
          }
        }
      }
    }
  }

  /**
   * Build the _optionIndex lookup map for the parsed options.
   */
  private void buildOptionIndex()
  {
    final int size = _options.size();
    _id2Option = new HashMap<Integer, CLOption>( size * 2 );
    _name2Option = new HashMap<String, CLOption>( size * 2 );

    for ( final CLOption option : _options )
    {
      final CLOptionDescriptor descriptor = getDescriptorFor( option.getId() );
      _id2Option.put( option.getId(), option );
      if ( null != descriptor && null != descriptor.getName() )
      {
        _name2Option.put( descriptor.getName(), option );
      }
    }
  }
}
