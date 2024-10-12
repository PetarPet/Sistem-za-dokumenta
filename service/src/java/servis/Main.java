/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servis;

import entity.Documentrequest;
import java.io.OutputStreamWriter;
import static java.lang.System.in;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.json.JSONObject;

/**
 *
 * @author Petar
 */
public class Main {
    @Resource(lookup="jms/__defaultConnectionFactory")
    static ConnectionFactory konekcija;
    
    @Resource(lookup="m")
    static Queue queue;
    
    public static final String ZAH_URL = "http://collabnet.netset.rs:8081/is/persoCentar/submit";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            // TODO code application logic here
            JMSContext kontekst=konekcija.createContext();
            JMSConsumer potrosac = kontekst.createConsumer(queue);
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("operatorPU");
            EntityManager em = emf.createEntityManager();
            
            while(true){
            try {
            ObjectMessage poruka = kontekst.createObjectMessage();
            poruka=(ObjectMessage) potrosac.receive();
            if(poruka==null) continue;
            
            Documentrequest obj=(Documentrequest) poruka.getObject();
            obj.setStatus("uProdukciji");
            
            boolean rezultat= slanjeZahteva(obj);
            
            if(rezultat){
                //tek nakon uspesnog slanja na perso
                em.getTransaction().begin();
                em.merge(obj);
                em.flush();
                em.getTransaction().commit();
                System.out.println("POST uspesan i poslato u perso");
            } else {
                JMSProducer proizvodjac = kontekst.createProducer();
                Message objMes = kontekst.createObjectMessage(obj);
                proizvodjac.send(queue, objMes);
                System.out.println("Objekat vracen u queue jer je POST neuspesan");
            }
            
         Thread.sleep(5000);
         
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
    private static boolean slanjeZahteva(Documentrequest podaci) throws Exception{
        
        JSONObject jobj = new JSONObject();
        jobj.put("JMBG",podaci.getJmbg());
        jobj.put("id","17538"+String.format("%07d", podaci.getId()));
        jobj.put("ime",podaci.getIme());
        jobj.put("prezime",podaci.getPrezime());
        jobj.put("imeMajke",podaci.getImeMajke());
        jobj.put("imeOca",podaci.getImeOca());
        jobj.put("prezimeMajke",podaci.getPrezimeMajke());
        jobj.put("prezimeOca",podaci.getPrezimeOca());
        jobj.put("pol",podaci.getPol());
        jobj.put("datumRodjenja",podaci.getDatumRodjenja());
        jobj.put("nacionalnost",podaci.getNacionalnost());
        jobj.put("profesija",podaci.getProfesija());
        jobj.put("bracnoStanje",podaci.getBracnoStanje());
        jobj.put("opstinaPrebivalista",podaci.getOpstinaPrebivalista());
        jobj.put("ulicaPrebivalista", podaci.getUlicaPrebivalista());
        jobj.put("brojPrebivalista", podaci.getBrojPrebivalista());
        System.out.println(jobj);
        
        
        URL obj = new URL(ZAH_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(jobj.toString());
        wr.flush();
        wr.close();
     
        int responseCode = con.getResponseCode();
        System.out.println("POST Response code: " + responseCode);
        
        if(responseCode == 200 || responseCode == 201){
            System.out.println("HTTP POST uspesan");
            return true;
        }
        else  System.out.println("HTTP POST zahtev nije uspeo");
        return false;
    }
}
