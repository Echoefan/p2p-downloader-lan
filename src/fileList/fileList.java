package fileList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import peerp.peerp;


public class fileList implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String fileName=null;
	public List<peerp> peers=new ArrayList<>();
	public fileList(String f,peerp p)
	{
		this.fileName=f;
		this.peers.add(p);
	}
	
	
	
}
