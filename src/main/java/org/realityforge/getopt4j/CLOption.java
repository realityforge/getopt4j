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

  private String[] m_arguments;
  private CLOptionDescriptor m_descriptor;

  /**
   * Retrieve argument to option if it takes arguments.
   *
   * @return the (first) argument
   */
  public final String getArgument()
  {
    return getArgument(0);
  }

  /**
   * Retrieve indexed argument to option if it takes arguments.
   *
   * @param index The argument index, from 0 to
   *              {@link #getArgumentCount()}-1.
   * @return the argument
   */
  public final String getArgument(final int index)
  {
    if (null == m_arguments || index < 0 || index >= m_arguments.length)
    {
      return null;
    }
    else
    {
      return m_arguments[index];
    }
  }

  /**
   * Retrieve id of option.
   *
   * The id is eqivalent to character code if it can be a single letter option.
   *
   * @return the id
   */
  public final int getId()
  {
    return m_descriptor == null ? TEXT_ARGUMENT : m_descriptor.getId();
  }

  public final CLOptionDescriptor getDescriptor()
  {
    return m_descriptor;
  }

  /**
   * Constructor taking an descriptor
   *
   * @param descriptor the descriptor
   */
  public CLOption(final CLOptionDescriptor descriptor)
  {
    m_descriptor = descriptor;
  }

  /**
   * Constructor taking argument for option.
   *
   * @param argument the argument
   */
  public CLOption(final String argument)
  {
    this((CLOptionDescriptor) null);
    addArgument(argument);
  }

  /**
   * Mutator of Argument property.
   *
   * @param argument the argument
   */
  public final void addArgument(final String argument)
  {
    if (null == m_arguments)
    {
      m_arguments = new String[]{argument};
    }
    else
    {
      final String[] arguments = new String[m_arguments.length + 1];
      System.arraycopy(m_arguments, 0, arguments, 0, m_arguments.length);
      arguments[m_arguments.length] = argument;
      m_arguments = arguments;
    }
  }

  /**
   * Get number of arguments.
   *
   * @return the number of arguments
   */
  public final int getArgumentCount()
  {
    if (null == m_arguments)
    {
      return 0;
    }
    else
    {
      return m_arguments.length;
    }
  }

  /**
   * Convert to String.
   *
   * @return the string value
   */
  public final String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("[Option ");
    sb.append((char) m_descriptor.getId());

    if (null != m_arguments)
    {
      sb.append(", ");
      sb.append(Arrays.asList(m_arguments));
    }

    sb.append(" ]");

    return sb.toString();
  }
}
