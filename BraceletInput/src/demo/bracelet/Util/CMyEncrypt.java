package demo.bracelet.Util;

import java.security.MessageDigest;

public class CMyEncrypt {
	private final String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}; 
	private String key = "growth"; 
	private String[] chars = new String[] { "a" , "b" , "c" , "d" , "e" , "f" , "g" , "h" ,
            "i" , "j" , "k" , "l" , "m" , "n" , "o" , "p" , "q" , "r" , "s" , "t" ,
            "u" , "v" , "w" , "x" , "y" , "z" , "0" , "1" , "2" , "3" , "4" , "5" ,
            "6" , "7" , "8" , "9" , "A" , "B" , "C" , "D" , "E" , "F" , "G" , "H" ,
            "I" , "J" , "K" , "L" , "M" , "N" , "O" , "P" , "Q" , "R" , "S" , "T" ,
            "U" , "V" , "W" , "X" , "Y" , "Z"
     };
	public String TheCaptcha(String id) {  // ����6λ��֤�����������
		String hex = md5(key + id);;
		String result = "";
		// �Ѽ����ַ����� 8 λһ�� 16 ������ 0x3FFFFFFF ����λ������
       String sTempSubString = hex.substring(0, 8);
       // ������Ҫʹ�� long ����ת������Ϊ Inteper .parseInt() ֻ�ܴ��� 31 λ , ��λΪ����λ , ������� long �����Խ��
       long lHexLong = 0x3FFFFFFF & Long.parseLong (sTempSubString, 16);
       for ( int j = 0; j < 6; j++) {
          // �ѵõ���ֵ�� 0x0000003D ����λ�����㣬ȡ���ַ����� chars ����
          long index = 0x0000003D & lHexLong;
          result += chars[( int ) index];	// ��ȡ�õ��ַ����
          lHexLong = lHexLong >> 5;
       }
       return result;
	} 
	private String md5(String id) {
		return encodeByMD5(id);
	}
	 /**���ַ�������MD5����*/ 
    private String encodeByMD5(String originString){ 
        if (originString!=null) { 
            try { 
                //��������ָ���㷨���Ƶ���ϢժҪ 
                MessageDigest md5 = MessageDigest.getInstance("MD5"); 
                //ʹ��ָ�����ֽ������ժҪ���������£�Ȼ�����ժҪ���� 
                byte[] results = md5.digest(originString.getBytes()); 
                //���õ����ֽ��������ַ�������  
                String result = byteArrayToHexString(results); 
                return result; 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
        } 
        return null; 
    } 
    
    /** 
     * �ֻ��ֽ�����Ϊʮ�������ַ��� 
     * @param b �ֽ����� 
     * @return ʮ�������ַ��� 
     */ 
     private String byteArrayToHexString(byte[] b){ 
         StringBuffer resultSb = new StringBuffer(); 
         for(int i=0;i<b.length;i++){ 
             resultSb.append(byteToHexString(b[i])); 
         } 
         return resultSb.toString(); 
     } 
   //��һ���ֽ�ת����ʮ��������ʽ���ַ��� 
     private String byteToHexString(byte b){ 
         int n = b; 
         if(n<0) 
         n=256+n; 
         int d1 = n/16; 
         int d2 = n%16; 
         return hexDigits[d1] + hexDigits[d2]; 
     } 
     
}
