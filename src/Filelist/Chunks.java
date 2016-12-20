package Filelist;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Chunks {
	/** 
     * 文件分割 
     * @param src 源文件路径 
     * @param dest 目标文件路径 
     */  
    public static int split(String src, String dest){  
    	System.out.println("分割开始...");
        File srcFile = new File(src);//源文件  
          
        long srcSize = srcFile.length();//源文件的大小  
        long destSize = 1024*1024;//目标文件的大小（分割后每个文件的大小）  
          
        int number = (int)(srcSize/destSize);  
        number = srcSize%destSize==0?number:number+1;//分割后文件的数目  
          
        String fileName = src.substring(src.lastIndexOf("/"));//源文件名  
        InputStream in = null;//输入字节流  
        BufferedInputStream bis = null;//输入缓冲流  
        byte[] bytes = new byte[1024*1024];//每次读取文件的大小为1MB  
        int len = -1;//每次读取的长度值  
        try {  
            in = new FileInputStream(srcFile);  
            bis = new BufferedInputStream(in);  
            for(int i=0;i<number;i++){  
                  
                String destName = dest+File.separator+fileName+"-"+i;  
                OutputStream out = new FileOutputStream(destName);  
                BufferedOutputStream bos = new BufferedOutputStream(out);  
                int count = 0;  
                while((len = bis.read(bytes))!=-1){  
                    bos.write(bytes, 0, len);//把字节数据写入目标文件中  
                    count+=len;  
                    if(count>=destSize){  
                        break;  
                    }  
                }  
                bos.flush();//刷新  
                bos.close();  
                out.close();  
            } 
        	System.out.println("分割完成");
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            //关闭流  
            try {  
                if(bis!=null)bis.close();  
                if(in!=null)in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
		return number;  
    }  
      
    /** 
     * 文件合并 
     * 注意：在拼接文件路劲时，一定不要忘记文件的根路径，否则复制不成功 
     * @param dest 目标目录
     * @param src 源文件:src-i
     */  
    public static void merge(String src, String dest, int chunks){ 
    	System.out.println("开始合并...");
        //合并后的文件名  
        String name = src.substring(src.lastIndexOf("/"));  
        dest = dest+name;//合并后的文件路径  
          
        File destFile = new File(dest);//合并后的文件  
        OutputStream out = null;  
        BufferedOutputStream bos = null;  
        try {  
            out = new FileOutputStream(destFile);  
            bos = new BufferedOutputStream(out);  
            for (int i=0; i<chunks; i++) {  
                File srcFile = new File(src+"-"+i);  
                InputStream in = new FileInputStream(srcFile);  
                BufferedInputStream bis = new BufferedInputStream(in);  
                byte[] bytes = new byte[1024*1024];  
                int len = -1;  
                while((len = bis.read(bytes))!=-1){  
                    bos.write(bytes, 0, len);  
                }  
                bis.close();  
                in.close();  
                srcFile.delete();
            }  
            System.out.println("合并结束"); 
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            //关闭流  
            try {  
                if(bos!=null)bos.close();  
                if(out!=null)out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    public static void main(String[] args) {  
        /** 
         * 分割测试 
         */  
    	String src = "/home/rushzhou/Documents/test.mp4";//要分割的大文件  
    	String dest = "/home/rushzhou/Documents/split_right/";//文件分割后保存的路径    
    	int chunks_num = split(src, dest);  

    	
        /** 
         * 合并测试 
         */  
        //合并后文件的保存路径  
        String destPath = "/home/rushzhou/Documents/split_right/";  
        //要合并的文件路径  
        String srcPath = "/home/rushzhou/Documents/split_right/test.mp4";   
        merge(srcPath, destPath, chunks_num);  
        
    }  
}
