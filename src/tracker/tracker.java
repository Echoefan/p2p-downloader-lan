package tracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tmplist.tmplist;

import fileList.fileList;
import peerp.peerp;
class ServerThread implements Runnable
{
	Socket s=null;
	BufferedReader br=null;
	public ServerThread(Socket s) throws IOException {
		this.s=s;
		br=new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	public void run()
	{
		try
		{
			String content=null;
			while((content=readFromClient())!=null)
			{
				if(content.charAt(0)=='u')
				{
					String t[]=content.split(" ");
					int count=0;
					for(int i=0;i<tracker.fl.size();i++)
					{
						
						if(tracker.fl.get(i).fileName.compareTo(t[1])==0)
						{
							String ip[]=s.getInetAddress().toString().split("/");
						    System.out.println(ip[1]);
						    int countp=0;
						    for(int p=0;p<tracker.fl.get(i).peers.size();p++)
						    {
						    	if(tracker.fl.get(i).peers.get(p).peerIP.compareTo(ip[1])==0)
						    		break;
						    	countp++;
						    }
						    if(countp==tracker.fl.get(i).peers.size())
							    tracker.fl.get(i).peers.add(new peerp(t[2],ip[1]));
						}
						else
							count++;
					}
					if(count==tracker.fl.size())
					{
						System.out.println(s.getInetAddress().toString());
						String ip[]=s.getInetAddress().toString().split("/");
						System.out.println(ip[1]);
						peerp pp=new peerp(t[2],ip[1]);
						fileList f=new fileList(t[1],pp);
						tracker.fl.add(f);					
					}
				
				}
				if(content.charAt(0)=='f')
				{
					ObjectOutputStream os=new ObjectOutputStream(s.getOutputStream());
					tmplist tl=new tmplist();
					tl.setList(tracker.fl);
					os.writeObject(tl);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private String readFromClient()
	{
		try{
			return br.readLine();
		}
		catch(IOException e)
		{
			tracker.socketList.remove(s);
		}
		return null;
	}
}

public class tracker {

	
	public static List<Socket> socketList=Collections.synchronizedList(new ArrayList<>());
	public static List<fileList> fl=Collections.synchronizedList(new ArrayList<>());
	
	static File file =new File("test.dat");
    static FileInputStream in;
    static FileOutputStream out;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket ss =new ServerSocket(30002);
			try {
				while(true)
				{
					Socket s=ss.accept();
					socketList.add(s);
					new Thread (new ServerThread(s)).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
