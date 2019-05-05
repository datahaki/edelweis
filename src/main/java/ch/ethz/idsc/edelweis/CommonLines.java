// code by jph
package ch.ethz.idsc.edelweis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.ethz.idsc.edelweis.lang.ParserJava;
import ch.ethz.idsc.edelweis.util.Sets;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;

public class CommonLines {
  private final Map<ParserJava, Set<String>> map;
  private final Map<String, Integer> result = new HashMap<>();

  public CommonLines(Stream<BulkParser> bulkParsers) {
    map = bulkParsers.map(BulkParser::codes) //
        .flatMap(List::stream) //
        .filter(ParserJava.class::isInstance) //
        .map(ParserJava.class::cast) //
        .collect(Collectors.toMap(parserJava -> parserJava, parserJava -> parserJava.lines().map(String::trim).collect(Collectors.toSet())));
    List<ParserJava> list = new ArrayList<>(map.keySet());
    for (int c0 = 0; c0 < list.size() - 1; ++c0)
      for (int c1 = c0 + 1; c1 < list.size(); ++c1) {
        Set<String> set = Sets.intersect(map.get(list.get(c0)), map.get(list.get(c1)));
        if (2 < set.size())
          result.put(list.get(c0).fileTitle() + " " + list.get(c1).fileTitle(), set.size());
      }
  }

  public Stream<String> matrix() {
    List<String> pairs = new ArrayList<>(result.keySet());
    Collections.sort(pairs, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return -Integer.compare(result.get(o1), result.get(o2));
      }
    });
    return pairs.stream().limit(400).map(pair -> String.format("%5d %s", result.get(pair), pair));
  }

  public Tensor vector() {
    return Tensor.of(result.values().stream().sorted().map(RealScalar::of));
  }
}
