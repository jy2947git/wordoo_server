package com.focaplo.wordee.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.focaplo.wordee.PuzzleService;
import com.focaplo.wordee.WordeePuzzle;
import com.google.appengine.api.datastore.Key;

public class PuzzleServiceImpl implements PuzzleService {


	@SuppressWarnings("unchecked")
	public List<WordeePuzzle> browsePuzzlesList(int start, int end, String sortBy) {
		List<WordeePuzzle> results = new ArrayList();
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();

		Query query = pm.newQuery("select from " + WordeePuzzle.class.getName() + " order by uploadedDate desc");
//	    query.setOrdering("puzzleId  desc");
	    query.setRange(start, end);
	    try {
	        results = (List<WordeePuzzle>) query.execute();
	        
	    } finally {
	        query.closeAll();
	        pm.close();
	    }
		return results;
	}

	public WordeePuzzle getPuzzle(int id) {
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();
		WordeePuzzle puzzle = null;
		try{
			puzzle = (WordeePuzzle) pm.getObjectById(WordeePuzzle.class, id);
			long downloadCount = puzzle.getDownloadCount();
			downloadCount++;
			puzzle.setDownloadCount(downloadCount);
			pm.makePersistent(puzzle);
		}finally{
			pm.close();
		}
		return puzzle;
	}

	public String savePuzzle(WordeePuzzle puzzle) {
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();
		try{
			pm.makePersistent(puzzle);
		}finally{
			pm.close();
		}
		return puzzle.getPuzzleId().toString();
	}

	public void deletePuzzle(int id) {
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();
		try{
			pm.deletePersistent(pm.getObjectById(WordeePuzzle.class,id));
		}finally{
			pm.close();
		}
		
	}

	public void deleteAll() {
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();
		Query query = pm.newQuery(WordeePuzzle.class);
		try{
			query.deletePersistentAll();
		}finally{
			query.closeAll();
			pm.close();
		}
	}

	public void ratePuzzle(int id, int rating) {
		PersistenceManager pm = PersistenceManagerSingeton.instance().getPersistenceManager();
		WordeePuzzle puzzle = null;
		try{
			puzzle = (WordeePuzzle) pm.getObjectById(WordeePuzzle.class, id);
			//recalculate puzzle general rating
			double currentRating = puzzle.getRate();
			puzzle.setRate((currentRating+rating)/2);
			pm.makePersistent(puzzle);
		}finally{
			pm.close();
		}
	}


}
