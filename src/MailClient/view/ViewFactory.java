package MailClient.view;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;

import MailClient.controller.*;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.Buffer;

public class ViewFactory
{

    public static ViewFactory defaultFactory = new ViewFactory();
    private static boolean mainViewInitialized = false;

    private final String DEFAULT_CSS = "style.css";
    private final String MAIN_SCREEN_FXML = "MainLayout.fxml";
    private final String COMPOSE_SCREEN_FXML = "composeMessageLayout.fxml";

    private ModelAccess modelAccess = new ModelAccess();

    private MainController mainController;
    private EmailDetailsController emailDetailsController;


    public Scene getMainScene() throws OperationNotSupportedException {
        if (!mainViewInitialized) {
            mainController = new MainController(modelAccess);
            mainViewInitialized = true;
            return initializeScene(MAIN_SCREEN_FXML, mainController);
        } else {
            throw new OperationNotSupportedException("Main Scene allready initialized!!!!");
        }

    }

    public Scene getComposeMessageScene()
    {
        AbstractController composeController = new ComposeMessageController(modelAccess , "" , "" , "");
        return initializeScene(COMPOSE_SCREEN_FXML, composeController);
    }
    public Scene getReplyMessageScene(ModelAccess modelAccess , String sender , String receiever , String subject )
    {
        ComposeMessageController composeController = new ComposeMessageController(modelAccess , sender , receiever , subject);
        return initializeScene(COMPOSE_SCREEN_FXML, composeController);
    }
    public Button setReadStyle(Button button, boolean isRead) {
        if (isRead)
            button.setStyle(" -fx-background-color: #aeafaf;\n" + "  -fx-background-radius: 5em;\n" + "  -fx-min-width: 7px;\n" + "  -fx-min-height: 7px;\n" + "  -fx-max-width: 7px;\n" + "  -fx-max-height: 7px;");
        else
            button.setStyle(" -fx-background-color: #007a17;\n" + "  -fx-background-radius: 5em;\n" + "  -fx-min-width: 10px;\n" + "  -fx-min-height: 10px;\n" + "  -fx-max-width: 10px;\n" + "  -fx-max-height: 10px;");
        return button;
    }

    public Button setStarredStyle(Button button, boolean isRead) {
        if (isRead) {
            button.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.STAR_ALT , "15"));
            button.setStyle(" -fx-background-color: #aeafaf;\n" + "  -fx-min-width: 5px;\n" + "  -fx-min-height: 5px;\n" + "  -fx-max-width: 5px;\n" + "  -fx-max-height: 5px;");
        } else {
			button.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.STAR , "15"));
            button.setStyle(" -fx-background-color: #b0a700;\n" + "  -fx-min-width: 5px;\n" + "  -fx-min-height: 5px;\n" + "  -fx-max-width: 5px;\n" + "  -fx-max-height: 5px;");
        }
        return button;
    }

    public Node resolveIcon(String treeItemValue) {
        String lowerCaseTreeItemValue = treeItemValue.toLowerCase();
        ImageView returnIcon;
        try {
            if (lowerCaseTreeItemValue.contains("inbox"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/inbox.png")));
            else if (lowerCaseTreeItemValue.contains("starred") || lowerCaseTreeItemValue.contains("لرسائل المميزة بنجمة"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/starred.png")));
            else if (lowerCaseTreeItemValue.contains("bin") || lowerCaseTreeItemValue.contains("المهـملات"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/bin.png")));
            else if (lowerCaseTreeItemValue.contains("sent") || lowerCaseTreeItemValue.contains("بريد مرسل"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/sent2.png")));
            else if (lowerCaseTreeItemValue.contains("spam") || lowerCaseTreeItemValue.contains("الرسائل غير المرغوب فيها"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/spam.png")));
            else if (lowerCaseTreeItemValue.contains("draft") || lowerCaseTreeItemValue.contains("مسودّات"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/draft.png")));
            else if (lowerCaseTreeItemValue.contains("important") || lowerCaseTreeItemValue.contains("مهم"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/important.png")));
            else if (lowerCaseTreeItemValue.contains("@"))
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/email.png")));
            else
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("images/folder.png")));

        } catch (NullPointerException e) {
            System.out.println("Invalid image location!!!");
            e.printStackTrace();
            returnIcon = new ImageView();
        }

        returnIcon.setFitHeight(16);
        returnIcon.setFitWidth(16);

        return returnIcon;
    }

    private Scene initializeScene(String fxmlPath, AbstractController controller) {
        FXMLLoader loader;
        Parent parent;
        Scene scene;
        try {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            parent = loader.load();
        } catch (Exception e) {
            return null;
        }

        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS).toExternalForm());
        return scene;
    }


}
