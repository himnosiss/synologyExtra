package ro.bogdan.synology.search;

import java.net.URL;
import java.util.List;

import ro.bogdan.synology.engine.Searchable;

public interface Search {

	public List<URL> search(Searchable search);
}
