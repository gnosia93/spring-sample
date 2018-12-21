## 어플리케이션 이벤트 사용하기 ##
http://javaslave.tistory.com/73

## 직접 구현하기 ##

아래는 동기화 방식의 이벤트 처리 시스템을 구현한 코드로, 동일한 쓰레드에서 처리되는 것을 가정하여 작성한 코드이다.

비동기 방식으로 구현하게 되는 경우 이벤트 발행자와 구독자가 서로 다른 쓰레드 이므로, 트랜잭션 처리가 불가능하다.

Order Cancel ---> Refund 로 연결되는 구조로, Order 와 Refund(환불)은 동일한 패키지로 묶인다는 가정하에 작성된 내용이다.

만약 주문시스템과 환불시스템이 물리적으로 분리되어 있고, 코드 Base 또는 별도로 구현되어 있다면

메시지 큐 방식의 비동기 또는 DB를 이용한 forwarder / api 방식으로 구현해야 할 것으로 생각된다.


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
		
		
		//
		//  type checking 하도록 하면 하면 에러가 발생하는데 왜 그런지 모르겠다. 
		//
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
		
		// ThradLocal 에 있는 객체를 지운다.
		// 쓰레드시 풀로 반환되기전에 호출한다. 스프링에서는 즈로 @Service 객체의 함수호출 후 리턴 바로 직전.
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
		
		
		// 주문 취소 이벤트 발생  --> 환불처리
		eventDispather.addEventHandler(refundService);
		eventDispather.raise(new CancelOrderEvent(orderId));
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


