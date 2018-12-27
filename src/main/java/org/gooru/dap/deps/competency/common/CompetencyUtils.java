package org.gooru.dap.deps.competency.common;

import java.util.Iterator;

/**
 * @author gooru on 14-May-2018
 */
public final class CompetencyUtils {

  private CompetencyUtils() {
    throw new AssertionError();
  }

  public static String toPostgresArrayString(Iterator<String> input) {
    if (!input.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      String s = input.next();
      sb.append('"').append(s).append('"');
      if (!input.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',');
    }
  }
}
