package ro.bogdan.synology.search;

import ro.bogdan.synology.engine.beans.Searchable;

public interface Search {

	public Object[] search(Searchable search);
}
