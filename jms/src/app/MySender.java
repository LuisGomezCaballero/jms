package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class MySender {
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
			QueueSender queueSender = queueSession.createSender(queue);
			TextMessage textMessage = queueSession.createTextMessage();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println("Send message (empty line to exit):");
				String line = bufferedReader.readLine();
				
				if (line.equals("")) {
					break;
				}
				
				textMessage.setText(line);
				queueSender.send(textMessage);
			}
			
			queueConnection.close();
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}
}