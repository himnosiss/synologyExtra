package ro.bogdan.synology.engine;

import java.util.HashMap;
import java.util.Map;

public class Searchable {
	private String query;
	private Integer seariesNumber;
	private Integer startEpisode;
	private Integer stopEpisode;
	private Integer latestEpisode;
	private Boolean onlyFreeDownloads;
	private Boolean onlyAgregated;
	private String quality;
	private Map<String, Object> extraParams;

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

	public Integer getLatestEpisode() {
		return latestEpisode;
	}

	public void setLatestEpisode(Integer latestEpisode) {
		this.latestEpisode = latestEpisode;
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

}
