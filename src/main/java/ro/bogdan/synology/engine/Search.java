package ro.bogdan.synology.engine;

import java.net.URL;
import java.util.List;

public interface Search {

	public List<URL> search(Searchable search);
}
