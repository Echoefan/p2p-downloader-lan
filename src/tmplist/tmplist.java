package tmplist;

import java.io.Serializable;
import java.util.List;

import fileList.fileList;
public class tmplist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<fileList> list;
	public List<fileList> getlist()
	{
		return list;
	}
	public void setList(List<fileList> list)
	{
		this.list=list;
	}

}
