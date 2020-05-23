package MailClient.model;

import MailClient.model.table.AbstractTableItem;
import MailClient.view.ViewFactory;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailMessageBean extends AbstractTableItem
{

	public static Map<String, Integer> formattedValues = new HashMap<String, Integer>();

	private SimpleStringProperty sender , subject , size , date;
	private Message messageReference;
	private Button markReadButton , markStarredButton , markAttachmentButton;
	private List<MimeBodyPart> attachmentList = new ArrayList<>();
	private StringBuffer attachmentName = new StringBuffer();

	public Button getMarkAttachmentButton() {
		return markAttachmentButton;
	}

	public void setMarkAttachmentButton(Button markAttachmentButton) {
		this.markAttachmentButton = markAttachmentButton;
	}

	public EmailMessageBean(String Subject, String Sender , int size, boolean isRead  , String date , Message messageReference){
		super(isRead);
		this.subject = new SimpleStringProperty(Subject);
		this.sender = new SimpleStringProperty(Sender);
		this.size = new SimpleStringProperty(formatSize(size));
		this.messageReference = messageReference;
		this.date = new SimpleStringProperty(date);
		this.markReadButton = new JFXButton();
		this.markReadButton = new ViewFactory().setReadStyle(markReadButton , isRead);
		this.markStarredButton = new JFXButton();
		this.markStarredButton = new ViewFactory().setStarredStyle(markStarredButton , isRead);
		this.markAttachmentButton = new JFXButton();
		this.markAttachmentButton = new ViewFactory().setReadStyle(markAttachmentButton , isRead);
	}

	public String getDate() { return date.get(); }
	public String getSender(){ return sender.get(); }
	public String getSubject(){ return subject.get(); }
	public String getSize(){ return size.get(); }
	public Message getMessageReference() { return messageReference;}
	public Button getMarkReadButton() { return markReadButton; }
	public void setMarkReadButton(Button markReadButton) { this.markReadButton = markReadButton; }
	public Button getMarkStarredButton() { return markStarredButton; }
	public void setMarkStarredButton(Button markStarredButton) { this.markStarredButton = markStarredButton; }
	public List<MimeBodyPart> getAttachmentsList() { return attachmentList; }
	public StringBuffer getAttachments() { return attachmentName; }

	public void addAttachments(MimeBodyPart mimeBodyPart) throws MessagingException {
		attachmentList.add(mimeBodyPart);
		attachmentName.append(mimeBodyPart.getFileName() + " ; ");
	}

	public Boolean hasAttachments()
	{
		return !attachmentList.isEmpty();
	}
	public void clearAttachments()
	{
		attachmentList.clear();
		attachmentName.setLength(0);
	}
	private String formatSize(int size)
	{
		String returnValue;
		if(size<= 0){
			returnValue =  "0";}

		else if(size<1024){
			returnValue = size + " B";
		}
		else if(size < 1048576){
			returnValue = size/1024 + " kB";
		}else{
			returnValue = size/1048576 + " MB";
		}
		formattedValues.put(returnValue, size);
		return returnValue;
	}

}
