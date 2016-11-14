import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class AuthenticationThread extends Thread{
	private Socket cs;
	static String url = "jdbc:mysql://localhost/Network";
	static Connection conn = null;
	static Statement stmt = null;
	static java.sql.Statement st = null;
	static ResultSet rs = null;


	public static boolean checkDB(String name,String Password){
		try{
			conn = DriverManager.getConnection(url, "root", null);
			String sql = "select * from Users";
			java.sql.PreparedStatement prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			rs.next();
			String n = rs.getString(1).toString();
			String p = rs.getString(2).toString();

			if(n.equals(name) && p.equals(Password)){
				return true;
			}
			else{
				return false;
			}

		}
		catch(Exception e){
			System.out.println("Error in connecting");
		}
		return false;
	}




	public static boolean check(String Password){

		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("Password.txt")));
			String s = br.readLine().toString();

			if(s.equals(Password)){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		return false;
	}

	AuthenticationThread(Socket s) 
	{cs = s;}

	public void run()
	{
		Client c = new Client();
		int i = c.i;
		if(i==1){
			try {
				InputStreamReader isr = new InputStreamReader(cs.getInputStream());
				BufferedReader br =new BufferedReader(isr);
				String str=br.readLine();
				System.out.println("Received Request: "+str);
				if(checkDB("Hasnain", str)==true){
					PrintStream ps=new PrintStream(cs.getOutputStream());
					ps.println("Request Accepted Welcome \n");	
					cs.close();	
				}
				else{
					PrintStream ps=new PrintStream(cs.getOutputStream());
					ps.println("Password is not correct");	
					cs.close();	
				}
			}
			catch (Exception e)
			{}
		}
		else if(i == 2){
			try{
				InputStreamReader isr = new InputStreamReader(cs.getInputStream());
				BufferedReader br =new BufferedReader(isr);
				String str=br.readLine();
				System.out.println("Received Request: "+str);
				c.newUser("Hasnain", str);
			}
			catch(Exception e){
				System.out.println("Problem In Creating User");
			}
		}
	}

}
