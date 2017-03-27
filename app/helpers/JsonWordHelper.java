package helpers;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by emmafahlen on 2017-03-27.
 */
public class JsonWordHelper {

    public static String getWordForm(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("word_form").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getLemma(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("lemma").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getSucPosTag(JsonNode json, int index) {
        String str =
            json.get("sentences").get(0).get(index).get("suc_tags").get("pos_tag").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getSucFeatures(JsonNode json, int index) {
        String str =
            json.get("sentences").get(0).get(index).get("suc_tags").get("features").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getUdPosTag(JsonNode json, int index) {
        String str =
            json.get("sentences").get(0).get(index).get("ud_tags").get("pos_tag").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getUdFeatures(JsonNode json, int index) {
        String str =
            json.get("sentences").get(0).get(index).get("ud_tags").get("features").toString();
        return str.substring(1, str.length() - 1);
    }

    public static String getTokenID(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("token_id").toString();
        return str.substring(1, str.length() - 1);
    }

    public static int getNumberOfWords(JsonNode json) {
        int counter = 0;
        while (true) {
            if (json.get("sentences").get(0).has(counter)) {
                counter++;
            } else {
                break;
            }
        }
        System.out.println("COUNT = " + counter);
        return counter;
    }
}
