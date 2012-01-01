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
	 * 실제로 network layer에 있는 자료를 받는 역활을 하는 메쏘드.
	 * ServerSocket의 경우, accept를 호출하는 역활.
	 * 데이타 스트림을 받은 이후, process 함수를 호출해 주어야 한다.
	 * 
	 * 주의 : 해당 메쏘드는 Blocking methods로 간주한다. (Sync 방식)
	 *       만약, Blocking이 되지 않는 경우, 해당 Listener가 지속적으로 반복되어
	 *       서버 부하가 대량으로 발생된다.
	 * 
	 * @throws Exception
	 */
	abstract protected Object listen() throws Exception;
	
	public void beforeReceive() {
		// TODO : API for hooking
	}
	
	public void afterReceive() {
		// TODO : API for hooking
	}
	
	public void receiveData() throws Exception {
		beforeReceive();
		Object rtnValue = listen();
		process(rtnValue);
		afterReceive();
	}
	
	public void clear() {
		clearHandler();
	}
	
	/**
	 * listen한 자료를 실제로 처리하는 CommonHandler를 호출한다.
	 * 내부적으로 등록된(addReceiveHandler) Handler를 여러번 호출할 수 있다. 
	 * 
	 * @param packet
	 */
	public void process(Object packet) {
		for ( ICommonReceiveHandler handler : this.handlerList ) {
			handler.process(packet);
		}
	}

}
