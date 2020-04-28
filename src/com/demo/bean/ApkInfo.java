package com.demo.bean;

import java.util.List;


@SuppressWarnings("unchecked")
public class ApkInfo {

	private String versionCode;
	private String versionName;
	private String apkPackage;
	private String minSdkVersion;
	private String apkName;
	private String qudaohao;
	private List<String> uses_permission;
	private String packageName;
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getQudaohao() {
		return qudaohao;
	}

	public void setQudaohao(String qudaohao) {
		this.qudaohao = qudaohao;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getApkPackage() {
		return apkPackage;
	}

	public void setApkPackage(String apkPackage) {
		this.apkPackage = apkPackage;
	}

	public String getMinSdkVersion() {
		return minSdkVersion;
	}

	public void setMinSdkVersion(String minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public List<String> getUses_permission() {
		return uses_permission;
	}

	public void setUses_permission(List<String> uses_permission) {
		this.uses_permission = uses_permission;
	}
	
	public static boolean checkName(String apkname,String banben,String qudao){
		apkname=apkname.substring(0,apkname.lastIndexOf("."));
		String[] namelist=apkname.split("_");
		if(namelist[1].equalsIgnoreCase(qudao)&&namelist[2].equalsIgnoreCase(banben)){
			return true;
		}else{
			return false;
		}
	}
	public String toString() {
            return  "文件名:"+apkName+"\r\n版本号"+versionCode+"\r\n版本名"+versionName+"\r\n包名:"+packageName+"\r\n渠道号为"+qudaohao+"\r\n"+"对比结果:"+checkName(apkName,versionName, qudaohao)+"\r\n\r\n";
	}
}
