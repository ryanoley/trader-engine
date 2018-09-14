package com.roundaboutam.trader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.StringJoiner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.roundaboutam.trader.MessageContainer;
import com.roundaboutam.trader.TraderApplication;

import quickfix.Message;



@SuppressWarnings("serial")
public class FIXMonitor extends JPanel implements Observer, Runnable  {

    protected final HashMap<Integer, MessageContainer> messageMap;
    protected final HashMap<Integer, Date> timeStampMap;
    private JTextArea sessionArea;
    private JTextArea seqArea;
    private JTextArea heartbeatArea;
    private Integer seqNum = 0;
    private Date lastContact = new Date();
    private String sessionID = "NOT CONNECTED";


	public FIXMonitor(TraderApplication application) {
		this.setBackground(Color.LIGHT_GRAY);
		this.add(setSessionArea(sessionID), BorderLayout.LINE_START);
		this.add(setSeqArea(seqNum), BorderLayout.LINE_START);
		this.add(setHeartbeatArea(), BorderLayout.LINE_START);
		application.addMessageObserver(this);
		messageMap = new HashMap<Integer, MessageContainer>();
		timeStampMap = new HashMap<Integer, Date>();
	}

	private JTextArea setSessionArea(String areaText) {
		sessionArea = new JTextArea();
		sessionArea.setBackground(Color.LIGHT_GRAY);
		sessionArea.setForeground(Color.BLUE);
		sessionArea.setText("CompID: " + areaText);
		return sessionArea;
	}

	private JTextArea setSeqArea(Integer areaNum) {
		seqArea = new JTextArea();
		seqArea.setBackground(Color.LIGHT_GRAY);
		seqArea.setForeground(Color.BLUE);
		seqArea.setText("MsgSeqNum: " + areaNum);
		return seqArea;
	}

	private JTextArea setHeartbeatArea() {
		heartbeatArea = new JTextArea();
		heartbeatArea.setBackground(Color.LIGHT_GRAY);
		heartbeatArea.setForeground(Color.BLUE);
		heartbeatArea.setText("ContactDiff: -");
		return heartbeatArea;
	}

    private void addMessage(Message message) {
    	int row = messageMap.size();
    	MessageContainer messageContainer = new MessageContainer(message);
    	if (messageContainer.getDirection().equals("Inbound")) {
        	Date timeNow = new Date();
    		messageMap.put(row, messageContainer);
        	timeStampMap.put(row, timeNow);
        	lastContact = timeNow;
        	seqNum = messageContainer.getMsgSeqNum();
            sessionID = messageContainer.getTargetCompID();
            }
    }

	public void update(Observable o, Object arg) {
		Message message = (Message) arg;
        addMessage(message);
	}

	JFrame alertFrame;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		try {
			while (true) {
				Long contactDiff = new Date().getTime() - lastContact.getTime();
				sessionArea.setText("CompID: " + sessionID);
				seqArea.setText("MsgSeqNum: " + seqNum);
				heartbeatArea.setText("ContactDiff: " + contactDiff);
				
	            if (sessionID.equals("ROUNDPROD01")){
	            	sessionArea.setForeground(Color.RED);
	            	Font sessionFont = sessionArea.getFont();
					Map attributes = sessionFont.getAttributes();
	            	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	            	attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
	            	attributes.put(TextAttribute.SIZE, 16L);
	            	sessionArea.setFont(sessionFont.deriveFont(attributes));
	            }
	            
	            // If no contact after 30 seconds change font
				if (contactDiff > 30000){
					heartbeatArea.setForeground(Color.RED);
	            	Font heartbeatFont = heartbeatArea.getFont();
					Map attributes = heartbeatFont.getAttributes();
	            	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	            	attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
	            	attributes.put(TextAttribute.SIZE, 16L);
	            	heartbeatArea.setFont(heartbeatFont.deriveFont(attributes));
				}
				else{
					heartbeatArea.setForeground(Color.BLUE);
	            	Font heartbeatFont = heartbeatArea.getFont();
					Map attributes = heartbeatFont.getAttributes();
	            	attributes.put(TextAttribute.UNDERLINE, -1);
	            	attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
	            	attributes.put(TextAttribute.SIZE, 12L);
	            	heartbeatArea.setFont(heartbeatFont.deriveFont(attributes));
				}
				
				// If no contact for one minute alert
				if (contactDiff > 60000 && contactDiff < 62000){
	            	alertFrame = new JFrame();
	            	alertFrame.setLocation(500, 400);
	        		alertFrame.setVisible(true);
	        		alertFrame.setAlwaysOnTop(true);
	        		String alertMessage = "FIX ISSUE: No messages recieved in last 60 seconds";
	        		int input = JOptionPane.showOptionDialog(alertFrame, alertMessage, "FIX MESSAGE ALERT", 
	        				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	        		alertFrame.dispose();
	            }

	            Thread.sleep(1000);
	         }
	      }
	      catch (Exception e) {
	    	  e.printStackTrace();
	      }
	}
	
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

}

