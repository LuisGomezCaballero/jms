package app;

import javax.jms.*;

public class MyListener implements MessageListener {

	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Received: " + textMessage.getText());
		} catch (JMSException jmsException) {
			System.out.println(jmsException);
		}
	}
}