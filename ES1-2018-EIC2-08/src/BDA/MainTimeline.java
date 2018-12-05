package BDA;

import javax.mail.Message;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.restfb.types.Post;
import twitter4j.TwitterException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


/**
 * Class that represents the app's MainTimiline with access to the Email, Facebook and Twitter Timeline and to its respective contents
 * @author Diogo
 *
 */

public class MainTimeline {

private JFrame launcher;
private Facebook face;
private TwitterApp twitterapp;
private boolean isFaceOn;
private boolean isMailOn;
private boolean isTwitterOn;
private JList<FacePost> facePosts;
private JList <T> tweets;
private JList<Email> emails;
private DefaultListModel<Email> mail;

	
/**
 * Contructor, initiates the GUI and displays the content.
 * @param face Given Facebook access
 * @param twitter Given Twitter access
 * @param username Given Account Outlook Email
 * @param password Given Account Outlook Password
 * @throws TwitterException problem in the Twitter
 */
public MainTimeline(Facebook face, TwitterApp twitter, String username, String password, boolean f, boolean t) throws TwitterException{
	try {
		this.face=face;
		this.twitterapp=twitter;
		this.isFaceOn=f;
		this.isTwitterOn=t;
		this.isMailOn=Mail.isMailOnline();
		init(username, password);
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



/**
 * It creates the Gui with all its Features.
 * Displays various Emails, Tweets and Facebook posts
 * Gives acess to every Email, Tweets and Facebook posts displayed in the GUI
 * @param username Given email account
 * @param password Given email account's password
 * @throws IOException Some I/O exception occurred.
 * @throws TwitterException Some Twitter Exception occurred.
 */

private void init(String username, String password) throws IOException, TwitterException {
	
	//SettingsJFrame
	launcher = new JFrame("BOM DIA ACADEMIA!");
	launcher.setResizable(false);
	launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	launcher.setLayout(new BorderLayout());
	launcher.setVisible(true);
	
	//Background
	Background background=new Background("images/maintimeline.png");
	background.setLayout(new GridBagLayout());
	background.setPreferredSize(new Dimension(800,600));
	
	
	//JTextPanes
	JPanel email= new JPanel(new FlowLayout());
	email.setOpaque(false);
	email.setPreferredSize(new Dimension(245,525));
	GridBagConstraints c=new GridBagConstraints();
	c.gridx=0;
	c.gridy=1;
	c.insets=new Insets(70, 0,0,0);
	c.gridheight=3;
	background.add(email,c);
	
	JPanel facebook= new JPanel();
	facebook.setOpaque(false);
	facebook.setPreferredSize(new Dimension(245,525));
	c.gridx=1;
	c.gridy=1;
	c.gridheight=3;
	c.insets=new Insets(70,15,0,15);
	background.add(facebook,c);
	
	JPanel twitter= new JPanel();
	twitter.setOpaque(false);
	twitter.setPreferredSize(new Dimension(245,525));
	c.gridx=2;
	c.gridy=1;
	c.gridheight=3;
	c.insets=new Insets(70, 0,0,0);
	background.add(twitter,c);
	
	//JList<Email> emails;
	emails=new JList<Email>();
	
	
	((DefaultListCellRenderer)emails.getCellRenderer()).setOpaque(false);
	emails.setFixedCellHeight(70);
	emails.setBorder(new EmptyBorder(10,5, 10, 0));
	emails.setOpaque(false);

	emails.addListSelectionListener( new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if (emails.getSelectedValue() != null && !e.getValueIsAdjusting()) {
				Email selectedValue = emails.getSelectedValue();
				System.out.println("Body:"+ selectedValue.getBody());
				new EmailSingular(selectedValue);
			}
		}
	});
	
	
	JScrollPane scroll=new JScrollPane(emails);
	scroll.setOpaque(false);
	scroll.getViewport().setOpaque(false);
	scroll.setPreferredSize(new Dimension(250,490));
	//email.add(scroll);
	
new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JTextPane load=new JTextPane();
			load.setEditable(false);
			load.setOpaque(false);
			load.setText("Loading...");
			load.setPreferredSize(new Dimension(100,50));
			email.add(load);
			
			System.out.println(isMailOn);
			if(isMailOn){
				
				 mail=Mail.LoginMail(username, password);
			}else{
				 mail=Mail.FetchFromBackup();
			}
			
			email.remove(load);
			emails.setModel(mail);
			email.add(scroll);
			launcher.revalidate();
			launcher.repaint();
			
		}
	}).start();
	
	//JList<FacePost> facePosts;
	System.out.println(isFaceOn);
	System.out.println(isTwitterOn);
	if(isFaceOn && face!=null){
		 facePosts=new JList<FacePost>(face.getTimeLinePosts());//filtro retirado pra teste
	}else{
		 facePosts=new JList<FacePost>(Facebook.FetchFromBackup());
	}
	
	((DefaultListCellRenderer)facePosts.getCellRenderer()).setOpaque(false);
	facePosts.setFixedCellHeight(70);
	facePosts.setBorder(new EmptyBorder(10,5, 10, 0));
	facePosts.setOpaque(false);

	facePosts.addListSelectionListener( new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if (facePosts.getSelectedValue() != null && !e.getValueIsAdjusting()) {
				FacePost selectedValue = facePosts.getSelectedValue();
				new FacebookPost(selectedValue);
			}
		}
	});
	
	
	JScrollPane scroll2=new JScrollPane(facePosts);
	scroll2.setOpaque(false);
	scroll2.getViewport().setOpaque(false);
	scroll2.setPreferredSize(new Dimension(250,490));
	facebook.add(scroll2);
	
	//JList <T> tweets;
	
	if(isTwitterOn && twitterapp!=null){
		tweets=new JList<T>(twitterapp.getTimeline());
	}else{
		tweets=new JList<T>(TwitterApp.FetchFromBackup());
	}
	
	((DefaultListCellRenderer)tweets.getCellRenderer()).setOpaque(false);
	tweets.setFixedCellHeight(70);
	tweets.setBorder(new EmptyBorder(10,5, 10, 0));
	tweets.setOpaque(false);

	tweets.addListSelectionListener( new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			
			if (tweets.getSelectedValue() != null && !e.getValueIsAdjusting()) {
				T selectedValue = tweets.getSelectedValue();
				new Tweet(twitterapp, selectedValue);
			}
		}
	});
	
	//
	
	JScrollPane scroll3=new JScrollPane(tweets);
	scroll3.setOpaque(false);
	scroll3.getViewport().setOpaque(false);
	scroll3.setPreferredSize(new Dimension(250,490));
	twitter.add(scroll3);
	
	
	//Atualizar conteudo
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	JButton but= new JButton("Atualizar");
	
	 but.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if(isTwitterOn && twitterapp!=null){
	        		try {
						tweets=new JList<T>(twitterapp.getTimeline());
					} catch (TwitterException e1) {
						e1.printStackTrace();
					}
	        	}
	        	if(isFaceOn && face!=null){
	       		 facePosts=new JList<FacePost>(face.getTimeLinePosts());
	       	} 	
	        	if(isMailOn){
					
					 mail=Mail.LoginMail(username, password);
				}
	        	emails.setModel(mail);
	        }
	    } );
	 
	
	
		
		but.setPreferredSize(new Dimension(50,20));
		c.gridx=0;
		c.gridy=0;
		c.gridheight=1;
		c.insets=new Insets(10,10,0,0);
		background.add(but,c);
		
	 
		
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	launcher.add(background);
	launcher.setSize(800, 600);
	launcher.pack();

 }

/**
 * Gets the JFrame that serves as launcher of the window.
 * @return launcher
 */




public JFrame getLauncher() {
	return launcher;
}




}