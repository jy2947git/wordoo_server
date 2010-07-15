package com.focaplo.wordee;

public interface PuzzleRemoteService {
	public String requestToken="2983032043nksfd0-1fda-1hf";
	String browsePuzzlesByPage(int start, int end, String sortBy);
	String downloadPuzzleDataById(int id);
	String downloadPuzzleData(String input);
	String downloadPuzzleDesc(String input);
	String savePuzzle(String puzzleName, String puzzleAuthor, String puzzleData);
	String deleteAll();
}
