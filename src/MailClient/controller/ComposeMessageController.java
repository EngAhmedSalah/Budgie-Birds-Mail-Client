package MailClient.controller;

import MailClient.controller.Services.EmailSenderService;
import MailClient.model.EmailConstants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends AbstractController implements Initializable{

	private  String sender , reciever , subject;
	public ComposeMessageController(ModelAccess modelAccess , String sender , String reciever , String subject) {
		super(modelAccess);
		this.sender = sender;
		this.reciever = reciever;
		this.subject = subject;
	}
	public ComposeMessageController(ModelAccess modelAccess) {
		super(modelAccess);
	}
	private List<File> attachments = new  ArrayList<File>();

    @FXML
    private Label attachmentsLabel;

    @FXML
    private ChoiceBox<String> senderChoice;

    @FXML
    private JFXTextField recipientField;

    @FXML
    private JFXTextField subjectField;
    
    @FXML
    private HTMLEditor composeArea;

    @FXML
    private Label errorLabel;

    @FXML
	JFXButton sentBtn;
    @FXML
    void attchBtnAction() {
    	
    	FileChooser fileChooser = new FileChooser();
    	File selectedFile = fileChooser.showOpenDialog(null);
    	if(selectedFile != null){
    		attachments.add(selectedFile);
    		attachmentsLabel.setText(attachmentsLabel.getText() + selectedFile.getName() + "; ");
    	}

    }

    @FXML
    void sendBtnAction() {
    	errorLabel.setText("");
    	EmailSenderService emailSenderService =
    			new EmailSenderService(getModelAccess().getEmailAccountByName(senderChoice.getValue()),
    					subjectField.getText(), 
    					recipientField.getText(),
    					composeArea.getHtmlText(),
    					attachments);
    	emailSenderService.restart();
    	emailSenderService.setOnSucceeded(e->{
    		if(emailSenderService.getValue() == EmailConstants.MESSAGE_SENT_OK)
    		{
    			errorLabel.setText("message sent successefully");
				sentBtn.setDisable(true);
    		}
    		else
    			{
    			errorLabel.setText("message sending error!!!");
    		}
    	});
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		senderChoice.setItems(getModelAccess().getEmailAccountNames());
		senderChoice.setValue(getModelAccess().getEmailAccountNames().get(0));
		recipientField.setText(reciever);
		subjectField.setText(subject);
	}

}