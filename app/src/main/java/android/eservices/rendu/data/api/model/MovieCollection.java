package android.eservices.rendu.data.api.model;

import java.util.ArrayList;
import java.util.List;

public class MovieCollection {

    private int id;
    private String name;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private List<Movie> parts;

    public MovieCollection() {
        parts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<Movie> getParts() {
        return parts;
    }

    public void setParts(List<Movie> parts) {
        this.parts = parts;
    }
}
