package p2pcs;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import fileList.Chunks;

public class Server extends Thread{ 

	private Socket server; 
	public String Send_Filepah;
	public Server(Socket server,String Send_Filepah){ 
		this.server=server; 
		this.Send_Filepah = Send_Filepah;
	} 

	public void run(){ 
	
		try{
			BufferedReader in=new BufferedReader(new InputStreamReader(server.getInputStream())); 
			PrintWriter out=new PrintWriter(server.getOutputStream()); 
			String str=in.readLine();
			System.out.println(str); 
//===========================================收到了数据===分片把每一片传回去========================			
		    String[] sourceStrArray = str.split(",");
		   
		    int sumsplit=Chunks.split(Send_Filepah+sourceStrArray[0], Send_Filepah);
		    int your_hostnum=Integer.parseInt(sourceStrArray[1]);
		    int sumhost=Integer.parseInt(sourceStrArray[2]);
		    int limit=sumsplit/sumhost;//算出上限
		    	    
		    
		    if(your_hostnum==sumhost)
		    {
		    	 // 发送分片开始、结束信息
			    out.println(""+(your_hostnum-1)*limit+","+sumsplit+","+sumsplit);
			    out.flush();
			    // 发送分片
		    	for(int i=(your_hostnum-1)*limit;i<sumsplit;i++)
		    	{
		    		System.out.println("[1]: " + i);
		    		sendFile(Send_Filepah+sourceStrArray[0]+"-"+i, server.getInetAddress());
		    	}
		    }
		    else
		    {
		    	 // 发送分片开始、结束信息
			    out.println(""+(your_hostnum-1)*limit+","+your_hostnum*limit+","+sumsplit);
			    out.flush();
			    // 发送分片
		    	for(int i=(your_hostnum-1)*limit;i<your_hostnum*limit;i++)
		    	{
		    		System.out.println("[2]: " + i);
		    		sendFile(Send_Filepah+sourceStrArray[0]+"-"+i, server.getInetAddress());
		    	}
		    }
		    server.close(); 
		    
		    // 删除临时分片文件
		    for(int i=0; i<sumsplit; i++){
		    	File del = new File(Send_Filepah+sourceStrArray[0]+"-"+i);
		    	del.delete();
		    }
		}
		catch(IOException ex){ 	
		}
	} 
	
	public void sendFile(String fileName,InetAddress IPAddress) throws IOException {
		int length = 0;
		double sumL = 0 ;
		byte[] sendBytes = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		boolean bool = false;	
		try {
			File file = new File(fileName); //要传输的文件路径
			long l = file.length(); 
			dos = new DataOutputStream(server.getOutputStream());
			fis = new FileInputStream(file);      
			sendBytes = new byte[1024];  
			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
				sumL += length;  
				System.out.println("已传输："+((sumL/l)*100)+"%");
				dos.write(sendBytes, 0, length);
				dos.flush();
			} 
			//虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
			if(sumL==l){
				bool = true;
			}
		}catch (Exception e) {
			System.out.println("客户端文件传输异常");
			bool = false;
			e.printStackTrace();  
		} finally{  
			fis.close();   
		}
		System.out.println(bool?"成功":"失败");
	}
}