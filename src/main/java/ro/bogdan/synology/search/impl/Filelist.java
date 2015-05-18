package ro.bogdan.synology.search.impl;

import javax.enterprise.inject.Default;

import ro.bogdan.synology.search.Search;


@Default
public class Filelist implements Search {

	@Override
    public String search(String search) {
	    // TODO Auto-generated method stub
	    return "filelist used";
    }

}
