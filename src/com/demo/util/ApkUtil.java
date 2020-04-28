package com.demo.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.demo.bean.ApkInfo;

public class ApkUtil {
	//命名空间
	private static final Namespace NS = Namespace.getNamespace("http://schemas.android.com/apk/res/android");
	
	private static final String FileEndFix=".txt";
	
	public static void main(String[] args){
		String path="G:\\apkapk\\";//记得在最后加 “ / ”
		while(true){
		System.out.println("输入apk的根目录。。。。");
		Scanner scanner=new Scanner(System.in);
		path=scanner.next();
		File file= new File(path);
		if(!file.isDirectory()){
			System.err.println("不是有效路径，请重新选择");
			System.out.println("\n");
			continue;
		}else{
			File[] tempList = file.listFiles();
			System.out.println("开始读取。。。");
			String outString="";
			Integer apkcount=0;
			for (int i = 0; i < tempList.length; i++) {
				if(isApk(tempList[i].getName())){
					ApkInfo info=getApkInfo(path+tempList[i].getName());
					info.setApkName(tempList[i].getName());
					outString+=info.toString();
					apkcount++;
				}
			}
			if(apkcount<=0){
				System.err.println("未发现apk文件");
				continue;
			}
			InputStream inputStream= new ByteArrayInputStream(outString.getBytes());
			String filename=String.valueOf(new Date().getTime());
			try {
				SaveFileFromInputStream(inputStream,path,filename+FileEndFix);
				System.out.println("读取完成。。。");
				System.err.println("文件名为:"+filename+FileEndFix);
			} catch (IOException e) {
				System.out.println("文件写入失败");
				break;
			}
			break;
		}
		}
		
	}
	@SuppressWarnings("unchecked")
	public static ApkInfo getApkInfo(String apkPath){
		ApkInfo apkInfo = new ApkInfo();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try{
			document = builder.build(getXmlInputStream(apkPath));
		}catch (Exception e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();//manifest
		apkInfo.setVersionCode(root.getAttributeValue("versionCode",NS));
		apkInfo.setVersionName(root.getAttributeValue("versionName", NS));
		apkInfo.setApkPackage(root.getAttributeValue("package", NS));
		Element appElement=root.getChild("application");
		List<Element> list=appElement.getChildren("meta-data");
		String result="";//渠道�?
		for(Element ele:list){
			if(ele.getAttributeValue("name",NS).equals("UMENG_CHANNEL")){
				result=ele.getAttributeValue("value",NS);
			}
		}
		String s = root.getAttributes().toString();
		String c[] = s.split(",");
		String versionCode = null;
		String versionName = null;
		String packageName = null;
		for(String a: c){
			if(a.contains("versionCode")){
				versionCode = a.substring(a.indexOf("versionCode=\"")+13, a.lastIndexOf("\""));
			}
			if(a.contains("versionName")){
				versionName = a.substring(a.indexOf("versionName=\"")+13, a.lastIndexOf("\""));
			}
			if(a.contains("package")){
				packageName = a.substring(a.indexOf("package=\"")+9, a.lastIndexOf("\""));
			}
		}
		apkInfo.setVersionCode(versionCode);
		apkInfo.setVersionName(versionName);
		apkInfo.setPackageName(packageName);
		apkInfo.setQudaohao(result);
//		String str = "\r\n版本号"+versionCode+"\r\n版本名"+versionName+"\r\n包名:"+packageName+"\r\n渠道号为"+result+"\r\n\r\n";
		return apkInfo;
	}

	private static InputStream getXmlInputStream(String apkPath) {
		InputStream inputStream = null;
		InputStream xmlInputStream = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(apkPath);
			ZipEntry zipEntry = new ZipEntry("AndroidManifest.xml");
			inputStream = zipFile.getInputStream(zipEntry);
			AXMLPrinter xmlPrinter = new AXMLPrinter();
			xmlPrinter.startPrinf(inputStream);
			xmlInputStream = new ByteArrayInputStream(xmlPrinter.getBuf().toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				inputStream.close();
				zipFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return xmlInputStream;
	}
	private static boolean isApk(String name){
		String tempString=name.substring(name.lastIndexOf(".")+1, name.length());
		if(tempString.equalsIgnoreCase("apk")){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 传入文件的输入流和文件路径以及文件的文件名进行保存文件的方法
	 * 
	 * @param stream
	 *            文件的字节流
	 * @param path
	 *            文件上传的路径
	 * @param filename
	 *            文件名
	 * @throws IOException
	 *             可能抛出io异常
	 */
	public static void SaveFileFromInputStream(InputStream stream, String path,
			String filename) throws IOException {
		//通过path路径实例化File对象
		File file= new File(path);
		//如果文件夹没有存在则创建文件夹
		if(!file.exists()){
			//可以创建多级子目录
			file.mkdirs();
		}
		// 实例化一个文件字节输出流，通过路径加上文件名
		FileOutputStream fs = new FileOutputStream(path + "/" + filename);
		// 实例化字节数组，用于读写文件
		byte[] buffer = new byte[1024 * 1024];
		//已读自己变量
		int byteread = 0;
		// 进行文件读写操作
		while ((byteread = stream.read(buffer)) != -1) {
			fs.write(buffer, 0, byteread);
			//刷新文件流
			fs.flush();
		}
		// 流对象关闭操作
		fs.close();
		stream.close();
	}


}
