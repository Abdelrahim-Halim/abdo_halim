/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 *
 * @author halim
 */
public class Notepad extends Application {
    
    public TextArea txtArea;
    public MenuBar menuBar;
    public Menu file;
    public Menu edit;
    public Menu help;
    public BorderPane bP;
    public FileWriter fileWriter;
    public PrintWriter printWriter;
    public FileChooser fc;
    public Stage primaryStage;
    public BufferedReader bufferedReader;
    public PrintStream ps ;
    @Override
    public void init() throws Exception {
        super.init();
        setMenuBar();
        setTxtArea();
        fc = new FileChooser();
    }
    
    @Override
    public void start(Stage primaryStage) {
         
        bP = new BorderPane();
        bP.setTop(menuBar);
        bP.setCenter(txtArea);
       
        //StackPane root = new StackPane();
        //root.getChildren().add(btn);
        
        Scene scene = new Scene(bP, 300, 250);
        
        this.primaryStage=primaryStage;
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    private void setMenuBar(){
        menuBar = new MenuBar(getFileMenu(),getEditMenu(),getHelpMenu());
    }
    private Menu getFileMenu(){
        file = new Menu("File");
        // intialize menu items
        file.getItems().addAll(newMenuItem(),openMenuItem(),saveMenuItem());
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitMenuItem());
        return file;
    }
    private MenuItem newMenuItem(){
        // new menu item
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+q"));
        newMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!txtArea.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("New File");
                    alert.setHeaderText("File is not Empty !");
                    alert.setContentText("Are U want to save it !?");

                    ButtonType buttonTypeYes = new ButtonType("Yes");
                    ButtonType buttonTypeNo = new ButtonType("No");

                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeYes) {
                       saveFile();
                    } else {
                        txtArea.setText("");
                    }

                }
            }
        });
        return newMenuItem;
    }
    private MenuItem openMenuItem(){
        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+w"));
        openMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    File file = fc.showOpenDialog(primaryStage);
                    bufferedReader = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = bufferedReader.readLine())!= null) {
                        txtArea.appendText(st);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Notepad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return openMenuItem;
    }
    private MenuItem saveMenuItem(){
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+e"));
        saveMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(!txtArea.getText().isEmpty())
                    saveFile();
            }
        });
        return saveMenuItem;
    }
    private MenuItem exitMenuItem(){
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+r"));
        exitMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });
        return exitMenuItem;
    }
    // Start Edit menu
    private Menu getEditMenu(){
        edit = new Menu("Edit");
        // intialize menu items
        edit.getItems().add(undoMenuItem());
        edit.getItems().add(new SeparatorMenuItem());
        edit.getItems().add(cutMenuItem());
        edit.getItems().add(copyMenuItem());
        edit.getItems().add(pasteMenuItem());
        edit.getItems().add(deleteMenuItem());
        edit.getItems().add(new SeparatorMenuItem());
        edit.getItems().add(selectAllMenuItem());
        return edit;
    }
    private MenuItem undoMenuItem(){
        MenuItem undoMenuItem = new MenuItem("Undo");
        undoMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+t"));
        undoMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.undo();
            }
        });
        return undoMenuItem;
    }
    private MenuItem cutMenuItem(){
        MenuItem cutMenuItem = new MenuItem("Cut");
        cutMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+y"));
        cutMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.cut();
            }
        });
        return cutMenuItem;
    }
    private MenuItem copyMenuItem(){
        MenuItem copyMenuItem = new MenuItem("copy");
        copyMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+u"));
        copyMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.copy();
            }
        });
        return copyMenuItem;
    }
    private MenuItem pasteMenuItem(){
        MenuItem pasteMenuItem = new MenuItem("Paste");
        pasteMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+i"));
        pasteMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.paste();
            }
        });
        return pasteMenuItem;
    }
    private MenuItem deleteMenuItem(){
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+o"));
        deleteMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.deleteText(txtArea.getSelection());
            }
        });
        return deleteMenuItem;
    }
    private MenuItem selectAllMenuItem(){
        MenuItem selectAllMenuItem = new MenuItem("Select All");
        selectAllMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+p"));
        selectAllMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtArea.selectAll();
            }
        });
        return selectAllMenuItem;
    }
    // start help menu
    private Menu getHelpMenu(){
        help = new Menu("Help");
        help.getItems().add(aboutMenuItem());
        help.getItems().add(comMenuItem());
        // intialize menu items
        return help;
    }
    private MenuItem aboutMenuItem(){
        MenuItem selectAllMenuItem = new MenuItem("About");
        selectAllMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+l"));
        selectAllMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("About NotPad");
              alert.setHeaderText("");
              alert.setContentText("this NotePad was Created in ITI by Halim");
              alert.showAndWait();
            }
        });
        return selectAllMenuItem;
    }
    private MenuItem comMenuItem(){
        MenuItem comMenuItem = new MenuItem("Compile");
        comMenuItem.setAccelerator(KeyCodeCombination.keyCombination("ctrl+k"));
        comMenuItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                JavaCompiler comp = ToolProvider.getSystemJavaCompiler() ;
                File file = fc.showOpenDialog(primaryStage);
                comp.run(null, null, null,file.toPath().toString());
            
            }
        });
        return comMenuItem;
    }
    private void setTxtArea(){
        txtArea = new TextArea();
    }
    private void saveFile() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save File");
        dialog.setContentText("Plz enter file name:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                // save in a file
                fileWriter = new FileWriter(result.get(), true);
                printWriter = new PrintWriter(fileWriter);
                printWriter.println(txtArea.getText());
                printWriter.close();
                fileWriter.close();
                txtArea.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
