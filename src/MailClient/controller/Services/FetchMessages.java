package MailClient.controller.Services;

import MailClient.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;

public class FetchMessages extends Service<Void>
{
    private EmailFolderBean<String> emailFolderBean;
    private Folder folder;

    public FetchMessages(EmailFolderBean<String> emailFolderBean, Folder folder) {
        this.emailFolderBean = emailFolderBean;
        this.folder = folder;
    }

    @Override
    protected Task<Void> createTask()
    {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception
            {
                if(folder.getType() != Folder.HOLDS_FOLDERS)
                    folder.open(Folder.READ_WRITE);
                int sz = folder.getMessageCount();
                for (int i = sz; i > 0 ; --i)
                {
                    Message message = folder.getMessage(i);
                    emailFolderBean.addEmail(-1, message);
                }
                return null;
            }
        };
    }
}
