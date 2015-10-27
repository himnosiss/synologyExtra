package ro.bogdan.synology.search;

import java.net.URL;
import java.util.List;

import ro.bogdan.synology.engine.beans.Searchable;

public interface Search {

	public Object[] search(Searchable search);
}
