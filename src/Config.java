import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    static Dotenv dotenv = Dotenv.load();
    public static String bitlyToken = dotenv.get("BITLY_TOKEN");

}
