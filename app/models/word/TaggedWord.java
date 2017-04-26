package models.word;

/**
 * Represents a word with added meta information from JSON Tagger API.
 * Used in the parsing process.
 *
 * @author Fredrik Kindstrom
 */
public class TaggedWord {
    private String word;
    private String lemma;
    private String udPosTag;

    public TaggedWord(String word, String lemma, String udPosTag) {
        this.word = word;
        this.lemma = lemma;
        this.udPosTag = udPosTag;
    }

    public String getWord() {
        return word;
    }
    public String getLemma() {
        return lemma;
    }
    public String getUdPosTag() {
        return udPosTag;
    }
}
