package kr.co.goodstream.lotus.admin;


public class BaseException extends Exception {


	private static final long serialVersionUID = -9048489224708328916L;

	public BaseException() {
		super();
	}

	public BaseException(String msg)  {
		super (msg);
	}

	public BaseException(Exception e)  {
		super(e);
	}
}
