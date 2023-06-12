package com.koreaIT.project.util;

import java.security.MessageDigest;

public class Util {
	public static String f(String format, Object... args) {
		return String.format(format, args);
	}
	
	
	//@ResponseBody 있고 리턴해야되고 페이지 다른걸로 바꿔줘야될 때
	public static String jsReplace(String msg, String uri) {
		if(msg == null) {
			msg = "";
		}
		if(uri == null) {
			uri="";
		}
		return Util.f("""
				<script>
					const msg = '%s'.trim();
					if(msg.length > 0) {
						alert(msg);
					}
					location.replace('%s');
				</script>
				""", msg, uri);
	}
	
	
	//@ResponseBody 있고 오류메세지 리턴하고 뒤로가기 할 때
	public static String jsHistoryBack(String msg) {
		if(msg == null) {
			msg = "";
		}
		return Util.f("""
				<script>
					const msg = '%s'.trim();
					if(msg.length > 0) {
						alert(msg);
					}
					history.back();
				</script>
				""", msg);
	}

	public static boolean empty(Object obj) {
		
		if (obj == null) {
			return true;
		}
		
		String str = (String)obj;
		
		return str.trim().length() == 0;
	}
	
	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch (Exception ex) {
			return "";
		}
	}


	public static String getTempPassword(int length) {
		int index = 0;
		char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			index = (int) (charArr.length * Math.random());
			sb.append(charArr[index]);
		}

		return sb.toString();
	}
	
	
}
