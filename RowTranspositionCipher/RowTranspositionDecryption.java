import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class RowTranspositionDecryption {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Input \"Encrypted Message C\", Press \"Enter\" to end the input:");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String cipherText= br.readLine();
		//String cipherText = onlyLetter(inputText);
		System.out.println("Input \"Word w\" use to decrypt, Press \"Enter\" to end the input:");
		
		br = new BufferedReader(new InputStreamReader(System.in));
		String inputText2= br.readLine();
		String word= onlyLetter(inputText2);
		
		int k= word.length();
		
		if(cipherText.length() % k !=0) {
			System.out.println("Cipher text is not correct, please check");
			return ;
		}
			
		int row= cipherText.length()/k;
		char[][] matrix= new char[row+1][k];
		
		//the first row save the alphabetical order of the letters in w
		for(int j=0; j< k; j++)
			matrix[0][j]= word.charAt(j);
			//matrix[0][j]= keyword[j];
		
		
		//construct the row transposition matrix;
		//divide the cipher text to by column length; 
		//and put them to the matrix
		
		char[] keyword= word.toCharArray();
		Arrays.sort(keyword);
		boolean[] visited= new boolean[keyword.length];
		
		int index=0;
		for(int i=0; i< keyword.length; i++) {
			for(int j= 0; j< matrix[0].length ; j++) {
				if(keyword[i]==matrix[0][j] && !visited[j]) {
					visited[j] = true;
					for(int c=1; c< matrix.length; c++)
						matrix[c][j]= cipherText.charAt(index++);
				}			
			}	
		}
		
		System.out.println();
		System.out.println("\"C\": "+cipherText);
		System.out.println("\"w\": "+word);
		
		System.out.println("The Transposition matrix is: ");
		for(int i=0; i< matrix.length; i++) {
			for(int j=0; j< matrix[0].length; j++) {
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
			if(i==0)
			System.out.println("-------------------");
		}
		
		StringBuilder plainText= new StringBuilder();
		
		for(int i=1; i< matrix.length; i++) {
			for(int j= 0; j< matrix[0].length; j++) {
				if(matrix[i][j] !='X')
					plainText.append(matrix[i][j]);
			}
		}
		
		System.out.println();
		System.out.println("\"M\": "+ plainText.toString());
	}
	
	private static String onlyLetter(String string) {
		StringBuilder res= new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if (Character.isLowerCase(string.charAt(i))
					|| Character.isUpperCase(string.charAt(i))) {
				res.append(string.charAt(i));
			} 
		}
		return res.toString().toLowerCase();
 
	}

}
