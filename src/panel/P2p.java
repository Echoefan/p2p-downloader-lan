package panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fileList.fileList;
import p2pcs.Client;
import p2pcs.Server;
import p2pcs.peer; 


public class P2p {
    
    public static void main(String[] args) {    
    	
    	
    	//主页面
        final JFrame mainframe = new JFrame("P2P 	PROJECT");
        mainframe.setSize(800, 600);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel panel = new JPanel(); 
        mainframe.add(panel);
        
        panel.setLayout(null);
        
        //定位初始值
        int xint = 70;
        int yint = 20;
        //边框
        LineBorder lineBorder = (LineBorder)BorderFactory.createLineBorder(Color.black);

        //链接状态
        JLabel userLabel = new JLabel("您好，您当前的连接状态为：已连接");
        userLabel.setBounds(xint,yint,300,25);//x,y,width,height
        panel.add(userLabel);
        
        //主机列表标注
        JLabel hostLabel = new JLabel("局域网内活动主机列表");
        hostLabel.setBounds(xint,yint+20,200,25);
        panel.add(hostLabel);
        
        //主机列表
        final Vector<String> hostvector = new Vector<String>();
       // hostvector.add("user1");
       // hostvector.add("user2");
        //hostvector.add("user3");
        final JList hostlist = new JList(hostvector);
        hostlist.setBounds(xint,yint+50, 220, 250);
        hostlist.setBorder(lineBorder);
        panel.add(hostlist);
        
        //host IP列表标注
        JLabel hostipjl = new JLabel("tracker IP地址(请输入)：");
        hostipjl.setBounds(xint+240,yint,150,25);
        panel.add(hostipjl);
        //hostIP输入框
        final JTextField hostipjtf = new JTextField();
        hostipjtf.setBounds(xint+400,yint,200,25);
        panel.add(hostipjtf);
        
        
        //下载文件路径
        final JTextField downfiletf = new JTextField();
        downfiletf.setBounds(xint+10, yint+350, 200, 25);
        downfiletf.setEditable(false);
        panel.add(downfiletf);
        
        //浏览文件按钮
        JButton filedownButton = new JButton("选择路径");
        filedownButton.setBounds(xint+10,yint+390,200, 25);
        filedownButton.addActionListener(new ActionListener() {
        	   @Override
        	   public void actionPerformed(ActionEvent e) {
        		 //按钮点击事件
        		    JFileChooser chooser = new JFileChooser();
        		    String choosertitle="";
        		    chooser = new JFileChooser();
        		    chooser.setCurrentDirectory(new java.io.File("."));
        		    chooser.setDialogTitle(choosertitle);
        		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		    chooser.setAcceptAllFileFilterUsed(false);
        		    if (chooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
        		    	downfiletf.setText(chooser.getSelectedFile()+"\\");
        		    	String filepath = downfiletf.getText();
        		    	//开启server
        		    	new Thread(new ServerThread(filepath)).start();
        		    	filedownButton.setEnabled(false);
        		    } 
        	   }
        	  });
        panel.add(filedownButton);
        
        //上传文件路径
        final JTextField curfiletf = new JTextField();
        curfiletf.setBounds(xint+10, yint+450, 200, 25);
        curfiletf.setEditable(false);
        panel.add(curfiletf);
        
        //浏览文件按钮
        JButton fileupdateButton = new JButton("浏览文件");
        fileupdateButton.setBounds(xint+10,yint+490, 100, 25);
        fileupdateButton.addActionListener(new ActionListener() {
        	   @Override
        	   public void actionPerformed(ActionEvent e) {
        		 ////按钮点击事件
        		   String filepath2 = downfiletf.getText();
        		   JFileChooser jfc = new JFileChooser(filepath2);
        		     if(jfc.showOpenDialog(mainframe)==JFileChooser.APPROVE_OPTION ){
        		    	 curfiletf.setText(jfc.getSelectedFile().getAbsolutePath());
        		     }
        	   }
        	  });
        panel.add(fileupdateButton);
        
        //确认上传文件按钮
        JButton filelistButton = new JButton("确认上传");
        filelistButton.setBounds(xint+110,yint+490, 100, 25);
        filelistButton.addActionListener(new ActionListener() {
     	   @Override
     	   public void actionPerformed(ActionEvent e) {
     		   ////按钮点击事件
     		   String hostip = hostipjtf.getText().toString();
     		   String upfile = curfiletf.getText().toString();
     		   String [] spfile = upfile.split("\\");
     		   upfile = spfile[spfile.length-1];
     		   try {
				peer.upfilefun(upfile,hostip);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
     		   
     	   }
     	  });
        panel.add(filelistButton);
        
        //本地文件列表标注
        JLabel localfileLabel = new JLabel("本地共享文件列表");
        localfileLabel.setBounds(xint+240,yint+20,200,25);
        panel.add(localfileLabel);
        
        //本地文件列表
        final Vector<String> localfilevector = new Vector<String>();
        //localfilevector.add("a.txt");
        //localfilevector.add("b.jpg");
        final JList localfilelist = new JList(localfilevector);
        localfilelist.setBounds(xint+240,yint+50, 190, 300);
        localfilelist.setBorder(lineBorder);
        panel.add(localfilelist);

        //选中文件主机列表
        final Vector<String> hostchosedvector = new Vector<String>();
        //hostchosedvector.add("user1");
        final JList hostchosedlist = new JList(hostchosedvector);
        hostchosedlist.setBounds(xint+240,yint+380, 390, 100);
        hostchosedlist.setBorder(lineBorder);
        panel.add(hostchosedlist);
        
        //局域网内文件列表标注
        JLabel netfileLabel = new JLabel("局域网内共享文件列表");
        netfileLabel.setBounds(xint+440,yint+20,200,25);
        panel.add(netfileLabel);
        
        //局域网内共享文件列表
        final Vector<String> netfilevector = new Vector<String>();
        //netfilevector.add("a.txt");
        //netfilevector.add("b.jpg");
        //netfilevector.add("c.mp3");
        //netfilevector.add("d.wav");
        final JList netfilelist = new JList(netfilevector);
        netfilelist.setBounds(xint+440,yint+50, 190, 300);
        netfilelist.setBorder(lineBorder);
        netfilelist.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				List<fileList> filelist;
				String hostip = hostipjtf.getText().toString();
				
				try {
					hostchosedvector.clear();
					filelist = peer.getmeesage("f",hostip);
					String filechosedstr = (String) netfilelist.getSelectedValue();
					for(int i=0;i<filelist.size();i++)
					{
						if(filelist.get(i).fileName.equals(filechosedstr))
						{
							for(int j=0;j<filelist.get(i).peers.size();j++)
							{
								hostchosedvector.add(filelist.get(i).peers.get(j).hostname);
							}
							break;
						}
					}
					hostchosedlist.setListData(hostchosedvector);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	
		});
        panel.add(netfilelist);
        
        //选中主机标注
        JLabel hostchosedLabel = new JLabel("拥有选中文件的主机");
        hostchosedLabel.setBounds(xint+240,yint+350,200,25);
        panel.add(hostchosedLabel);
        
        
        
        //下载按钮
        JButton downloadButton = new JButton("P2P并行下载");
        downloadButton.setBounds(xint+440,yint+352,190,25);
        downloadButton.addActionListener(new ActionListener() {
      	   @Override
      	   public void actionPerformed(ActionEvent e) {
      		   ////按钮点击事件
      		   try {
      			 String hostip = hostipjtf.getText().toString();
      			   String filename = netfilelist.getSelectedValue().toString();
      			   String filepath = downfiletf.getText().toString();
					if(filepath!=null&&filename!=null)
					{
						Client.clientdown(filepath, filename,hostip);
					}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      	   }
      	  });
        panel.add(downloadButton);
        
        //下载进度标签
        JLabel processLabel = new JLabel("下载进度：");
        processLabel.setBounds(xint+240,yint+490,200,25);
        panel.add(processLabel);
        
        //下载进度条
        JProgressBar progress = new JProgressBar();
        progress.setBounds(xint+300,yint+490,326,20);
        progress.setMinimum(0);  
        progress.setMaximum(100);  
        progress.setValue(80);   
        panel.add(progress);
        mainframe.setVisible(true);
      //刷新列表按钮
        JButton updateLabel = new JButton("刷新列表");
        updateLabel.setBounds(xint+10,yint+310, 200, 25);
        updateLabel.addActionListener(new ActionListener() {
     	   @Override
     	   public void actionPerformed(ActionEvent e) {
     		 //按钮点击时事件
     		  try {
     			hostvector.clear();
     			netfilevector.clear();
     			localfilevector.clear();
 				hostlist.setListData(hostvector);
 				netfilelist.setListData(netfilevector);
 				String hostip = hostipjtf.getText().toString();
				List<fileList> filelist=peer.getmeesage("f",hostip);
				if(filelist!=null)
				{
					InetAddress ia=null;
					ia=InetAddress.getLocalHost();
					String ln=ia.getHostName();
					for(int i=0;i<filelist.size();i++)
					{
						netfilevector.add(filelist.get(i).fileName);
						for(int j=0;j<filelist.get(i).peers.size();j++)
						{
							//System.out.println(filelist.get(i).peers.get(j).hostname);
							int count=0;
							for(int c=0;c<hostvector.size();c++)
							{
								if(filelist.get(i).peers.get(j).hostname.equals(hostvector.get(c)))
									break;
								count++;
							}
							if(count==hostvector.size())
							{
							    hostvector.add(filelist.get(i).peers.get(j).hostname);
							}
							if(filelist.get(i).peers.get(j).hostname.equals(ln))
							{
								localfilevector.add(filelist.get(i).fileName);
							}
						}
					}
					localfilelist.setListData(localfilevector);
					hostlist.setListData(hostvector);
					netfilelist.setListData(netfilevector);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
     	   }
     	  });
        panel.add(updateLabel);
    }
    static class ServerThread implements Runnable{ 
    	public  String Send_Filepah;
    	//private Socket server; 
    	public ServerThread(String Send_Filepah){ 
    		this.Send_Filepah=Send_Filepah; 
    	} 
    	
    	public void run(){ 
    		try {
    			ServerSocket server=new ServerSocket(30001); 
    			while(true){ 
    				Server s=new Server(server.accept(),Send_Filepah); 
    				System.out.println("开始发送文件线程");
    				s.start(); 		
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    	}
    	
    }
}
