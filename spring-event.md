## 어플리케이션 이벤트 사용하기 ##
http://javaslave.tistory.com/73

## 직접 구현하기 ##

```
package io.startup.event;

public interface Event {

  	void setSource(Object source);
	default Instant getTime()  {
		return Instant.now();
	}
}


public interface EventHandler<T> {

	void handle(T event);
	boolean canHandle(Event e);
}


public interface EventDispather {

	void raise(Event e);
	
	void addEventHandler(EventHandler<?> handler);

	
	class LogicOrderException extends RuntimeException {
		private static final long serialVersionUID = -357156289244948133L;

		public LogicOrderException() {
			super("call addEventHandler() before raise()");
		}
	}
	
	public static SimpleEventDispatcher simpleDispatcher( ) {
		return new SimpleEventDispatcher();
	}
	
	class SimpleEventDispatcher implements EventDispather {
		
		/*
		 * ThreadLocal 을 사용하여 쓰레드간에 List 공유를 방지하고 있다.  
		 */
		ThreadLocal<List<EventHandler<?>>> localEventHandlers = new ThreadLocal<>();
		ThreadLocal<Boolean> publishing = new ThreadLocal<Boolean>() {
			@Override 
			protected Boolean initialValue() {
		        return Boolean.FALSE;
		    }
		};
		
		@SuppressWarnings("unchecked")
		void processEvent(Event e) {
			List<EventHandler<?>> eventHandlers = localEventHandlers.get();
			
			for(@SuppressWarnings("rawtypes") EventHandler handler : eventHandlers)
				if( handler.canHandle(e) )	
					handler.handle(e);	

			/*
			eventHandlers.stream().forEach(handler -> {
			if( handler.canHandle(e) )	
				handler.handle(e);	
			});
			*/
		}

		@Override
		public void raise(Event e) {
			List<EventHandler<?>> eventHandlers = localEventHandlers.get();
			if(eventHandlers == null) {
				System.err.println("event handler for " + e.getClass().getName() + " doest not exist ..");
				return;
			}
			processEvent(e);
		}

		@Override
		public void addEventHandler(EventHandler<?> handler) {
			List<EventHandler<?>> eventHandlers = localEventHandlers.get();
			if(eventHandlers == null) {
				eventHandlers = new ArrayList<>();
				localEventHandlers.set(eventHandlers);
			}
			eventHandlers.add(handler);
		}
		
		public void reset() {
			localEventHandlers.remove();
		}
		
	}
	
}

```



```
package io.startup.order;

public class CancelOrderEvent implements Event {

	Object source;
	int orderId;
	
	
	public CancelOrderEvent(int orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public void setSource(Object source) {
		this.source = source;
	}
	
	public int getOrderId() {
		return this.orderId;
	}
	
}


public class Order {
	int orderId;
	String orderName;
	String status;

	public Order(int orderId, String orderName) {
		this.orderId = orderId;
		this.orderName = orderName;
	}
	
	public Order() {
		
	}
	
	public void cancel() {
		
		this.status = "cancel";
		System.out.println("주문번호 " + this.orderId + "취소됨");
	}
	
}



public class OrderService {

	OrderRepository orderRepository = new OrderRepository();
	RefundService refundService = new RefundService();
	SimpleEventDispatcher eventDispather = EventDispather.simpleDispatcher();
	
	public void cancelOrder(int orderId) {

		// 주문 취소
		Order order = orderRepository.findById(orderId);
		order.cancel();
		orderRepository.save(order);
		
		// 주문 취소 이벤트 발생
		//eventDispather.raise(new CancelOrderEvent(orderId));
		
		eventDispather.addEventHandler(refundService);
		eventDispather.raise(new CancelOrderEvent(orderId));
		//eventDispather.raise(new UnCatchedEvent(orderId));
		eventDispather.reset();
		
		/*
		try {
			refundService.refund(orderId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
}


public class RefundService implements EventHandler<CancelOrderEvent> {
	RefundRepository refundRepository = new RefundRepository();
	
	public void refund(int orderId) {
		
		try {
			// 환불로직.
			// 
			//
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(CancelOrderEvent event) {
		System.out.println(event.getOrderId() + " 환불됨.");
		refund(event.getOrderId());
	}

	@Override
	public boolean canHandle(Event e) {
		if(e == null)
			return false;
		
		if(!e.getClass().equals(CancelOrderEvent.class))
			return false;
		
		return true;
	}
	
}

```


