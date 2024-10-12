/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekat1;
import entity.Documentrequest;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Petar
 */
public class Forma extends JFrame {
    
    public JTextField JMBG;
    public JTextField ime;
    public JTextField prezime;
    public JTextField imeM;
    public JTextField prezM;
    public JTextField imeO;
    public JTextField prezO;
    public JTextField datumR;
    public JTextField opstina;
    public JTextField pol;
    public JTextField ulica;
    public JTextField broj;
    public JTextField nacionalnost;
    public JTextField prof;
    public JTextField bracnoSt;
    
    
    public JFormattedTextField id;
    NumberFormat integerFieldFormatter;
    public JFormattedTextField idUrucen;
    public JTextField status;
    
    public JLabel terminStatus;
    public JLabel kreirajStatus;
    public JLabel proveraStatus;
    
    public JButton dugme1;
    public JButton dugme2;
    public JButton dugme3;
    public JButton dugme4;
    public JButton dugme5;
    public JButton dugme6;
  
    public Forma(){
        super("Podnosenje zahteva");
        super.setBounds(300,100,600,500);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dodajKomponente();
        super.setVisible(true);
        
        dugme1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Main.proveraTermina=Main.proveriTermin();
                    if(Main.proveraTermina == true){
                       terminStatus.setText("Termin je dostupan"); 
                    } else {
                       terminStatus.setText("Termin nije dostupan"); 
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Forma.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        dugme2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Main.proveraTermina==true){
                    if(proveraPolja()){
                    Documentrequest doc = new Documentrequest(1, bracnoSt.getText(),broj.getText(), datumR.getText(), ime.getText(), imeM.getText(), imeO.getText(), JMBG.getText(), nacionalnost.getText(), opstina.getText(), pol.getText(), prezime.getText(), prezM.getText(), prezO.getText(), prof.getText(), "Kreiran", ulica.getText());
                    Main.kreirajZahtev(doc);
                    System.out.println("Zahtev je kreiran");
                } else System.out.println("Nisu popunjena sva polja");
                }
                else System.out.println("Termin nije dostupan");
            }
        });
        
        dugme3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                  int i = Integer.parseInt(id.getText());
                  Main.azuriraj(i);
                } catch (IOException ex) {
                    Logger.getLogger(Forma.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        dugme4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = Integer.parseInt(idUrucen.getText());
                Main.uruci(i);
            }
        });
        
        dugme5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 isprazniPolja();
            }
        });
        
        dugme6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Main.radiTajmer==false){
                    Main.radiTajmer=true;
                    System.out.println("Tajmer je ukljucen.");
                } else {
                    Main.radiTajmer=false;
                    System.out.println("Tajmer je iskljucen.");
                }
            }
        });
        
        
    }
    
    private void dodajKomponente(){
        JPanel panel = new JPanel(new BorderLayout());
        add(panel,"Center");
        
        JPanel p = new JPanel(new GridLayout(15,2));
        panel.add(p, "Center");
        
        p.add(new JLabel("JMBG: "));
        p.add( JMBG = new JTextField(""));
        p.add(new JLabel("Ime: "));
        p.add( ime = new JTextField(""));
        p.add(new JLabel("Prezime: "));
        p.add( prezime = new JTextField(""));
        p.add(new JLabel("Datum rodjenja: "));
        p.add( datumR = new JTextField(""));
        p.add(new JLabel("Ime majke: "));
        p.add( imeM= new JTextField(""));
        p.add(new JLabel("Prezime majke: "));
        p.add( prezM = new JTextField(""));
        p.add(new JLabel("Ime oca: "));
        p.add( imeO = new JTextField(""));
        p.add(new JLabel("Prezime oca: "));
        p.add( prezO = new JTextField(""));
        p.add(new JLabel("Opstina: "));
        p.add( opstina = new JTextField(""));
        p.add(new JLabel("Ulica: "));
        p.add( ulica = new JTextField(""));
        p.add(new JLabel("Broj: "));
        p.add( broj = new JTextField(""));
        p.add(new JLabel("Pol: "));
        p.add( pol = new JTextField(""));
        p.add(new JLabel("Nacionalnost: "));
        p.add( nacionalnost = new JTextField(""));
        p.add(new JLabel("Bracno stanje: "));
        p.add( bracnoSt = new JTextField(""));
        p.add(new JLabel("Profesija: "));
        p.add( prof = new JTextField(""));
        
        p = new JPanel(new GridLayout(6,3));
        panel.add(p, "South");
        
        p.add(dugme1 = new JButton("Proveri termin"));
        p.add(terminStatus = new JLabel(""));
        p.add(dugme6 = new JButton("Upali tajmer/Ugasi tajmer"));
        
        p.add(dugme2 = new JButton("Kreiraj zahtev"));
        p.add(kreirajStatus = new JLabel(""));
        p.add(dugme5 = new JButton("Nova forma"));
        
        p.add(new JLabel(""));
        p.add(new JLabel("Unesite ID zahteva za proveru"));
        p.add(new JLabel(""));
        
        p.add(dugme3 = new JButton("Proveri status"));
        
        integerFieldFormatter = NumberFormat.getIntegerInstance();
        integerFieldFormatter.setMaximumFractionDigits(0);
        
        
        p.add(id= new JFormattedTextField(integerFieldFormatter));
        
        p.add(proveraStatus = new JLabel(""));
        
        p.add(new JLabel(""));
        p.add(new JLabel("Unesite ID zahteva za urucenje"));
        p.add(new JLabel(""));
        
        p.add(dugme4 = new JButton("Uruci"));
        
        integerFieldFormatter = NumberFormat.getIntegerInstance();
        integerFieldFormatter.setMaximumFractionDigits(0);
        p.add(idUrucen= new JFormattedTextField(integerFieldFormatter));
        p.add(new JLabel(""));
    }

    public JTextField getJMBG() {
        return JMBG;
    }

    public JTextField getIme() {
        return ime;
    }

    public JTextField getPrezime() {
        return prezime;
    }

    public JTextField getImeM() {
        return imeM;
    }

    public JTextField getPrezM() {
        return prezM;
    }

    public JTextField getImeO() {
        return imeO;
    }

    public JTextField getPrezO() {
        return prezO;
    }

    public JTextField getDatumR() {
        return datumR;
    }

    public JTextField getOpstina() {
        return opstina;
    }

    public JTextField getPol() {
        return pol;
    }

    public JTextField getUlica() {
        return ulica;
    }

    public JTextField getBroj() {
        return broj;
    }

    public JTextField getNacionalnost() {
        return nacionalnost;
    }

    public JTextField getProf() {
        return prof;
    }

    public JTextField getBracnoSt() {
        return bracnoSt;
    }

    public JTextField getId() {
        return id;
    }

    public JTextField getStatus() {
        return status;
    }

    public JLabel getTerminStatus() {
        return terminStatus;
    }

    public JLabel getKreirajStatus() {
        return kreirajStatus;
    }

    public JLabel getProveraStatus() {
        return proveraStatus;
    }
    
    public boolean proveraPolja(){
        if(JMBG.getText().equals("") || JMBG.getText().length()!=13) return false;
        if(ime.getText().equals("")) return false;
        if(prezime.getText().equals("")) return false;
        if(imeM.getText().equals("")) return false;
        if(prezM.getText().equals("")) return false;
        if(imeO.getText().equals("")) return false;
        if(prezO.getText().equals("")) return false;
        if(datumR.getText().equals("")) return false;
        if(opstina.getText().equals("")) return false;
        if(pol.getText().equals("")) return false;
        if(ulica.getText().equals("")) return false;
        if(broj.getText().equals("")) return false;
        if(nacionalnost.getText().equals("")) return false;
        if(prof.getText().equals("")) return false;
        if(bracnoSt.getText().equals("")) return false;
        return true;
    }
    
    public int dohvatiKljuc(){
        String s=id.getText();
        if(s.equals("")) return 0;
        else return Integer.parseInt(s);
    }
    
    public void isprazniPolja(){
        JMBG.setText("");
        ime.setText("");
        prezime.setText("");
        imeM.setText("");
        prezM.setText("");
        imeO.setText("");
        prezO.setText("");
        datumR.setText("");
        opstina.setText("");
        pol.setText("");
        ulica.setText("");
        broj.setText("");
        nacionalnost.setText("");
        prof.setText("");
        bracnoSt.setText("");
    };
}
