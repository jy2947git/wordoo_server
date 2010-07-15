package com.focaplo.wordee;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.focaplo.wordee.server.impl.PuzzleServiceImpl;
import com.google.appengine.api.datastore.Text;

public class PuzzleServiceTestCase extends LocalDataStoreTest {
	@Test
	public void testAll(){
		System.out.println("------------saving....");
		this.testSavePuzzle();
		System.out.println("---------browsing....");
		this.testBrowsePuzzle();
		System.out.println("---------downloading....");
		this.testDownloadPuzzle();
		System.out.println("----------downloading....");
		this.testDownloadPuzzle();
		System.out.println("-----------browsing....");
		this.testBrowsePuzzle();
		System.out.println("---------deleting....");
		this.testDeletePuzzle();
//		this.testBrowsePuzzle();
	}
	@Test
	public void testDeleteAll(){
		PuzzleService test = new PuzzleServiceImpl();
		test.deleteAll();
	}
	@Test
	public void testSavePuzzle(){
		{
			WordeePuzzle p = new WordeePuzzle();
			p.setAuthorName("jerry you");
			p.setPuzzleData(new Text("test data"));
			p.setPuzzleName("test " + System.currentTimeMillis());
			p.setUploadedDate(new Date());
			PuzzleService test = new PuzzleServiceImpl();
			String result = test.savePuzzle(p);
			System.out.println("result:" + result);
		}
		{
			WordeePuzzle p = new WordeePuzzle();
			p.setAuthorName("jerry you");
			p.setPuzzleData(new Text("test data"));
			p.setPuzzleName("test " + System.currentTimeMillis());
			p.setUploadedDate(new Date());
			PuzzleService test = new PuzzleServiceImpl();
			String result = test.savePuzzle(p);
			System.out.println("result:" + result);
		}
		{
			WordeePuzzle p = new WordeePuzzle();
			p.setAuthorName("jerry you");
			p.setPuzzleData(new Text("test data"));
			p.setPuzzleName("test " + System.currentTimeMillis());
			p.setUploadedDate(new Date());
			PuzzleService test = new PuzzleServiceImpl();
			String result = test.savePuzzle(p);
			System.out.println("result:" + result);
		}
		{
			WordeePuzzle p = new WordeePuzzle();
			p.setAuthorName("jerry you");
			p.setPuzzleData(new Text("test data"));
			p.setPuzzleName("test  " + System.currentTimeMillis());
			p.setUploadedDate(new Date());
			PuzzleService test = new PuzzleServiceImpl();
			String result = test.savePuzzle(p);
			System.out.println("result:" + result);
		}
	}
	@Test
	public void testBrowsePuzzle(){
		PuzzleService test = new PuzzleServiceImpl();
		List<WordeePuzzle> results = test.browsePuzzlesList(0,25, "");
		for(int i=0;i<results.size();i++){
			WordeePuzzle p  = (WordeePuzzle)results.get(i);
			System.out.println(p.getPuzzleId());
			System.out.println(p.getPuzzleName());
			System.out.println(p.getAuthorName());
			System.out.println(p.getPuzzleData().getValue());
			System.out.println(p.getUploadedDate());
			System.out.println("downloaded:" + p.getDownloadCount());
		}
	}
	@Test
	public void testDownloadPuzzle(){
		PuzzleService test = new PuzzleServiceImpl();
		WordeePuzzle p  = test.getPuzzle(4);
		System.out.println(p.getPuzzleId());
		System.out.println(p.getPuzzleName());
		System.out.println(p.getAuthorName());
		System.out.println(p.getPuzzleData().getValue());
		System.out.println(p.getUploadedDate());
		System.out.println("downloaded:" + p.getDownloadCount());
	}
	@Test
	public void testDeletePuzzle(){
		PuzzleService test = new PuzzleServiceImpl();
		test.deletePuzzle(1);

	}
}
