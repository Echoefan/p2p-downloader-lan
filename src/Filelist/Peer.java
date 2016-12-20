package Filelist;

public class Peer 
{
	public String hostname;
    public String host_ip;
    public String port;
    
public Peer()
{
		hostname="";
		host_ip="";
		port="";
}
public Peer(String host_ip,String hostname, String port)
{
	this.hostname=hostname;
	this.host_ip=host_ip;
	this.port=port;
}	
}
