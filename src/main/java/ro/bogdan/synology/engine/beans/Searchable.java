package ro.bogdan.synology.engine.beans;

import java.util.HashMap;
import java.util.Map;

public class Searchable {
	private String query;
	private Integer seariesNumber = null;
	private Integer startEpisode = null;
	private Integer stopEpisode = null;
	private Boolean latestEpisode = false;
	private Integer latestEpisodes = 1;
	private Boolean onlyFreeDownloads = false;
	private Boolean onlyAgregated = false;
	private Boolean ignoreMissing = false;
	private String quality = null;
	private String category = null;
	private Map<String, Object> extraParams;
	private Long sleepTime = 20000L;

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getSeariesNumber() {
		return seariesNumber;
	}

	public void setSeariesNumber(Integer seariesNumber) {
		this.seariesNumber = seariesNumber;
	}

	public Integer getStartEpisode() {
		return startEpisode;
	}

	public void setStartEpisode(Integer startEpisode) {
		this.startEpisode = startEpisode;
	}

	public Integer getStopEpisode() {
		return stopEpisode;
	}

	public void setStopEpisode(Integer stopEpisode) {
		this.stopEpisode = stopEpisode;
	}

	public Boolean getOnlyFreeDownloads() {
		return onlyFreeDownloads;
	}

	public void setOnlyFreeDownloads(Boolean onlyFreeDownloads) {
		this.onlyFreeDownloads = onlyFreeDownloads;
	}

	public Boolean getOnlyAgregated() {
		return onlyAgregated;
	}

	public void setOnlyAgregated(Boolean onlyAgregated) {
		this.onlyAgregated = onlyAgregated;
	}

	public Map<String, Object> getExtraParams() {
		return extraParams;
	}

	public void setExtraParams(Map<String, Object> extraParams) {
		this.extraParams = extraParams;
	}

	public void addExtraParams(String key, Object value) {
		if (this.extraParams == null) {
			this.extraParams = new HashMap<String, Object>();
		}
		this.extraParams.put(key, value);
	}

	public Boolean getLatestEpisode() {
		return latestEpisode;
	}

	public void setLatestEpisode(Boolean latestEpisode) {
		this.latestEpisode = latestEpisode;
	}

	public Integer getLatestEpisodes() {
		return latestEpisodes;
	}

	public void setLatestEpisodes(Integer latestEpisodes) {
		this.latestEpisodes = latestEpisodes;
	}

	public Boolean getIgnoreMissing() {
		return ignoreMissing;
	}

	public void setIgnoreMissing(Boolean ignoreMissing) {
		this.ignoreMissing = ignoreMissing;
	}

	@Override
	public String toString() {
		return "query:" + query + " serie:" + seariesNumber + " firstEpisode:" + startEpisode + " lastEpisode:"
		        + stopEpisode + " ignore missing:" + ignoreMissing + " onlyFree:" + onlyFreeDownloads + " onlyAgregated:" + onlyAgregated
		        + " latestEpisode:" + latestEpisode + " latestEpisodes:" + latestEpisodes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }
	
}
