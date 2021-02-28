package android.eservices.rendu.data.api.model;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {
    @SerializedName("iso_639_1")
    private String iso6391;
    private String name;
    @SerializedName("english_name")
    private String englishName;

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
