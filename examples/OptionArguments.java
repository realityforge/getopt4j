import org.realityforge.getopt4j.CLArgsParser;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.getopt4j.CLUtil;

/**
 * This simple example shows how to have options, requiring
 * an argument, optionally supporting an argument or requiring
 * 2 arguments.
 */
public class OptionArguments
{
  // Define our short one-letter option identifiers.
  private static final int FILE_OPT = 'f';
  private static final int DEFINE_OPT = 'D';
  private static final int SECURE_OPT = 'S';
  private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
      //File requires an argument
      new CLOptionDescriptor( "file",
                              CLOptionDescriptor.ARGUMENT_REQUIRED,
                              FILE_OPT,
                              "specify a file" ),

      //secure can take an argument if supplied
      new CLOptionDescriptor( "secure",
                              CLOptionDescriptor.ARGUMENT_OPTIONAL,
                              SECURE_OPT,
                              "set security mode" ),

      //define requires 2 arguments
      new CLOptionDescriptor( "define",
                              CLOptionDescriptor.ARGUMENTS_REQUIRED_2,
                              DEFINE_OPT,
                              "Require 2 arguments" )
    };

  public static void main( final String[] args )
  {
    System.out.println( "Starting OptionArguments..." );
    System.out.println( CLUtil.describeOptions( OPTIONS ) );
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

        case FILE_OPT:
          System.out.println( "File: " + option.getArgument() );
          break;

        case SECURE_OPT:
          if ( null == option.getArgument() )
          {
            System.out.println( "Secure Mode with no args" );
          }
          else
          {
            System.out.println( "Secure Mode with arg: " + option.getArgument() );
          }
          break;

        case DEFINE_OPT:
          System.out.println( "Defining: " +
                              option.getArgument( 0 ) + "=" +
                              option.getArgument( 1 ) );
          break;
      }
    }
  }
}
