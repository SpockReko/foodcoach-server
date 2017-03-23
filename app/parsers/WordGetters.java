package parsers;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class WordGetters {


    public String getWordForm(JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("word_form").toString();
        return str.substring(1, str.length()-1);
    }

    public String getLemma (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("lemma").toString();
        return str.substring(1, str.length()-1);
    }

    public String getSucPosTag (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("suc_tags").get("pos_tag").toString();
        return str.substring(1, str.length()-1);
    }

    public String getSucFeatures (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("suc_tags").get("features").toString();
        return str.substring(1, str.length()-1);
    }

    public String getUdPosTag (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("ud_tags").get("pos_tag").toString();
        return str.substring(1, str.length()-1);
    }

    public String getUdFeatures (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("ud_tags").get("features").toString();
        return str.substring(1, str.length()-1);
    }

    public String getTokenID (JsonNode json, int index){
        String str = json.get("sentences").get(0).get(index).get("token_id").toString();
        return str.substring(1, str.length()-1);
    }

    public int getNumberOfWords (JsonNode json){
        int counter = 0;
        while (true) {
            if (json.get("sentences").get(0).has(counter)) {
                counter++;
            }
            else {
                break;
            }
        }
        System.out.println("COUNT = " + counter);
        return counter;
    }

}


/*
* 1. Kolla om receptraden har () / eller något av andra avdelande ord och avdela därefter
* 2. Kolla vad som är adjektiv, verb och numeralt och sortera därefter
* 3. Resten får köras in i IngredientParser:
*   3.1. De ord som är efter varandra körs in som EN stäng; om vi hittar nåt, spara, annars kör in uppdelat
*
*
*
* */