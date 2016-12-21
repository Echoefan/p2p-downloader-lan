package p2pcs;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import peerp.peerp;
import fileList.Chunks;
import fileList.fileList;

	

	public class Client extends Thread {

		Socket so; // 套接字
		fileList searchfilelist;
		static int chunks_sum; // 分片总数
		int i;
		String filename;
		String filepath;
		
		public Client(fileList searchfilelist, int i, String filename,String filepath) {
			this.searchfilelist = searchfilelist;
			this.i = i;
			this.filename = filename;
			this.so = null;
			this.filepath = filepath;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				InetAddress IPAddress = InetAddress.getByName(searchfilelist.peers.get(i).peerIP);
				System.out.println(InetAddress.getLocalHost());
				System.out.println("现在获得了所需文件所在主机的IP地址和端口号"); 
				String request=searchfilelist.fileName+","+(i+1)+","+searchfilelist.peers.size();    //此处获取文件发送方对应的片的序号，可能有多个        		
				connect(IPAddress,30001);            
				System.out.println(request); //向文件的所有者发送传送文件的请求
				send(request,searchfilelist.fileName);  
				receiveFile(filename);
				}
			catch (SocketException e) 
			{
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}	
		}

		//完成请求的传递和文件的接收
		private void send(String sendMessage, final String filename) throws IOException {
			System.out.println("现在开始连接文件发送端");
			PrintWriter out=new PrintWriter(so.getOutputStream()); 
			out.println(sendMessage); 
			out.flush();   
		}
		
		private void connect(InetAddress address,int port) throws SocketException { //创建连接
			try {
				so=new Socket(address,port);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				System.out.println("创建socket失败");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block			
				System.out.println("创建socket失败");
				e1.printStackTrace();
			}
		}

		private void receiveFile(String filename) throws IOException {
			byte[] inputByte = null;
			DataInputStream dis = null;
			FileOutputStream fos = null;

			//接受分片开始、结束信息
			BufferedReader in=new BufferedReader(new InputStreamReader(so.getInputStream()));
			String[] messege = in.readLine().split(",");
			int chunks_start=Integer.parseInt(messege[0]);
			int chunks_end=Integer.parseInt(messege[1]);
			chunks_sum=Integer.parseInt(messege[2]);
			try {
				dis = new DataInputStream(so.getInputStream());
				for (int i=chunks_start; i<chunks_end; i++){
					try {
						File f = new File(filepath);
						if(!f.exists()){
							f.mkdir();  
						}
						/*  
						 * 文件存储位置  
						 */
						String filePath = filepath+filename+"-"+i;
						fos = new FileOutputStream(new File(filePath));    
						inputByte = new byte[1024];   
						System.out.println("开始接收数据...");  
						int j = 0;
						while (true) {
							dis.readFully(inputByte);
							fos.write(inputByte);
							fos.flush();  
							j++;
							if(j == 1024)
								break;
						}
						System.out.println("完成接收："+filePath);
					} 
					finally {
						if (fos != null)
							fos.close();
					}
				}
			}catch (IOException ie){
				;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static void clientdown(String filepath,String filename,String hostip)throws Exception{ 
			
				System.out.println(filepath);
				try {
					List<fileList> filelist=peer.getmeesage("f",hostip);
					System.out.println(filelist.get(0).peers.get(0).peerIP);
				  
				  //************test_data*******************************************
				  fileList searchfilelist=null;//存储查询到的filelist
				  int sumhosts=0;
			      //String filename=filename1;//在界面选择得到一个需要下载的文件名
				  for(int j = 0; j < filelist.size(); j++)  
			      {  
					  if(filelist.get(j).fileName.equals(filename))
						{
						    searchfilelist=filelist.get(j);
							break;
						}
			      } 
				  //===========================获取到数据并准备好数据准备传输===========================
				 sumhosts=searchfilelist.peers.size();
				 ArrayList<Client> threads = new ArrayList<Client>();
				 for(int i=0;i<sumhosts;i++)
				 {
						 threads.add(new Client(searchfilelist, i, filename,filepath));
						 threads.get(i).start();
				 } 
				 // 路障 
				 for(int i=0;i<sumhosts;i++){
					 threads.get(i).join();
				 }
					 
				 // 合并文件
				 Chunks.merge(filepath+filename, filepath, Client.chunks_sum);
				 peer.upfilefun(filename,hostip);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}