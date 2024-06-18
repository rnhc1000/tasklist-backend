import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Teste {

  public static void main(String[] args) {
    teste();
  }
  public static void teste() {
    Set<String> scope = Set.of("ROLE_ADMIN", "ROLE_USER");
    var scopes = scope.stream().map(x -> x).collect(Collectors.joining(" "));
    System.out.println(scopes);
  }
}
