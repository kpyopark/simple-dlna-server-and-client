package com.elevenquest.sol.upnp.network;

public abstract class HttpReceiver {
	IHttpReceiveHandler handler = null;

	public void setReceiveHandler(IHttpReceiveHandler handler) {
		this.handler = handler;
	}
	
	public void clearHandler() {
		this.handler = null;
	}
	
	abstract public void initSocket();
	
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
	abstract public HttpBaseStructure listen() throws Exception;
	
	public void beforeReceive() {
		// TODO : API for hooking
	}
	
	public void afterReceive() {
		// TODO : API for hooking
	}
	
	public void receiveData() throws Exception {
		beforeReceive();
		HttpBaseStructure rtnValue = listen();
		process(rtnValue);
		afterReceive();
	}
	
	/**
	 * listen�� �ڷḦ ������ ó���ϴ� CommonHandler�� ȣ���Ѵ�.
	 * ���������� ��ϵ�(addReceiveHandler) Handler�� ������ ȣ���� �� �ִ�. 
	 * 
	 * @param packet
	 */
	public HttpBaseStructure process(HttpBaseStructure request) {
		if ( handler != null )
			return handler.process(request);
		else
			return null;
	}
	
	abstract public void close();

}
