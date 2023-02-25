package y1s2_a4;

import java.io.*;

public class Tester 
{
	public static void main(String[] args) 
	{
		File f = new File("C:\\Users\\PC\\eclipse-workspace\\y1s2\\src\\y1s2_a4\\beatles.txt");
		int ch=0;
		String word="";
		
		try
		{
			FileReader reader = new FileReader(f.getAbsoluteFile());
			while (ch != -1 )
			{
				ch = reader.read();
				word += (char) ch;

				while (ch != 32 && ch != -1)
				{
					ch = reader.read();

					word += (char) ch;
				}
			}
			
			reader.close();

			for(int i=0; i< word.length(); i++)
			{
				if (word.charAt(i) >= 65  &&  word.charAt(i) <= 90)
				{
					char lower = (char)(  (int)word.charAt(i) + 32);
					word=word.replace(word.charAt(i), lower);
				}
				else if (!(word.charAt(i)>=97 && word.charAt(i)<=122) && word.charAt(i)!=' ' && word.charAt(i)!='\n')

					word=word.replace(Character.toString(word.charAt(i)), "");
			}

			
			
			
			FileWriter writer = new FileWriter(f.getAbsolutePath().replace(".txt", "Normalize.txt"));
			writer.write(word);
			
			
			writer.close();
					}
	catch (IOException e)
	{
	e.printStackTrace();	
	}
	finally
	{
	}
	}
}
	


