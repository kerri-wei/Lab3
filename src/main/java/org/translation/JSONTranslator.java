package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, Map<String, String>> countryTranslationsMap;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        this.countryTranslationsMap = new HashMap<>();

        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray countries = new JSONArray(jsonString);

            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);

                String countryCode = country.getString("alpha3");
                Map<String, String> translations = new HashMap<>();

                for (String key : country.keySet()) {
                    if (!"id".equals(key) && !"alpha2".equals(key) && !"alpha3".equals(key)) {
                        translations.put(key, country.getString(key));
                    }
                }

                countryTranslationsMap.put(countryCode, translations);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        if (countryTranslationsMap.containsKey(country)) {
            Map<String, String> translations = countryTranslationsMap.get(country);
            return new ArrayList<>(translations.keySet());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        Set<String> countryCodes = countryTranslationsMap.keySet();
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {
        if (countryTranslationsMap.containsKey(country)) {
            Map<String, String> translations = countryTranslationsMap.get(country);
            return translations.get(language);
        }
        else {
            return "Country Not Found";
        }
    }
}
