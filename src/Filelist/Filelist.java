package Filelist;

import java.util.List;

public class Filelist 
{
public String filename;
public List<Peer> peers;
public Filelist(String filename,List<Peer> peers)
{
	this.filename=filename;
	this.peers=peers;
	
}
public Filelist()
 {
	this.filename="";
	this.peers=null;
 }
}



 