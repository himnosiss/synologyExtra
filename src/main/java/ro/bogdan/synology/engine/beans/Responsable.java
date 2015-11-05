package ro.bogdan.synology.engine.beans;

import java.net.URL;

public class Responsable {
    private Integer id;
    private Integer seed = null;
    private URL url = null;
    private boolean agregated;
    private Integer episode;
    private Integer[] season;
    private boolean free;
    private String quality;
    private String name;

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public boolean isAgregated() {
        return agregated;
    }

    public void setAgregated(boolean agregated) {
        this.agregated = agregated;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Integer[] getSeason() {
        return season;
    }

    public void setSeason(Integer[] season) {
        this.season = season;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
