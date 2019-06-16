import org.realityforge.getopt4j.CLArgsParser;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.getopt4j.CLUtil;

/**
 * Demonstrates Basic example of command line parsing. It also demonstrates
 * generating a usage message from the list of options.
 */
public class BasicCLI
{
  // Define our short one-letter option identifiers.
  private static final int HELP_OPT = 'h';
  private static final int VERSION_OPT = 'v';
  /**
   * Define the understood options. Each CLOptionDescriptor contains:
   * - The "long" version of the option. Eg, "help" means that "--help" will
   * be recognised.
   * - The option flags, governing the option's argument(s).
   * - The "short" version of the option. Eg, 'h' means that "-h" will be
   * recognised.
   * - A description of the option.
   */
  private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
      new CLOptionDescriptor( "help",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              HELP_OPT,
                              "print this message and exit" ),
      new CLOptionDescriptor( "version",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              VERSION_OPT,
                              "print the version information and exit" )
    };

  public static void main( final String[] args )
  {
    System.out.println( "Starting BasicCLI..." );
    System.out.println();

    // Parse the arguments
    final CLArgsParser parser = new CLArgsParser( args, OPTIONS );

    //Make sure that there was no errors parsing arguments
    if ( null != parser.getErrorString() )
    {
      System.err.println( "Error: " + parser.getErrorString() );
      return;
    }

    for ( final CLOption option : parser.getArguments() )
    {

      switch ( option.getId() )
      {
        case CLOption.TEXT_ARGUMENT:
          //This occurs when a user supplies an argument that
          //is not an option
          System.out.println( "Unknown arg: " + option.getArgument() );
          break;

        case HELP_OPT:
          printUsage();
          break;

        case VERSION_OPT:
          printVersion();
          break;
      }
    }
  }

  /**
   * Print out a dummy version
   */
  private static void printVersion()
  {
    System.out.println( "1.0" );
    System.exit( 0 );
  }

  /**
   * Print out a usage statement
   */
  private static void printUsage()
  {
    final String lineSeparator = System.getProperty( "line.separator" );

    final StringBuilder msg = new StringBuilder();

    msg.append( lineSeparator );
    msg.append( "Command-line arg parser demo" );
    msg.append( lineSeparator );
    msg.append( "Usage: java " ).append( IncompatOptions.class.getName() ).append( " [options]" );
    msg.append( lineSeparator );
    msg.append( lineSeparator );
    msg.append( "Options: " );
    msg.append( lineSeparator );

    /**
     * Notice that the next line uses CLUtil.describeOptions to generate the
     * list of descriptions for each option
     */
    msg.append( CLUtil.describeOptions( BasicCLI.OPTIONS ).toString() );

    System.out.println( msg.toString() );

    System.exit( 0 );
  }
}
