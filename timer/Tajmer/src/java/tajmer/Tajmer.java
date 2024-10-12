/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tajmer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author Petar
 */
public class Tajmer {
Timer timer;
@Resource(lookup = "jms/__defaultConnectionFactory")
public static ConnectionFactory conn;
        
@Resource(lookup = "t")
public static Topic topic;
  
	public static void main(String[] args) {
            while(true){
                try {
                    System.out.println("Tajmer pokrenut sacekajte 40s.");
                    new Tajmer(40);
                    Thread.sleep(40000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tajmer.class.getName()).log(Level.SEVERE, null, ex);
                }
	}
        }
 
	public Tajmer(int seconds) {
		Timer timer = new Timer();
		//Scheduling NextTask() call in 40 second. 
		timer.schedule(new NextTask(), seconds * 1000);		
	}
 
	class NextTask extends TimerTask {
		@Override
		public void run() {
                JMSContext context=conn.createContext();
                JMSProducer proizvodjac=context.createProducer();
		TextMessage txtMsg=context.createTextMessage("Primljena poruka od tajmera");
                proizvodjac.send(topic, txtMsg);
                System.out.println("Timer poslao poruku");
		}
	}
}
