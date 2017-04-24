package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import models.word.TaggedWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmafahlen on 2017-03-27.
 */
public class TaggerHelper {

    public static List<TaggedWord> getTaggedWords(JsonNode jsonNode) {
        JsonNode words = jsonNode.get("sentences").get(0);
        List<TaggedWord> taggedWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i).get("word_form").textValue();
            String lemma = words.get(i).get("lemma").textValue();
            String udPosTag = words.get(i).get("ud_tags").get("pos_tag").textValue();
            taggedWords.add(new TaggedWord(word, lemma, udPosTag));
        }
        return taggedWords;
    }

    /**
     * Converts a list of tagged words to a string.
     * @return A string containing all the tagged words.
     */
    public static String taggedToString(List<TaggedWord> taggedWords) {
        StringBuilder builder = new StringBuilder(" ");
        for (TaggedWord taggedWord : taggedWords) {
            builder.append(taggedWord.getWord()).append(" ");
        }
        return builder.toString();
    }
}
