/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekat1;

import entity.Documentrequest;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSConsumer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.core.HttpHeaders;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author petar
 */

public class Main {
   private static HttpURLConnection con;
   public static final String TERM_URL= "http://collabnet.netset.rs:8081/is/terminCentar/checkTimeslotAvailability?regionalniCentarId=17538&termin=";
   public static final String STATUS_URL = "http://collabnet.netset.rs:8081/is/persoCentar/";
   
   @Resource(lookup="jms/__defaultConnectionFactory")
   public static ConnectionFactory konekcija;
    
  @Resource(lookup = "t")
   public static Topic topic;
  
   @Resource(lookup="m")
    public static Queue queue;
   
    public static boolean proveraTermina;
    public static boolean radiTajmer;
    
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("operatorPU");
    public static EntityManager em = emf.createEntityManager();
    
    public static void main(String[] args) {
        
        JMSContext kontekst = konekcija.createContext();
        JMSProducer proizvodjac = kontekst.createProducer();
        JMSConsumer consumer=kontekst.createConsumer(topic);
       
        projekat1.Forma forma = new projekat1.Forma();
       /* Documentrequest doc = new Documentrequest(1, "ozenjen","2", "1998-05-05", "Djoka", "Ruzica", "Aca", "0505998773630", "srbin", "Vracar", "muski", "Djokic", "Djokic", "Djokic", "student", "Kreiran", "Bulevar JNA");
       */
        try {
           
           while(true){
           if(radiTajmer){
           Message msg=consumer.receive();
           if(msg instanceof TextMessage){
                TextMessage txtmsg=(TextMessage)msg;
                int iden=forma.dohvatiKljuc();
                if(iden!=0) azuriraj(iden);
                System.out.println(txtmsg.getText());
           }
           }
           }//uruci(27);
           
       } catch (Exception ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    public static boolean proveriTermin() throws Exception{

        LocalDateTime ldt = LocalDateTime.now();
        String currTime = "";
        currTime += ldt.getYear()+ "-" +
                String.format("%02d", ldt.getMonthValue())+ "-" +
                String.format("%02d", ldt.getDayOfMonth()) + "T" +
                String.format("%02d", ldt.getHour()) + ":" +
                String.format("%02d", ldt.getMinute()) + ":" +
                String.format("%02d", ldt.getSecond());

        String slobodanTer = "2020-2-10T15:0:0";
        URL obj = new URL(TERM_URL + slobodanTer);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("GET Response code: " + responseCode);

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            
                JSONObject jobj;
            try {
                jobj = new JSONObject(response.toString());
                
                 if(jobj.getString("dostupnost").equals("true")){
                     in.close();
                     return true;
                 }
                 
            } catch (JSONException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        else System.out.println("GET zahtev greska");
        
        return false;
        
    }

    public static void azuriraj(int id) throws MalformedURLException, IOException{
        Documentrequest d = em.find(Documentrequest.class, id);
        String sstatus=d.getStatus();
        
        if(sstatus.equals("cekaUrucenje")||sstatus.equals("urucen")){
            System.out.println("Stanje zahteva je nepromenjeno");
            return;
        } else {
            
        String str = "17538"+String.format("%07d", id);
        URL obj = new URL(STATUS_URL + str);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", HttpHeaders.USER_AGENT);

        int responseCode = conn.getResponseCode();
        System.out.println("GET Response code: " + responseCode);

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            
            JSONObject jobj;
            
            try {
                jobj = new JSONObject(response.toString());
                
                System.out.println("Json objekat: " + jobj.toString());
                
                 if(jobj.getString("status").equals("proizveden")){
                     in.close();
                    //kreiranje objekta zahteva
                    Documentrequest dok = new Documentrequest(id, jobj.getString("bracnoStanje"),jobj.getString("brojPrebivalista"), jobj.getString("datumRodjenja"), jobj.getString("ime"), jobj.getString("imeMajke"), jobj.getString("imeOca"), jobj.getString("JMBG"), jobj.getString("nacionalnost"), jobj.getString("opstinaPrebivalista"), jobj.getString("pol"), jobj.getString("prezime"), jobj.getString("prezimeMajke"), jobj.getString("prezimeOca"), jobj.getString("profesija"), jobj.getString("status"), jobj.getString("ulicaPrebivalista"));
                    dok.setStatus("cekaUrucenje");
                    em.getTransaction().begin();
                    em.merge(dok);
                    em.flush();
                    em.getTransaction().commit();
                    System.out.println(dok.getStatus());
                    System.out.println("Status zahteva promenjen u bazi u "+dok.getStatus());
                     
                     
                     return;
                 }     
            } catch (JSONException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        else System.out.println("GET zahtev proveraStatusa greska");
        }
    }
   
    
    public static void uruci(int id){
        Documentrequest d = em.find(Documentrequest.class, id);
        if(d.getStatus().equals("urucen")||d.getStatus().equals("uProdukciji") ||d.getStatus().equals("Kreiran")){
            System.out.println("Dokument sa id "+id+" nije moguce uruciti!");
    }else {
        d.setStatus("urucen");
        em.getTransaction().begin();
        em.merge(d);
        em.flush();
        em.getTransaction().commit();
        System.out.println("Status zahteva sa id "+id+" je promenjen u urucen");
        System.out.println(d.getStatus());
        }
    }
    
    public static void kreirajZahtev(Documentrequest doc){
        JMSContext kontekst = konekcija.createContext();
        JMSProducer proizvodjac = kontekst.createProducer();
        em.getTransaction().begin();
        em.persist(doc);
        em.flush();
        em.getTransaction().commit();
                
                //slanje u red
                Message objMes=kontekst.createObjectMessage(doc);
                proizvodjac.send(Main.queue, objMes);
                System.out.println("Poslata je poruka");
                System.out.println("Upisao u bazu podatke sa ID:"+doc.getId());
    }
}
