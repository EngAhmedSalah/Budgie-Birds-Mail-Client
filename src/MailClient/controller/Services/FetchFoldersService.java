package MailClient.controller.Services;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import MailClient.controller.ModelAccess;
import MailClient.model.EmailAccountBean;
import MailClient.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FetchFoldersService extends Service<Void>{

    private EmailFolderBean<String> foldersRoot;
    private EmailAccountBean emailAccountBean;
    private ModelAccess modelAccess;
    private static int NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE = 0;


    public FetchFoldersService(EmailFolderBean<String> foldersRoot, EmailAccountBean emailAccountBean, ModelAccess modelAccess) {
        this.foldersRoot = foldersRoot;
        this.emailAccountBean = emailAccountBean;
        this.modelAccess = modelAccess;

        this.setOnSucceeded(e->{
            NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE--;
        });
    }


    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE++;
                if(emailAccountBean != null){
                    Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();

                    for(Folder folder:folders){
                        modelAccess.addFolder(folder);
                        EmailFolderBean<String> item = new EmailFolderBean<String>(folder.getName(), folder.getFullName());
                        foldersRoot.getChildren().add(item);
                        item.setExpanded(true);
                        addMessageListenerToFolder(folder, item);
                        FetchMessages fetchMessagesOnFolderService = new FetchMessages(item, folder);
                        fetchMessagesOnFolderService.start();
                        System.out.println("added: " + folder.getName());
                        Folder[] subFolders = folder.list();
                        for(Folder subFolder: subFolders){
                            modelAccess.addFolder(subFolder);
                            EmailFolderBean<String> subItem = new EmailFolderBean<String>(subFolder.getName(), subFolder.getFullName());
                            item.getChildren().add(subItem);
                            addMessageListenerToFolder(subFolder, subItem);
                            FetchMessages fetchMessagesOnSubFolderService = new FetchMessages(subItem, subFolder);
                            fetchMessagesOnSubFolderService.start();
                            System.out.println("added: " + subFolder.getName());
                        }
                    }
                }
                return null;
            }

        };
    }

    private void addMessageListenerToFolder(Folder folder, EmailFolderBean<String> item){
        folder.addMessageCountListener(new MessageCountAdapter(){
            @Override
            public void messagesAdded(MessageCountEvent e){
                for(int i = 0; i <e.getMessages().length; i++){
                    try {
                        Message currentMessage = folder.getMessage(folder.getMessageCount() - i);
                        item.addEmail(0, currentMessage);
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public static boolean noServicesActive(){
        return NUMBER_OF_FETCHFOLDERSSERVICES_ACTIVE == 0;
    }




}
