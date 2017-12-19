package app;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class MyReceiver {
	
	public static void main(String[] args) {
		
		try {
			Properties jndiParameters = new Properties();
			jndiParameters.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			// jndiParameters.put(Context.PROVIDER_URL, "tcp://localhost:61616");
			InitialContext initialContext = new InitialContext(jndiParameters);
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) initialContext
					.lookup("ConnectionFactory");
			QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) initialContext.lookup("dynamicQueues/Q1");
			QueueReceiver queueReceiver = queueSession.createReceiver(queue);

			MyListener myListener = new MyListener();

			queueReceiver.setMessageListener(myListener);

			System.out.println("MyListener waiting...");
			
			while (true) {
				Thread.sleep(1000);
			}
			
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

}