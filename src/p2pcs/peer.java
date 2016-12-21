package p2pcs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import tmplist.tmplist;
import fileList.fileList;
public class peer {

    public static   List <fileList> gotted_lists;
	public static  List <fileList> getmeesage(String inputOrder,String hostip) throws Exception {
		Socket s=new Socket(hostip,30002);
		//new Thread(new ClientThread(s)).start();\
		PrintStream ps=new PrintStream(s.getOutputStream());
		//String line=null;
		//BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		while(inputOrder!=null)
		{
			try {
				InetAddress ia=null;
				ia=InetAddress.getLocalHost();
				String ln=ia.getHostName();
				String send;
				send=inputOrder+ln;
				System.out.println(send);
				ps.println(send);
				BufferedInputStream bi=new BufferedInputStream(s.getInputStream());
				ObjectInputStream is =new ObjectInputStream(bi);
				Object obj =is.readObject();
				if(obj!=null)
				{
					tmplist t=(tmplist)obj;	
				    gotted_lists=t.getlist();
					
					System.out.println("got data!!");
					System.out.println(gotted_lists.get(0).peers.get(0).peerIP);
					return gotted_lists;
				}
				s.close();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	
	}
	public static  void upfilefun(String inputOrder,String hostip) throws Exception {
		Socket s=new Socket(hostip,30002);
		//new Thread(new ClientThread(s)).start();\
		PrintStream ps=new PrintStream(s.getOutputStream());
		//String line=null;
		//BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		while(inputOrder!=null)
		{
			try {
				InetAddress ia=null;
				ia=InetAddress.getLocalHost();
				String ln=ia.getHostName();
				String send;
				send="u"+" "+inputOrder+" "+ln;
				System.out.println(send);
				ps.println(send);
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
