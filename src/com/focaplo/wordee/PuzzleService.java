package com.focaplo.wordee;

import java.util.List;

public interface PuzzleService {
	List<WordeePuzzle> browsePuzzlesList(int start, int end, String sortBy);
	String savePuzzle(WordeePuzzle puzzle);
	WordeePuzzle getPuzzle(int id);
	void deletePuzzle(int id);
	void deleteAll();
	void ratePuzzle(int id, int rating);
}
