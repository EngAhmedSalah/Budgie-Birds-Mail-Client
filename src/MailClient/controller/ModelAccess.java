package MailClient.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Folder;

import MailClient.model.EmailAccountBean;
import MailClient.model.EmailMessageBean;
import MailClient.model.folder.EmailFolderBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModelAccess {


	private Map<String, EmailAccountBean> emailAccounts = new HashMap<String, EmailAccountBean>();
	private ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();

	public ObservableList<String> getEmailAccountNames(){
		return emailAccountsNames;
	}

	public EmailAccountBean getEmailAccountByName(String name){
		return emailAccounts.get(name);
	}

	public void addAccount(EmailAccountBean account){
		emailAccounts.put(account.getEmailAdress(), account);
		emailAccountsNames.add(account.getEmailAdress());
	}

	private EmailMessageBean selectedMessage;

	public EmailMessageBean getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(EmailMessageBean selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public EmailFolderBean<String> getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	private EmailFolderBean<String> selectedFolder;

	private List<Folder> foldersList = new ArrayList<Folder>();

	public List<Folder> getFoldersList(){
		return  foldersList;
	}

	public void addFolder(Folder folder){
		foldersList.add(folder);
	}

}
