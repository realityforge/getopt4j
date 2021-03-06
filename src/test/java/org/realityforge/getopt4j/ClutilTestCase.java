package org.realityforge.getopt4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ClutilTestCase
{
  private static final String[] ARGLIST1 = new String[]{ "--you", "are", "--all", "-cler", "kid" };
  private static final String[] ARGLIST2 = new String[]{ "-Dstupid=idiot", "are", "--all", "here", "-d" };
  ///Contains duplicates
  private static final String[] ARGLIST3 = new String[]{ "-Dstupid=idiot", "are", "--all", "--all", "here" };
  ///Contains incompatable (blee/all)
  private static final String[] ARGLIST4 = new String[]{ "-Dstupid=idiot", "are", "--all", "--blee", "here" };
  private static final String[] ARGLIST5 = new String[]{ "-f", "myfile.txt" };
  private static final String[] ARGLIST6 = new String[]{ "-Dusername=some-user" };
  private static final String[] ARGLIST7 = new String[]{ "-f", "my-file.txt" };
  private static final int DEFINE_OPT = 'D';
  private static final int CASE_CHECK_OPT = 'd';
  private static final int YOU_OPT = 'y';
  private static final int ALL_OPT = 'a';
  private static final int CLEAR1_OPT = 'c';
  private static final int CLEAR2_OPT = 'l';
  private static final int CLEAR3_OPT = 'e';
  private static final int CLEAR5_OPT = 'r';
  private static final int BLEE_OPT = 'b';
  private static final int FILE_OPT = 'f';
  private static final int TAINT_OPT = 'T';
  private static final CLOptionDescriptor DEFINE =
    new CLOptionDescriptor( "define", CLOptionDescriptor.ARGUMENTS_REQUIRED_2, DEFINE_OPT, "define" );
  private static final CLOptionDescriptor CASE_CHECK =
    new CLOptionDescriptor( "charCheck",
                            CLOptionDescriptor.ARGUMENT_DISALLOWED,
                            CASE_CHECK_OPT,
                            "check character case sensitivity" );
  private static final CLOptionDescriptor YOU =
    new CLOptionDescriptor( "you", CLOptionDescriptor.ARGUMENT_DISALLOWED, YOU_OPT, "you" );
  private static final CLOptionDescriptor CLEAR1 =
    new CLOptionDescriptor( "c", CLOptionDescriptor.ARGUMENT_DISALLOWED, CLEAR1_OPT, "c" );
  private static final CLOptionDescriptor CLEAR2 =
    new CLOptionDescriptor( "l", CLOptionDescriptor.ARGUMENT_DISALLOWED, CLEAR2_OPT, "l" );
  private static final CLOptionDescriptor CLEAR3 =
    new CLOptionDescriptor( "e", CLOptionDescriptor.ARGUMENT_DISALLOWED, CLEAR3_OPT, "e" );
  private static final CLOptionDescriptor CLEAR5 =
    new CLOptionDescriptor( "r", CLOptionDescriptor.ARGUMENT_DISALLOWED, CLEAR5_OPT, "r" );
  private static final CLOptionDescriptor BLEE =
    new CLOptionDescriptor( "blee", CLOptionDescriptor.ARGUMENT_DISALLOWED, BLEE_OPT, "blee" );
  private static final CLOptionDescriptor ALL =
    new CLOptionDescriptor( "all",
                            CLOptionDescriptor.ARGUMENT_DISALLOWED,
                            ALL_OPT,
                            "all",
                            new CLOptionDescriptor[]{ BLEE } );
  private static final CLOptionDescriptor FILE =
    new CLOptionDescriptor( "file", CLOptionDescriptor.ARGUMENT_REQUIRED, FILE_OPT, "the build file." );
  private static final CLOptionDescriptor TAINT =
    new CLOptionDescriptor( "taint",
                            CLOptionDescriptor.ARGUMENT_OPTIONAL,
                            TAINT_OPT,
                            "turn on tainting checks (optional level)." );

  @Test
  public void optionalArgWithSpace()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ ALL, TAINT };

    final String[] args = new String[]{ "-T", "param", "-a" };

    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();

    assertEquals( clOptions.size(), 3 );

    final CLOption option0 = (CLOption) clOptions.get( 0 );
    assertEquals( TAINT_OPT, option0.getId() );
    assertNull( option0.getArgument( 0 ) );

    final CLOption option1 = (CLOption) clOptions.get( 1 );
    assertEquals( option1.getId(), CLOption.TEXT_ARGUMENT );
    assertEquals( option1.getArgument( 0 ), "param" );

    final CLOption option2 = (CLOption) clOptions.get( 2 );
    assertEquals( option2.getId(), ALL_OPT );
    assertNull( option2.getArgument( 0 ) );
  }

  @Test
  public void shortOptArgUnenteredBeforeOtherOpt()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ ALL, TAINT };

    final String[] args = new String[]{ "-T", "-a" };

    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();

    assertEquals( clOptions.size(), 2 );

    final CLOption option0 = (CLOption) clOptions.get( 0 );
    assertEquals( TAINT_OPT, option0.getId(), "Option Code: " + option0.getId() );
    assertNull( option0.getArgument( 0 ), "Option Arg: " + option0.getArgument( 0 ) );

    final CLOption option1 = (CLOption) clOptions.get( 1 );
    assertEquals( option1.getId(), ALL_OPT );
    assertNull( option1.getArgument( 0 ) );
  }

  @Test
  public void optionalArgsWithArgShortBeforeOtherOpt()
  {
    //"-T3","-a"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ ALL, TAINT };

    final String[] args = new String[]{ "-T3", "-a" };
    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 2 );
    final CLOption option0 = (CLOption) clOptions.get( 0 );
    assertEquals( option0.getId(), TAINT_OPT );
    assertEquals( option0.getArgument( 0 ), "3" );

    final CLOption option1 = (CLOption) clOptions.get( 1 );
    assertEquals( ALL_OPT, option1.getId() );
    assertNull( option1.getArgument( 0 ) );
  }

  @Test
  public void optionalArgsNoArgShortBeforeOtherOpt()
  {
    //"-T","-a"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ ALL, TAINT };

    final String[] args = new String[]{ "-T", "-a" };
    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 2 );
    final CLOption option0 = (CLOption) clOptions.get( 0 );
    assertEquals( TAINT_OPT, option0.getId() );
    assertNull( option0.getArgument( 0 ) );

    final CLOption option1 = (CLOption) clOptions.get( 1 );
    assertEquals( ALL_OPT, option1.getId() );
    assertNull( option1.getArgument( 0 ) );
  }

  @Test
  public void fullParse()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ YOU, ALL, CLEAR1, CLEAR2, CLEAR3, CLEAR5 };

    final CLArgsParser parser = new CLArgsParser( ARGLIST1, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 8 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), YOU_OPT );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions.get( 2 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions.get( 3 ) ).getId(), CLEAR1_OPT );
    assertEquals( ( (CLOption) clOptions.get( 4 ) ).getId(), CLEAR2_OPT );
    assertEquals( ( (CLOption) clOptions.get( 5 ) ).getId(), CLEAR3_OPT );
    assertEquals( ( (CLOption) clOptions.get( 6 ) ).getId(), CLEAR5_OPT );
    assertEquals( ( (CLOption) clOptions.get( 7 ) ).getId(), 0 );
  }

  @Test
  public void duplicateOptions()
  {
    //"-Dstupid=idiot","are","--all","--all","here"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE, ALL, CLEAR1 };

    final CLArgsParser parser = new CLArgsParser( ARGLIST3, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 5 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), DEFINE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions.get( 2 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions.get( 3 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions.get( 4 ) ).getId(), 0 );
  }

  @Test
  public void incompatibleOptions()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE, ALL, CLEAR1, BLEE };

    final CLArgsParser parser = new CLArgsParser( ARGLIST4, options );

    assertNotNull( parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 5 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), DEFINE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions.get( 2 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions.get( 3 ) ).getId(), BLEE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 4 ) ).getId(), 0 );
  }

  @Test
  public void singleArg()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ FILE };
    final CLArgsParser parser = new CLArgsParser( ARGLIST5, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), FILE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getArgument(), "myfile.txt" );
  }

  @Test
  public void singleArgWhereValueHasDash()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ FILE };
    final CLArgsParser parser = new CLArgsParser( ARGLIST7, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), FILE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getArgument(), "my-file.txt" );
  }

  @Test
  public void twoArgsParse()
  {
    //"-Dstupid=idiot","are","--all","here"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE, ALL, CLEAR1, CASE_CHECK };

    final CLArgsParser parser = new CLArgsParser( ARGLIST2, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 5 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), DEFINE_OPT );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions.get( 2 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions.get( 3 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions.get( 4 ) ).getId(), CASE_CHECK_OPT );

    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( "stupid", option.getArgument( 0 ) );
    assertEquals( "idiot", option.getArgument( 1 ) );
  }

  @Test
  public void twoArgsParseWhere2ndValueHashDash()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE };

    final CLArgsParser parser = new CLArgsParser( new String[]{ "-Dversion=1-2" }, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), DEFINE_OPT );

    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( "version", option.getArgument( 0 ) );
    assertEquals( "1-2", option.getArgument( 1 ) );
  }

  @Test
  public void twoArgsParseWithDashInValue()
  {
    //-Dusername=some-user
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE };

    final CLArgsParser parser = new CLArgsParser( ARGLIST6, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), DEFINE_OPT );

    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( "username", option.getArgument( 0 ) );
    assertEquals( "some-user", option.getArgument( 1 ) );
  }

  @Test
  public void partParse()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ YOU };

    final ParserControl control = new AbstractParserControl()
    {
      public boolean isFinished( int lastOptionCode )
      {
        return lastOptionCode == YOU_OPT;
      }
    };

    final CLArgsParser parser = new CLArgsParser( ARGLIST1, options, control );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    assertEquals( ( (CLOption) clOptions.get( 0 ) ).getId(), YOU_OPT );
    //"--you", "are", "--all", "-cler", "kid"
    assertEquals( parser.getUnParsedArgs(), new String[]{ "are", "--all", "-cler", "kid" } );
  }

  @Test
  public void twoPartParse()
  {
    final CLOptionDescriptor[] options1 = new CLOptionDescriptor[]{ YOU };
    final CLOptionDescriptor[] options2 = new CLOptionDescriptor[]{ ALL, CLEAR1, CLEAR2, CLEAR3, CLEAR5 };

    final ParserControl control1 = new AbstractParserControl()
    {
      public boolean isFinished( int lastOptionCode )
      {
        return lastOptionCode == YOU_OPT;
      }
    };

    final CLArgsParser parser1 = new CLArgsParser( ARGLIST1, options1, control1 );

    assertNull( parser1.getErrorString(), parser1.getErrorString() );

    final List clOptions1 = parser1.getArguments();
    final int size1 = clOptions1.size();

    assertEquals( size1, 1 );
    assertEquals( ( (CLOption) clOptions1.get( 0 ) ).getId(), YOU_OPT );

    final CLArgsParser parser2 =
      new CLArgsParser( parser1.getUnParsedArgs(), options2 );

    assertNull( parser2.getErrorString(), parser2.getErrorString() );

    final List clOptions2 = parser2.getArguments();
    final int size2 = clOptions2.size();

    assertEquals( size2, 7 );
    assertEquals( ( (CLOption) clOptions2.get( 0 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions2.get( 1 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions2.get( 2 ) ).getId(), CLEAR1_OPT );
    assertEquals( ( (CLOption) clOptions2.get( 3 ) ).getId(), CLEAR2_OPT );
    assertEquals( ( (CLOption) clOptions2.get( 4 ) ).getId(), CLEAR3_OPT );
    assertEquals( ( (CLOption) clOptions2.get( 5 ) ).getId(), CLEAR5_OPT );
    assertEquals( ( (CLOption) clOptions2.get( 6 ) ).getId(), 0 );
  }

  @Test
  public void twoPartPartialParse()
  {
    final CLOptionDescriptor[] options1 = new CLOptionDescriptor[]{ YOU, ALL, CLEAR1 };
    final CLOptionDescriptor[] options2 = new CLOptionDescriptor[]{};

    final AtomicInteger controlCallCount = new AtomicInteger();
    final ParserControl control = new AbstractParserControl()
    {
      public boolean isFinished( final int lastOptionCode )
      {
        controlCallCount.incrementAndGet();
        return lastOptionCode == CLEAR1_OPT;
      }
    };

    final CLArgsParser parser1 =
      new CLArgsParser( new String[]{ "--you", "are", "--all", "-cler", "kid" }, options1, control );
    assertEquals( controlCallCount.get(), 4 );

    assertNull( parser1.getErrorString(), parser1.getErrorString() );

    final List clOptions1 = parser1.getArguments();
    final int size1 = clOptions1.size();

    assertEquals( size1, 4 );
    assertEquals( ( (CLOption) clOptions1.get( 0 ) ).getId(), YOU_OPT );
    assertEquals( ( (CLOption) clOptions1.get( 1 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions1.get( 2 ) ).getId(), ALL_OPT );
    assertEquals( ( (CLOption) clOptions1.get( 3 ) ).getId(), CLEAR1_OPT );

    assertEquals( "ler", parser1.getUnParsedArgs()[ 0 ] );

    final CLArgsParser parser2 =
      new CLArgsParser( parser1.getUnParsedArgs(), options2 );

    assertNull( parser2.getErrorString(), parser2.getErrorString() );

    final List clOptions2 = parser2.getArguments();
    final int size2 = clOptions2.size();

    assertEquals( size2, 2 );
    assertEquals( ( (CLOption) clOptions2.get( 0 ) ).getId(), 0 );
    assertEquals( ( (CLOption) clOptions2.get( 1 ) ).getId(), 0 );
  }

  @Test
  public void duplicatesFail()
  {
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ YOU, ALL, CLEAR1, CLEAR2, CLEAR3, CLEAR5 };
    final CLArgsParser parser = new CLArgsParser( ARGLIST1, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );
  }

  @Test
  public void incomplete2Args()
  {
    //"-Dstupid="
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE };
    final CLArgsParser parser = new CLArgsParser( new String[]{ "-Dstupid=" }, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 1 );
    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( option.getId(), DEFINE_OPT );
    assertEquals( option.getArgument( 0 ), "stupid" );
    assertEquals( option.getArgument( 1 ), "" );
  }

  @Test
  public void incomplete2ArgsMixed()
  {
    //"-Dstupid=","-c"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE, CLEAR1 };
    final String[] args = new String[]{ "-Dstupid=", "-c" };

    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 2 );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), CLEAR1_OPT );
    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( option.getId(), DEFINE_OPT );
    assertEquals( option.getArgument( 0 ), "stupid" );
    assertEquals( option.getArgument( 1 ), "" );
  }

  @Test
  public void incomplete2ArgsMixedNoEq()
  {
    //"-Dstupid","-c"
    final CLOptionDescriptor[] options = new CLOptionDescriptor[]{ DEFINE, CLEAR1 };
    final String[] args = new String[]{ "-Dstupid", "-c" };

    final CLArgsParser parser = new CLArgsParser( args, options );

    assertNull( parser.getErrorString(), parser.getErrorString() );

    final List clOptions = parser.getArguments();
    final int size = clOptions.size();

    assertEquals( size, 2 );
    assertEquals( ( (CLOption) clOptions.get( 1 ) ).getId(), CLEAR1_OPT );
    final CLOption option = (CLOption) clOptions.get( 0 );
    assertEquals( option.getId(), DEFINE_OPT );
    assertEquals( option.getArgument( 0 ), "stupid" );
    assertEquals( option.getArgument( 1 ), "" );
  }

  /**
   * Test the getArgumentById and getArgumentByName lookup methods.
   */
  @Test
  public void argumentLookup()
  {
    final String[] args = { "-f", "testarg" };
    final CLOptionDescriptor[] options = { FILE };
    final CLArgsParser parser = new CLArgsParser( args, options );

    CLOption optionById = parser.getArgumentById( FILE_OPT );
    assertNotNull( optionById );
    assertEquals( FILE_OPT, optionById.getId() );

    CLOption optionByName = parser.getArgumentByName( FILE.getName() );
    assertNotNull( optionByName );
    assertEquals( FILE_OPT, optionByName.getId() );
  }

  /**
   * Test that you can have null long forms.
   */
  @Test
  public void nullLongForm()
  {
    final CLOptionDescriptor test =
      new CLOptionDescriptor( null, CLOptionDescriptor.ARGUMENT_DISALLOWED, 'n', "test null long form" );

    final String[] args = { "-n", "testarg" };
    final CLOptionDescriptor[] options = { test };
    final CLArgsParser parser = new CLArgsParser( args, options );

    final CLOption optionByID = parser.getArgumentById( 'n' );
    assertNotNull( optionByID );
    assertEquals( 'n', optionByID.getId() );

    assertNull( parser.getArgumentByName( FILE.getName() ) );
  }

  /**
   * Test that you can have null descriptions.
   */
  @Test
  public void nullDescription()
  {
    final CLOptionDescriptor test =
      new CLOptionDescriptor( "nulltest", CLOptionDescriptor.ARGUMENT_DISALLOWED, 'n', null );

    final String[] args = { "-n", "testarg" };
    final CLOptionDescriptor[] options = { test };
    final CLArgsParser parser = new CLArgsParser( args, options );

    final CLOption optionByID = parser.getArgumentById( 'n' );
    assertNotNull( optionByID );
    assertEquals( 'n', optionByID.getId() );

    final StringBuffer sb = CLUtil.describeOptions( options );
    final String lineSeparator = System.getProperty( "line.separator" );
    assertEquals( "\t-n, --nulltest" + lineSeparator, sb.toString() );
  }
}
