package epqnemak;


import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eduardoriojas
 */
public class VentanaOutput2 extends JFrame {
    
    JTextPane texto = new JTextPane();
    StyledDocument doc = texto.getStyledDocument();
    JScrollPane scrolltxt = new JScrollPane(texto, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
    SimpleAttributeSet attSet = new SimpleAttributeSet();
    
    public VentanaOutput2()
    {
        super();
        //setLayout(null);
        Dimension dim2 = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(500,500);
        this.setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //scrolltxt.setBounds(3, 3, dim2.width, dim2.height);
        this.getContentPane().add(scrolltxt);
        
        texto.setBackground(Color.WHITE);
        setVisible(true);
    }
    
    public void Mostrar(int type, String outp) throws BadLocationException
    {
      Color colr = Color.blue;
      
      switch (type){
        case 1: colr = Color.BLACK;
                break;
        case 2: colr = Color.DARK_GRAY;
                break;
        case 3: colr = Color.GREEN;
                break;
        case 4: colr = Color.BLUE;
                break;
        case 5: colr = Color.MAGENTA;
                break;
        case 6: colr = Color.RED;
                break;
        case 7: colr = Color.PINK;
                break;
        case 8: colr = Color.ORANGE;
                break;
                  case 9: colr = Color.GREEN;
                break;
      } 
      
      
      attSet.addAttribute(StyleConstants.CharacterConstants.Foreground,
        colr);
      
      String typeS = Integer.toString(type);
      doc.insertString(doc.getLength(),outp, attSet);
      
      
      
  
    }


}