package com.pyg.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;



import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUtil {
	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public static ChannelSftp connect(String host, int port, String username,
			String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return sftp;
	}

	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 */
	public static void upload(String directory, InputStream in,String fileName,
			ChannelSftp sftp) {
		/*
		 * sftp.cd(directory); sftp.cd(dest); sftp.mkdir(dest); File file = new
		 * File(uploadFile); sftp.put(new FileInputStream(file),
		 * file.getName());
		 */
		mkDir(directory, sftp);
		//InputStream in = null;
		try {
			sftp.cd(directory);
			//File file = new File(uploadFile);
			//in = new FileInputStream(file);
			//sftp.put(new FileInputStream(file), file.getName());
			sftp.put(in, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param sftp
	 */
	public static InputStream download(String directory, String downloadFile,
			ChannelSftp sftp) {
		InputStream in = null;
		try {
			sftp.cd(directory);
			in = sftp.get(downloadFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return in;
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public static void delete(String directory, String deleteFile,
			ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public static Vector listFiles(String directory, ChannelSftp sftp)
			throws SftpException {
		return sftp.ls(directory);
	}

	/**
	 * 获取目录name
	 * 
	 * @param Vector
	 *            目录集合
	 * @return
	 * @throws SftpException
	 */
	public static List<String> buildFiles(Vector ls) throws Exception {
		if (ls != null && ls.size() >= 0) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ls.size(); i++) {
				LsEntry f = (LsEntry) ls.get(i);
				String nm = f.getFilename();
				if (nm.equals(".") || nm.equals(".."))
					continue;
				list.add(nm);
			}
			return list;
		}
		return null;
	}

	/**
	 * 打开指定目录
	 * 
	 * @param directory
	 *            directory
	 * @return 是否打开目录
	 */
	public static boolean openDir(String directory, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

	/**
	 * 创建指定文件夹
	 * 
	 * @param dirName
	 *            dirName
	 */
	public static void mkDir(String dirName, ChannelSftp sftp) {
		String[] dirs = dirName.split("/");

		try {
			String now = sftp.pwd();
			sftp.cd("/");
			for (int i = 0; i < dirs.length; i++) {
				if (StringUtils.isNotEmpty(dirs[i])) {
					boolean dirExists = openDir(dirs[i], sftp);
					if (!dirExists) {
						sftp.mkdir(dirs[i]);
						sftp.cd(dirs[i]);
					}
				}
			}
			sftp.cd(now);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
}
