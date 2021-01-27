import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FindKeyofAES {

	public String KEY;//key
	
	public static final String IV  = "010203040506070809000a0b0c0d0e0f";//iv
	public static final String plainText = "This is a top secret.";
	public static final int AesKeySize= 16; 
	public static final String wordsFileName = "/words.txt";
	public static final String cipherFileName = "/ciphertext6.bin";
	
	public static boolean EncryptAndCompare(String content, String KEY, byte[] cipherBin) throws Exception {  

		byte[] raw = KEY.getBytes();  
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		//change HexString to Byte[] which size is 16 
		IvParameterSpec ips = new IvParameterSpec(parseHexStr2Byte(IV));
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);  
		byte[] encrypted = cipher.doFinal(content.getBytes());  
		
		System.out.println(parseByte2HexStr(cipherBin)+" : "+ parseByte2HexStr(encrypted));
		if(Arrays.equals(encrypted, cipherBin))
			return true;
		else
			return false;
	}  

	public static void main(String[] args) {
		
		File directory = new File("");
		String wordListPath = directory.getAbsolutePath()+ wordsFileName;
		String cipherBinPath = directory.getAbsolutePath()+ cipherFileName;
		
		//read the cipher bin file to byte array
		byte[] cipherBin = null;
		
		try {
			cipherBin = readFileToByteArray(cipherBinPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//open an input stream to read in the word list file 
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			String str = "";
			
			String testKey;
			
			fis = new FileInputStream(wordListPath);// FileInputStream
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			
			if(cipherBin.length !=0) {
				//read word from the word List
				while ((str = br.readLine()) != null) {
					StringBuilder test= new StringBuilder(str.trim());
					
					//padding the word size to 16, if it's longer than 16 we select the first 16 bytes
					int j= AesKeySize - test.length();
					if(j>=0) {
						for(int i=0; i< j; i++) {
							test.append("#");
						}
						testKey= test.toString();
					}
					else
						testKey= test.substring(0, AesKeySize);
					
					//brute force to find the key by selecting word from the word List
					try {
						if(EncryptAndCompare(plainText, testKey, cipherBin)) {
							System.out.println("The plain text is: "+ plainText);
							System.out.println("The initial vector is: "+ IV);
							System.out.println("The cipher text in HEX mode is :"+ parseByte2HexStr(cipherBin));
							System.out.println("The Key is: "+str +" by padding "+ j+" of # signs");
						    return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("The Key does not in the word list, check it again"); 
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can't find word file");
		} catch (IOException e) {
			System.out.println("Read failure");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//some help function like change HexStr to byte array
	public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
	
	//change byte array to HexString
	public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
	}
	
	//read Binary file to a byte[]
	public static byte[] readFileToByteArray(String fileName) throws IOException{
	    InputStream input = new FileInputStream(fileName);
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    try{
	        copy(input, output);
	        return output.toByteArray();
	    }finally{
	        input.close();
	    }
	}
	
	public static void copy(InputStream input, OutputStream output) throws IOException{
	    byte[] buf = new byte[4096];
	    int bytesRead = 0; 
	    while((bytesRead = input.read(buf))!=-1){
	        output.write(buf, 0, bytesRead);
	    }
	}
}
