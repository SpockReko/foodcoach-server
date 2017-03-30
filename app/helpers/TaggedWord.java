package helpers;

/**
 * Created by fredrikkindstrom on 2017-03-30.
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
