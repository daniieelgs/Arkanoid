package arkanoid;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {

	private JSONObject jsonobject;
	
	private JSONArray userData;
	private JSONArray userList;
	
	private String path;
	
	public JSONReader(FileReader stream, String path) throws IOException, ParseException {
		
		this.path = path;
		
		jsonobject = (JSONObject) new JSONParser().parse(stream);
		
		userData = (JSONArray) jsonobject.get("userData");
		userList = (JSONArray) jsonobject.get("userList");
		
	}
	
	public JSONReader(File file) throws IOException, ParseException {
		this(new FileReader(file), file.getPath());
	}
	
	public JSONReader(String file) throws IOException, ParseException {	
		this(new FileReader(file), file);
	}
	
	public String[] getUsersName() {
				
		String[] usersName = new String[userList.size()];
		
		for(int i = 0; i < userList.size(); i++)
			usersName[i] = String.valueOf(userList.get(i));
		
		return usersName;
		
	}
	
	public String getUserHighestScore() {
		
		String userName = null;
		
		int score = 0;
		
		for(Object userObject : userData) {
			
			JSONObject user = (JSONObject) userObject;
			
			int userScore = Integer.parseInt(String.valueOf(user.get("score")));
			
			if(userScore > score) {
				
				userName = String.valueOf(user.get("name"));
				score = userScore;
				
			}
			
		}
		
		return userName;
		
	}
	
	public int getScoreUser(String userName) {
		
		JSONObject user = getUser(userName);
		
		if(user != null ) return Integer.parseInt(String.valueOf(user.get("score")));
		
		return -1;
	}
	
	public boolean userExist(String userName) {
				
		for(Object user : userData)
			if(String.valueOf(((JSONObject) user).get("name")).equalsIgnoreCase(userName))
				return true;
		
			
		return false;
		
	}
	
	public JSONObject getUser(String userName) {
		
		for(Object user : userData)
			if(String.valueOf(((JSONObject) user).get("name")).equalsIgnoreCase(userName))
				return (JSONObject) user;
		
			
		return null;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void saveUser(String userName, int score) throws IOException {
		
		JSONObject user = getUser(userName);
		
		if(user != null) user.put("score", score);	
		else {
			
			user = new JSONObject();
			
			user.put("name", userName);
			user.put("score", score);
			
			userData.add(user);
			userList.add(userName);
			
		}
		
		String[] userNames = getUsersName();
				
		String[] newUserList = new String[userNames.length];
		
		for(int i = 0; i < userNames.length; i++) {
			
			if(!userNames[i].equalsIgnoreCase(userName)) newUserList[i] = userNames[i];
			userList.remove(userNames[i]);
			
		}
		
		userList.remove(userName);
		
		userList.add(userName);
		
		for (String name : newUserList)
			if(name != null) userList.add(name);

		FileWriter streamSave = new FileWriter(path);
		
		streamSave.write(jsonobject.toJSONString());
		
		streamSave.close();
		
	}
	
	
}