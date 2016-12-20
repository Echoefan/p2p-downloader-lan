package peerp;

import java.io.Serializable;

public class peerp implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String hostname=null;
	public String peerIP=null;
	public peerp(String hostname,String peerIP)
	{
		this.hostname=hostname;
		this.peerIP=peerIP;
	}
}
