getopt4j - Command line parsing library
=======================================

[![Build Status](https://secure.travis-ci.org/realityforge/getopt4j.png?branch=master)](http://travis-ci.org/realityforge/getopt4j)

The "getopt4j" library is designed to parse the command line options in
the same manner as the C getopt() function in glibc (the GNU C runtime
library). It attempts to do this in a simpler, more Java-centric manner
than the original product.

The easiest way to download the library is to add the maven dependency;

```xml
<dependency>
   <groupId>org.realityforge.getopt4j</groupId>
   <artifactId>getopt4j</artifactId>
   <version>1.1</version>
</dependency>
```

The best way to understand how the library works is to dive into the
examples in the examples directory. However if you want to understand
more about how the parser works read on.

Parsing Rules
-------------

The command line is parsed according to the following rules. There are
two forms of options in this package, the long form and the short form.
The long form of an option is preceded by the '--' characters while the
short form is preceded by a single '-'. Some example options would be;
"--an-option", "-a", "--day", "-s -f -a".

In the tradition of UNIX programs, the short form of an option can occur
immediately after another short form option. So if 'a', 'b' and 'c' are
short forms of options that take no parameters then the following command
lines are equivalent: "-abc", "-a -bc", "-a -b -c", "-ab -c", etc.

Options can also accept arguments. You can specify that an option requires
an argument in which the text immediately following the option will be
considered to be an argument to the option. So if 'a' was an option that
required an argument then the following would be equivalent; "-abc", "-a bc"
(namely the option 'a' with argument 'bc').

Options can also specify optional arguments. In this case if there is any
text immediately following the option character then it is considered an
argument. Otherwise, the option has no arguments. For example if 'a' was an
option that required an optional argument then "-abc" is an option 'a' with
argument "bc" while "-a bc" is an option 'a' with no argument, followed by
the text "bc".

It is also possible to place an '=' sign between the option and it's argument.
So if we assume that a is an option that requires an argument then the
following are all equivalent; "-a=bc", "-a bc" "-abc".

In some cases it is also necessary to disable command line parsing so that
you can pass a text argument to the program that starts with a '-' character.
To do this insert the sequence '--' onto the command line with no text
immediately following it. This will disable processing for the rest of the
command line. The '--' characters will not be passed to the user program.
For instance the line "-- -b" would result in the program being passed the
text "-b" (ie. not as an option).

License
-------

The code originates from a small project written in 1998 and has remained
largely unchanged. However it has done several stints at some open-source
organizations and thus it needs to state "This product includes software
developed by the Apache Software Foundation (http://www.apache.org/)." Thanks
also goes out to Jeff Turner who added some documentation and Berin Loritsch
who fixed a parser bug that got through.
