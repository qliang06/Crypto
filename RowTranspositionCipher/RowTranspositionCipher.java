import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class RowTranspositionCipher {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Input \"Message M\", Press Enter to end the input:");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inputText= br.readLine();
		String plainText = onlyLetter(inputText);
	
		System.out.println("Input \"Word w\" use to encrypt, Press Enter to end the input:");
		
		br = new BufferedReader(new InputStreamReader(System.in));
		String inputText2= br.readLine();
		String word= onlyLetter(inputText2);
		
		int k= word.length();
		int row= plainText.length() % k==0? (plainText.length()/k):(plainText.length()/k)+1;
		char[][] matrix= new char[row+1][k];
		
		//the first row save the word as the index to output the column
		for(int j=0; j< k; j++)
			matrix[0][j]= word.charAt(j);
		
		
		//construct the row transposition matrix;
		int index=0;
		
		for(int i= 1; i< matrix.length; i++)
			for(int j=0; j< matrix[0].length; j++) 
			{
				if(index >= plainText.length())
					matrix[i][j]= 'X';
				else {
					matrix[i][j]= plainText.charAt(index);
				}
				index++;
			}
		
		System.out.println();
		System.out.println("\"M\": "+plainText);
		System.out.println("\"w\": "+word);
		System.out.println("Word w lengh of k is: "+ word.length());
		System.out.println();
		
		System.out.println("The Transposition matrix is: ");
		for(int i=0; i< matrix.length; i++) {
			for(int j=0; j< matrix[0].length; j++) {
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
			if(i==0)
			System.out.println("-------------------");
		}
		
		System.out.println();
		
		//we can now begin to output the cipher text:
		char[] keyword= word.toCharArray();
		Arrays.sort(keyword);
		boolean[] visited= new boolean[keyword.length];
		StringBuilder cipherText= new StringBuilder();
		
		for(int i=0; i< keyword.length; i++) {
			for(int c= 0; c< k; c++) {
				if(matrix[0][c]==keyword[i] && !visited[c]) {
					visited[c]= true;
					for(int r= 1; r< matrix.length; r++)
						cipherText.append(matrix[r][c]);
				}
			}
		}
		
		System.out.println("\"C\": "+ cipherText.toString());
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
