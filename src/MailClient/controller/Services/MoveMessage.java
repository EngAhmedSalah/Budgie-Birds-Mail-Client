package MailClient.controller.Services;

import MailClient.model.EmailAccountBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

public class MoveMessage extends Service
{

    @Override
    protected Task createTask() {
        return new Task()
        {
            @Override
            protected Object call() throws Exception
            {

                return null;
            }

        };
    }
}
