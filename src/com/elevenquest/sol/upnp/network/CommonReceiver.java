package com.elevenquest.sol.upnp.network;



public abstract class CommonReceiver {

	ICommonReceiveHandler handler = null;

	public void setReceiveHandler(ICommonReceiveHandler handler) {
		this.handler = handler;
	}
	
	public void clearHandler() {
		this.handler = null;
	}
	
	/**
	 * ������ network layer�� �ִ� �ڷḦ �޴� ��Ȱ�� �ϴ� �޽��.
	 * ServerSocket�� ���, accept�� ȣ���ϴ� ��Ȱ.
	 * ����Ÿ ��Ʈ���� ���� ����, process �Լ��� ȣ���� �־�� �Ѵ�.
	 * 
	 * ���� : �ش� �޽��� Blocking methods�� �����Ѵ�. (Sync ���)
	 *       ����, Blocking�� ���� �ʴ� ���, �ش� Listener�� ���������� �ݺ��Ǿ�
	 *       ���� ���ϰ� �뷮���� �߻�ȴ�.
	 * 
	 * @throws Exception
	 */
	abstract protected HTTPRequest listen() throws Exception;
	
	public void beforeReceive() {
		// TODO : API for hooking
	}
	
	public void afterReceive() {
		// TODO : API for hooking
	}
	
	public void receiveData() throws Exception {
		beforeReceive();
		HTTPRequest rtnValue = listen();
		process(rtnValue);
		afterReceive();
	}
	
	public void clear() {
		clearHandler();
	}
	
	/**
	 * listen�� �ڷḦ ������ ó���ϴ� CommonHandler�� ȣ���Ѵ�.
	 * ���������� ��ϵ�(addReceiveHandler) Handler�� ������ ȣ���� �� �ִ�. 
	 * 
	 * @param packet
	 */
	public HTTPResponse process(HTTPRequest request) {
		return handler.process(request);
	}

}
