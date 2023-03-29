import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String inputUrl = "https://google.com";
        String answer;
        try {
        if (Validators.checkIsBitlink(inputUrl, Addresses.isBitlink)) {
            answer = "Количество переходов " + BitlyMethods.countClicks(inputUrl);
        }
        else if (Validators.checkIsLink(inputUrl)) {
            answer = "Сокращенная ссылка " + BitlyMethods.shortUrl(inputUrl);
        }
        else {
            answer = "Ссылка " + inputUrl + " не является корректной";
        }
        } catch (IOException exc) {
            answer = "Что-то пошло не так, проверьте интернет-соединение";
        }
        System.out.println(answer);


    }
}