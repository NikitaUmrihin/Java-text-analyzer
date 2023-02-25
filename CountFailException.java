package y1s2_a4;

import java.io.IOException;

public class CountFailException extends IOException
{
		public CountFailException()
		{
			super();
		}
		
		CountFailException(String msg)
		{
			super(msg);
		}
	}


