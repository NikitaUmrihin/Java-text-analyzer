package y1s2_a4;

import java.io.IOException;

public class CleanFailException extends IOException
{
	public CleanFailException()
	{
		super();
	}
	
	public CleanFailException(String msg)
	{
		super(msg);
	}
}
