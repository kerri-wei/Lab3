package org.translation;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {
    private static final CountryCodeConverter COUNTRY_CODE_CONVERTER = new CountryCodeConverter();
    private static final LanguageCodeConverter LANGUAGE_CODE_CONVERTER = new LanguageCodeConverter();

    /**
     * Main entry point of our Translation System.<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * Runs the main program logic, prompting the user for input and translating as necessary.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        String quit = "quit";
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String countryName = promptForCountry(translator);
                if (quit.equals(countryName)) {
                    break;
                }
                String countryCode = COUNTRY_CODE_CONVERTER.fromCountry(countryName);

                String languageName = promptForLanguage(translator, countryCode);
                if (quit.equals(languageName)) {
                    break;
                }
                String languageCode = LANGUAGE_CODE_CONVERTER.fromLanguage(languageName);

                System.out.println(countryName + " in " + languageName + " is " + translator
                        .translate(countryCode, languageCode));
                System.out.println("Press enter to continue or quit to exit.");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String textTyped = scanner.nextLine();

                if (quit.equals(textTyped)) {
                    break;
                }
            }
        }
    }

    /**
     * Prompts the user to select a country and returns their choice.
     * @param translator the Translator implementation to use in the program
     * @return the user's selected country
     */
    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        countries.replaceAll(COUNTRY_CODE_CONVERTER::fromCountryCode);
        Collections.sort(countries);
        countries.forEach(System.out::println);

        System.out.println("select a country from above:");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    /**
     * Prompts the user to select a language and returns their choice.
     * @param translator the Translator implementation to use in the program
     * @param countryCode the 3-letter country code of the selected country
     * @return the user's selected language
     */
    private static String promptForLanguage(Translator translator, String countryCode) {
        List<String> languages = translator.getCountryLanguages(countryCode);
        languages.replaceAll(LANGUAGE_CODE_CONVERTER::fromLanguageCode);
        Collections.sort(languages);
        languages.forEach(System.out::println);

        System.out.println("select a language from above:");
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
