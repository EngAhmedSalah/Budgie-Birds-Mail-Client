package MailClient.controller;

import MailClient.controller.Services.*;
import MailClient.model.EmailMessageBean;
import MailClient.model.folder.EmailFolderBean;
import MailClient.model.table.BoldableRowFactory;
import MailClient.view.ViewFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.mail.*;
import javax.naming.OperationNotSupportedException;
import javax.swing.text.LabelView;
import javax.swing.text.View;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable
{

	public MainController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	@FXML
	private TreeView<String> emailFoldersTreeView;
	private MenuItem showDetails = new MenuItem("show details");
	private String username = "ahmed.salahfci1164@gmail.com";// change accordingly
	private String password = "BudgiBirds1841998";// change accordingly
	private SaveAttachmentsService saveAttachmentsService;
	private CreateAndRegisterEmailAccountService small;
	@FXML
	private TableView<EmailMessageBean> emailTableView;

	@FXML
	private TableColumn<EmailMessageBean, String> subjectCol , senderCol , sizeCol;

	@FXML
	private WebView messageRenderer;

	@FXML
	private JFXButton Button1 , downAttachmentBtn , deleteBtn , reply , forward;

	@FXML
	private TableColumn<EmailMessageBean, Button> markStarredCol , markReadCol , markAttachmentCol;

	@FXML
	private TableColumn<EmailMessageBean, String> dateCol , timeCol;

	@FXML
	private Label fromLabel , subjectLabel , dateLabel , downAttachmentsLabel;

	@FXML
	private JFXProgressBar downAttachmentsProgress;

	@FXML
	void Button1Action(ActionEvent event)
	{
		Scene scene = ViewFactory.defaultFactory.getComposeMessageScene();
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void changeReadAction() {
		EmailMessageBean message = getModelAccess().getSelectedMessage();
		if (message != null)
		{
			boolean value = message.isRead();
			message.setRead(!value);
			ViewFactory viewFactory = new ViewFactory();
			viewFactory.setReadStyle(message.getMarkReadButton() , !value);
			EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
			if (selectedFolder != null)
			{
				if (value)
					selectedFolder.incrementUnreadMessagesCount(1);
				else
					selectedFolder.decrementUnreadMessagesCount();
			}
		}
	}

	@FXML
	void downAttachBtnAction(ActionEvent event) {
		EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
		if(message != null  && message.hasAttachments()){
			saveAttachmentsService.setMessageToDownload(message);
			saveAttachmentsService.restart();
		}
	}

	@FXML
	void deleteAction(ActionEvent event) throws MessagingException
	{
		EmailMessageBean messageBean = emailTableView.getSelectionModel().getSelectedItem();
		EmailFolderBean<String> folderBean = getModelAccess().getSelectedFolder();
		DeleteMessageService deleteMessageService = new DeleteMessageService(folderBean , messageBean);
		FolderUpdaterService folderUpdaterService = new FolderUpdaterService(getModelAccess().getFoldersList());
		emailFoldersTreeView.getRoot().getChildren().clear();
		deleteMessageService.start();
		small.restart();
		emailTableView.getItems().clear();
	}

    @FXML
    void replyAction(ActionEvent event)
    {

        EmailMessageBean messageBean = emailTableView.getSelectionModel().getSelectedItem();
        Scene scene = ViewFactory.defaultFactory.getReplyMessageScene(getModelAccess() ,username , messageBean.getSender() , messageBean.getSubject());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
	@FXML
	void forwardAction(ActionEvent event)
	{
		EmailMessageBean messageBean = emailTableView.getSelectionModel().getSelectedItem();
		Scene scene = ViewFactory.defaultFactory.getReplyMessageScene(getModelAccess() ,username , messageBean.getSender() , messageBean.getSubject());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

	private MessageRendererService messageRendererService;
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		downAttachmentsProgress.setVisible(false);
		downAttachmentsLabel.setVisible(false);
		saveAttachmentsService = new SaveAttachmentsService(downAttachmentsProgress , downAttachmentsLabel);
		messageRendererService = new MessageRendererService(messageRenderer.getEngine());
		FolderUpdaterService folderUpdateService = new FolderUpdaterService(getModelAccess().getFoldersList());
		folderUpdateService.start();

		EmailFolderBean<String> root = new EmailFolderBean<>("");
		emailFoldersTreeView.setRoot(root);
		emailFoldersTreeView.setShowRoot(false);

		small = new CreateAndRegisterEmailAccountService(username , password , root , getModelAccess());
		small.start();



		emailTableView.setRowFactory(e-> new BoldableRowFactory<>());
		ViewFactory viewfactory = ViewFactory.defaultFactory;
		subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
		senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		markReadCol.setCellValueFactory(new PropertyValueFactory<>("markReadButton"));
		markStarredCol.setCellValueFactory(new PropertyValueFactory<>("markStarredButton"));
		markAttachmentCol.setCellValueFactory(new PropertyValueFactory<>("markAttachmentButton"));

		sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
		sizeCol.setComparator(new Comparator<String>() {
			Integer int1, int2;
			@Override
			public int compare(String o1, String o2) {
				int1 = EmailMessageBean.formattedValues.get(o1);
				int2 = EmailMessageBean.formattedValues.get(o2);
				return int1.compareTo(int2);
			}
		});

		emailFoldersTreeView.setOnMouseClicked(e ->{
			EmailFolderBean<String> item = (EmailFolderBean<String>)emailFoldersTreeView.getSelectionModel().getSelectedItem();
			if(item != null && !item.isTopElement()){
				emailTableView.setItems(item.getData());
				getModelAccess().setSelectedFolder(item);
				//clear the selected message:
				getModelAccess().setSelectedMessage(null);
			}
		});

		emailTableView.setOnMouseClicked(e->{
			EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
			if(message != null)
			{
				getModelAccess().setSelectedMessage(message);
				messageRendererService.setMessageToRender(message);
				messageRendererService.restart();
				Button button = message.getMarkReadButton();
				System.out.println("Test Attachments" + message.hasAttachments());
				button.setOnAction(event ->
				{
					System.out.println("AKAAKA");
					if (message != null)
					{
						boolean value = message.isRead();
						try {
							message.getMessageReference().setFlag(Flags.Flag.SEEN , !value);
						} catch (MessagingException ex) {
							ex.printStackTrace();
						}
						message.setRead(!value);
						viewfactory.setReadStyle(button , message.isRead());
						EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
						if (selectedFolder != null)
						{
							if (value)
								selectedFolder.incrementUnreadMessagesCount(1);
							else
								selectedFolder.decrementUnreadMessagesCount();
						}
					}
				});

				Button button2 = message.getMarkStarredButton();
				button2.setOnAction(event ->
				{
					System.out.println("AKAAKA");
					if (message != null)
					{
						boolean value = message.isRead();
						message.setRead(!value);
						viewfactory.setStarredStyle(button2 , message.isRead());
						EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
						if (selectedFolder != null)
						{
							if (value)
								selectedFolder.incrementUnreadMessagesCount(1);
							else
								selectedFolder.decrementUnreadMessagesCount();
						}
					}
				});

				fromLabel.setText("From : " + message.getSender());
				subjectLabel.setText("Subject : " + message.getSubject());
				dateLabel.setText("Date : " + message.getDate());
			}
		});

		markReadCol.setStyle("-fx-alignment: CENTER;");
		markStarredCol.setStyle("-fx-alignment: CENTER;");
		markAttachmentCol.setStyle("-fx-alignment: CENTER;");
	}
}