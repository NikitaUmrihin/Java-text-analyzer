package y1s2_a4;

import java.io.IOException;

public class GenerationFailException extends IOException
{
	public GenerationFailException()
	{
		super();
	}
	
	public GenerationFailException(String msg)
	{
		super(msg);
	}
}
