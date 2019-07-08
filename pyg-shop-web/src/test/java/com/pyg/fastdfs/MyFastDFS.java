package com.pyg.fastdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.pyg.utils.FastDFSClient;

public class MyFastDFS {
	
	/**
	 * 需求: 测试分布式文件系统fastdfs上传操作
	 * @throws Exception 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testUploadPic() throws Exception{
		
		//指定客户端配置文件绝对路径
		String conf = "E:\\hubin\\javaee297\\pyg-shop-web\\src\\main\\resources\\config\\client.conf";		
		//指定上传的图片地址
		String pic = "E:\\image\\Penguins.jpg";
		//读取客户端配置文件,连接远程图片服务器
		ClientGlobal.init(conf);
		
		//创建tracker服务客户端对象
		TrackerClient tc = new TrackerClient();
		//从客户端对象中获取tracker服务对象
		TrackerServer trackerServer = tc.getConnection();
		
		StorageServer storageServer=null;
		//创建storage客户端服务对象
		StorageClient sc = new StorageClient(trackerServer, storageServer);
		
		//直接上传图片
		String[] urls = sc.upload_file(pic, "jpg", null);
		
		//group1  组名
		//M00/00/02/ 虚拟磁盘路径
		//wKhCQ1qvJp2ASX9dAAvea_OGt2M277.jpg 文件名

		
		//打印
		for (String url : urls) {
			System.out.println(url);
			
		}
		
		
		
	}
	
	/**
	 * 需求: 测试使用工具类进行分布式文件系统fastdfs上传操作
	 * @throws Exception 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testUploadPicUtils() throws Exception{
		
		//指定客户端配置文件绝对路径
		String conf = "E:\\hubin\\javaee297\\pyg-shop-web\\src\\main\\resources\\config\\client.conf";		
		//指定上传的图片地址
		String pic = "E:\\image\\Chrysanthemum.jpg";
		
		//使用工具类进行上传
		//创建工具类对象
		FastDFSClient fds = new FastDFSClient(conf);		
		//上传
		String url = fds.uploadFile(pic);
		
		System.out.println(url);
		
		
	}


}
