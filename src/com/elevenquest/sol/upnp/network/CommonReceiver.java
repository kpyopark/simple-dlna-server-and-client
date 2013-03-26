package com.elevenquest.sol.upnp.network;

import java.util.Vector;


public abstract class CommonReceiver {

	Vector<ICommonReceiveHandler> handlerList = new Vector<ICommonReceiveHandler>();

	public void addReceiveHandler(ICommonReceiveHandler handler) {
		this.handlerList.add(handler);
	}
	
	public void clearHandler() {
		this.handlerList.clear();
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
	public void process(HTTPRequest request) {
		for ( ICommonReceiveHandler handler : this.handlerList ) {
			handler.process(request);
		}
	}

}
