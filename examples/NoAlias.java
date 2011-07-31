import org.realityforge.getopt4j.CLArgsParser;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.getopt4j.CLUtil;

/**
 * This simple example shows how to set it up so that only the
 * long form or only short form of an option is capable of
 * being used.
 */
public class NoAlias
{
  // Define our short one-letter option identifiers.
  private static final int SHORT_OPT = 's';
  private static final int LONG_OPT = 1;

  private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
      {
          new CLOptionDescriptor(null,
                                 CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                 SHORT_OPT,
                                 "option with only short form",
                                 new int[0]),
          new CLOptionDescriptor("long",
                                 CLOptionDescriptor.ARGUMENT_DISALLOWED,
                                 LONG_OPT,
                                 "option with long form")
      };

  public static void main(final String[] args)
  {
    System.out.println("Starting NoAlias...");
    System.out.println(CLUtil.describeOptions(OPTIONS));
    System.out.println();

    // Parse the arguments
    final CLArgsParser parser = new CLArgsParser(args, OPTIONS);

    //Make sure that there was no errors parsing arguments
    if (null != parser.getErrorString())
    {
      System.err.println("Error: " + parser.getErrorString());
      return;
    }

    for (final CLOption option : parser.getArguments())
    {
      switch (option.getId())
      {
        case CLOption.TEXT_ARGUMENT:
          //This occurs when a user supplies an argument that
          //is not an option
          System.out.println("Unknown arg: " + option.getArgument());
          break;

        case SHORT_OPT:
          System.out.println("Received short option");
          break;

        case LONG_OPT:
          System.out.println("Received long option");
          break;
      }
    }
  }
}