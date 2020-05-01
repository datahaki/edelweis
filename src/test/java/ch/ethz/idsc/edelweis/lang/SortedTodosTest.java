package ch.ethz.idsc.edelweis.lang;

import static ch.ethz.idsc.edelweis.lang.SortedTodos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SortedTodosTest extends TestCase {
  public void testSimple() {
    List<String> todoLines = new ArrayList<>();
    todoLines.add("                        // TODO @Hans 20190901 remove soon if no errors");
    todoLines.add("                        // TODO @ Hans 20190901 remove soon if no errors");
    todoLines.add("// TODO we did not name anybody here...errors");
    todoLines.add("// TODO @ Fritz resolve blabla ");
    List<String> developers = todoLines.stream().map(s -> SortedTodos.getDeveloper(s)).collect(Collectors.toList());
    // --
    Assert.assertTrue(developers.get(0).equals("Hans"));
    Assert.assertTrue(developers.get(1).equals("Hans"));
    Assert.assertTrue(developers.get(2).equals(UNKNOWN_IDENT));
    Assert.assertTrue(developers.get(3).equals("Fritz"));
  }
}
