/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class DashBoardController {

    @FXML
    private AnchorPane sidePane;
    @FXML
    private Label dashboardLabel;
    @FXML
    private ImageView invoiceLogo;
    @FXML
    private VBox buttonsVBox;
    @FXML
    private JFXButton invoice;
    @FXML
    private JFXButton products;
    @FXML
    private JFXButton customers;
    @FXML
    private AnchorPane headerPane;
    @FXML
    private JFXHamburger sidemenuToggle;

    /**
     * Initializes the controller class.
     */
    private enum Windows {
        HOME,
        INVOICE,
        PRODUCTS,
        CUSTOMERS,
        SETTINGS
    }
    @FXML
    private JFXButton settings;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton home;
    @FXML
    private AnchorPane contentNode;
    private boolean isMinimized = false;
    private HamburgerNextArrowBasicTransition ham;

    public void initialize() throws IOException, SQLException {
        dashboardLabel.setCursor(Cursor.HAND);
//        dashboardLabel.setOnMouseClicked(e -> {
//            new RubberBand(dashboardLabel).play();
//        });
        ham = new HamburgerNextArrowBasicTransition(sidemenuToggle);
        ham.setRate(-1);
        invoice.setOnMouseClicked(e -> openView(Windows.INVOICE));
        products.setOnMouseClicked(e -> openView(Windows.PRODUCTS));
        customers.setOnMouseClicked(e -> openView(Windows.CUSTOMERS));
        settings.setOnMouseClicked(e -> openView(Windows.SETTINGS));
        home.setOnMouseClicked(e -> openView(Windows.HOME));
        sidePane.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            AnchorPane.setLeftAnchor(headerPane, newValue.doubleValue());
            AnchorPane.setLeftAnchor(contentNode, newValue.doubleValue());
        });
        sidemenuToggle.setCursor(Cursor.HAND);
        sidemenuToggle.setOnMouseClicked(e -> {
            ham.setRate(ham.getRate() * -1);
            ham.play();
            if (isMinimized) {
                maximize();
                isMinimized = false;
            } else {
                minimize();
                isMinimized = true;
            }
        });
//        openView(Windows.HOME);
//        setAnimation();

//        new FadeInLeft(sidePane).setDelay(Duration.seconds(0.5)).play();
//        new FadeInDown(headerPane).setDelay(Duration.seconds(0.5)).play();
//        new RubberBand(dashboardLabel).setDelay(Duration.seconds(1.5)).play();
    }

    private void openView(Windows type) {
        String location = null;
        switch (type) {
            case HOME:
                location = "/simpleinvoice/view/home.fxml";
                break;
            case INVOICE:
                location = "/simpleinvoice/view/invoice.fxml";
                break;
            case PRODUCTS:
                location = "/simpleinvoice/view/products.fxml";
                break;
            case CUSTOMERS:
                location = "/simpleinvoice/view/customers.fxml";
                break;            
            case SETTINGS:
                location = "/simpleinvoice/view/settings.fxml";
                break;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        AnchorPane node = null;
        try {
            node = loader.load();
        } catch (IOException ex) {
            System.out.println("Cannot Load" + type.name() + " View");
        }
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        contentNode.getChildren().clear();
        contentNode.getChildren().add(node);
//        if (type != Windows.HOME) {
//            new JackInTheBox(node).play();
//        } else {
//            new FadeIn(node).play();
//        }
    }

//    public void setAnimation() {
//        buttonsVBox.getChildren().forEach(ex -> {
//            ex.setOnMouseEntered(e -> new FadeIn(ex).play());
//        });
//    }

    public void minimize() {
        buttonsVBox.getChildren().forEach(e -> {
            JFXButton b = (JFXButton) e;
            b.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        });
        dashboardLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        dashboardLabel.setAlignment(Pos.CENTER);
        sidePane.setPrefWidth(sidePane.getWidth() - 130);
    }

    public void maximize() {
        buttonsVBox.getChildren().forEach(e -> {
            JFXButton b = (JFXButton) e;
            b.setContentDisplay(ContentDisplay.LEFT);
        });
        dashboardLabel.setContentDisplay(ContentDisplay.LEFT);
        sidePane.setPrefWidth(sidePane.getWidth() + 130);

    }
}    
