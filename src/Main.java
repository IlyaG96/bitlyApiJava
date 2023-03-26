import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputUrl = "https://vk.com";
        if (Validators.checkIsBitlink(inputUrl, Addresses.isBitlink))
            System.out.println("Общее количество переходов: "+ BitlyMethods.countClicks(inputUrl));
        else if (Validators.checkIsLink(inputUrl)) {
            System.out.println("Сокращенная ссылка " + BitlyMethods.shortUrl(inputUrl));
        }
        else {
            System.out.println("Ссылка " + inputUrl + " не является корректной");
        }
    }
}