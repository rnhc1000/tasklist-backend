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

    for (int i = 5; i < 200; i+=5) {
      for (int j = 12; j < 200; j+=12) {
        for (int k = 0; k < 200; k+=18) {

          if(i == j && j == k) {
            System.out.println(i);
          }
        }
      }
    }
  }
}
