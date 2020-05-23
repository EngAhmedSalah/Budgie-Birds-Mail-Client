package MailClient.controller.Services;

import MailClient.controller.ModelAccess;
import MailClient.model.EmailAccountBean;
import MailClient.model.EmailMessageBean;
import MailClient.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Flags;
import javax.mail.Folder;

public class DeleteMessageService extends Service
{
    private EmailFolderBean<String> folderBean;
    private EmailMessageBean messageBean;
    private Folder folder;

    public DeleteMessageService(EmailFolderBean folderBean , EmailMessageBean messageBean)
    {
        this.messageBean = messageBean;
        this.folderBean = folderBean;
    }
    @Override
    protected Task createTask()
    {
        return new Task() {
            @Override
            protected Object call() throws Exception
            {
                messageBean.getMessageReference().setFlag(Flags.Flag.DELETED , true);
                folderBean.decrementUnreadMessagesCount();
                return null;
            }
        };
    }
}
