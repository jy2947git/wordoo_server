package com.focaplo.wordee.server.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.focaplo.wordee.PuzzleRemoteService;
import com.focaplo.wordee.PuzzleService;
import com.focaplo.wordee.WordeePuzzle;
import com.google.appengine.api.datastore.Text;

import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PuzzleRemoteServiceImpl extends HttpServlet implements
		PuzzleRemoteService {
	private static final Logger log = Logger.getLogger(PuzzleRemoteServiceImpl.class.getName());
	private PuzzleService puzzleService = new PuzzleServiceImpl();
	public PuzzleService getPuzzleService() {
		return puzzleService;
	}

	public void setPuzzleService(PuzzleService puzzleService) {
		this.puzzleService = puzzleService;
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		serve(req, resp);
	}

	protected void serve(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		Map myParameterMap = new HashMap();
		if(request.getMethod().equalsIgnoreCase("post")){
			Map parameterMap = request.getParameterMap();
			if(!parameterMap.isEmpty() && !parameterMap.containsKey("command")){
				//
				String queryString = (String)parameterMap.keySet().iterator().next();
				
				StringTokenizer st = new StringTokenizer(queryString, "&");
				while(st.hasMoreTokens()){
					String s = st.nextToken();
					String p = s.substring(0,s.indexOf("="));
					String v = s.substring(s.indexOf("=")+1);
					myParameterMap.put(p, v);
				}
			}
		}
		String token = request.getParameter("token")!=null?request.getParameter("token"):(String)myParameterMap.get("token");
		
		if(token==null || !token.equalsIgnoreCase(PuzzleRemoteService.requestToken)){
			//bad
			log.warning("bad request comes from " + request.getRemoteAddr());
			return;
		}
		String command = request.getParameter("command")!=null?request.getParameter("command"):(String)myParameterMap.get("command");
		String start = request.getParameter("start")!=null?request.getParameter("start"):(String)myParameterMap.get("start");
		String end = request.getParameter("end")!=null?request.getParameter("end"):(String)myParameterMap.get("end");
		String puzzleName = request.getParameter("puzzleName")!=null?request.getParameter("puzzleName"):(String)myParameterMap.get("puzzleName");
		String puzzleAuthor = request.getParameter("puzzleAuthor")!=null?request.getParameter("puzzleAuthor"):(String)myParameterMap.get("puzzleAuthor");
		String puzzleData = request.getParameter("puzzleData")!=null?request.getParameter("puzzleData"):(String)myParameterMap.get("puzzleData");
		String id = request.getParameter("id")!=null?request.getParameter("id"):(String)myParameterMap.get("id");
		String rating=request.getParameter("rating")!=null?request.getParameter("rating"):(String)myParameterMap.get("rating");
		if(command==null){
			return;
		}
		
		String result = "";
		if(command.equalsIgnoreCase("browse")){
			result = this.browsePuzzlesByPage(Integer.parseInt(start), Integer.parseInt(end), null);
		}else if(command.equalsIgnoreCase("upload")){
			result = this.savePuzzle(puzzleName, puzzleAuthor, puzzleData);
		}else if(command.equalsIgnoreCase("download")){
			result = this.downloadPuzzleData(id);
		}else if(command.equalsIgnoreCase("delete")){
			this.getPuzzleService().deletePuzzle(Integer.parseInt(id));
		}else if(command.equalsIgnoreCase("truncate")){
			result = this.deleteAll();
		}else if(command.equalsIgnoreCase("rate")){
			result = this.ratePuzzle(id, rating);
		}else{
			log.warning("unsupported command parameter:" + command);
		}
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(result);
        log.warning(result);
	}

	private String ratePuzzle(String id, String rating) {
		this.getPuzzleService().ratePuzzle(Integer.parseInt(id), Integer.parseInt(rating));
		return "SUCCESS";
	}

	public String deleteAll(){
		this.getPuzzleService().deleteAll();
		return "SUCCESS";
	}

	
	public String downloadPuzzleData(String input){
		int id = 0;
		if(input!=null && !input.equals("")){
			try{
				id = Integer.parseInt(input);
			}catch(Exception e){
				log.warning("bad download parameter:" + input);
			}
		}
		return this.downloadPuzzleDataById(id);
	}
	
	public String downloadPuzzleDesc(String input){
		int id = 0;
		if(input!=null && !input.equals("")){
			try{
				id = Integer.parseInt(input);
			}catch(Exception e){
				log.warning("bad download parameter:" + input);
				return null;
			}
		}
		WordeePuzzle puzzle = this.getPuzzleService().getPuzzle(id);
		StringBuffer buf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		buf.append(puzzle.getPuzzleId()+"^"+puzzle.getPuzzleName() + "^" + puzzle.getAuthorName() + "^" + sdf.format(puzzle.getUploadedDate()) + "^" + puzzle.getDownloadCount());
		//TODO:encoding url????
		return buf.toString();
	}
	
	public String browsePuzzlesByPage(int start, int end, String sortBy) {
		//puzzlename^author^date|puzzlename^author^date
		List<WordeePuzzle> puzzles = this.getPuzzleService().browsePuzzlesList(start, end, sortBy);
		StringBuffer buf = new StringBuffer("SUCCESS");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		for(int i=0; i<puzzles.size();i++){
			WordeePuzzle puzzle = (WordeePuzzle)puzzles.get(i);
			
			buf.append("|");
			
			buf.append(puzzle.getPuzzleId()+"^"+puzzle.getPuzzleName() + "^" + puzzle.getAuthorName() + "^" + sdf.format(puzzle.getUploadedDate()) + "^" + puzzle.getDownloadCount() + "^" + puzzle.getRate());
		}
		//TODO: encoding url???
		return buf.toString();
	}

	public String downloadPuzzleDataById(int id) {
		//pazzleName=xxxx&puzzleData=xxxx
		WordeePuzzle puzzle = this.getPuzzleService().getPuzzle(id);
		
		//TODO:encoding url????
		return puzzle.getPuzzleData().getValue();
	}


	public String savePuzzle(String puzzleName, String puzzleAuthor,
			String puzzleData) {
		WordeePuzzle p = new WordeePuzzle();
		p.setAuthorName(puzzleAuthor);
		p.setPuzzleData(new Text(puzzleData));
		p.setPuzzleName(puzzleName);
		p.setUploadedDate(new Date());
		p.setDownloadCount(0);
		p.setRate(0);
		this.getPuzzleService().savePuzzle(p);
		return "SUCCESS";
	}

}
