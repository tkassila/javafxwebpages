package com.metait.javafxwebpages;

import javafx.collections.transformation.FilteredList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.net.URISyntaxException;
import java.awt.Desktop;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

import com.metait.javafxwebpages.datarow.WebAddressRow;
import com.metait.javafxwebpages.datarow.WebAddresItem;
import com.metait.javafxwebpages.datarow.JSONWebAddress;
import com.metait.javafxwebpages.url.Url2String;

public class WebPagesController {
    @FXML
    private TextField textFieldSearch;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonMody;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonGlobalSearch;
    @FXML
    private Button buttonSearch;
    @FXML
    private TableView<WebAddresItem> tableViewWebPages;
    @FXML
    private TitledPane titledPaneWebPage;
    @FXML
    private SplitPane splitPanelWebPages;
    @FXML
    private Pane paneWebPages;
    @FXML
    private Label labelMsg;
    @FXML
    private TableColumn<WebAddresItem,Integer> tableColumnNr;
    @FXML
    private TableColumn<WebAddresItem,String>  tableColumnDate;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnStar;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnWebAddress;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnKeyWord;
    @FXML
    private TableColumn<WebAddresItem,String> tableColumnTitle;
    @FXML
    private TableColumn<WebAddresItem,Integer> tableColumnBookMk;
    @FXML
    private Label labelDate;
    @FXML
    private Label labelOrder;
    @FXML
    private ComboBox<String> comboStar;
    @FXML
    private TextField textFieldKeyWord;
    @FXML
    private HBox hboxEdit;
    @FXML
    private TextField textFieldWebAddress;
    @FXML
    private Button buttonSave;
    @FXML
    private TextField textFieldTitle;
    @FXML
    private RadioButton radioButtonNr;
    @FXML
    private RadioButton radioButtonDate;
    @FXML
    private RadioButton radioButtonStar;
    @FXML
    private RadioButton radioButtonKeyWord;
    @FXML
    private RadioButton radioButtonTitle;
    @FXML
    private RadioButton radioButtonWebAddress;
    @FXML
    private WebView webView;
    @FXML
    private Button buttonOpenBrowser;
    @FXML
    private RadioButton radioButtonGlobal;
    @FXML
    private ToggleButton buttonListAll;
    @FXML
    private ToggleButton buttonBookMark;
    @FXML
    private RadioButton radioButtonBookMark;
    @FXML
    private TextField textFieldShow;

    /*
    @FXML
    private Label headerNumbere;
    @FXML
    private Label headerDate;
    @FXML
    private Label headerKeyWords;
    @FXML
    private Label headerWebAddress;
    @FXML
    private Label headerStar;
    */

    private int iStartRow = 1;
    private ObservableList<WebAddresItem> webAddressRows = FXCollections.<WebAddresItem>observableArrayList();
    private ChangeListener<Boolean> radioButtonChangeListener = null;
    public static final String cnstNr = "Nr";
    public static final String cnstStar = "Star";
    public static final String cnstDate = "Date";
    public static final String cnstKeyword = "Keyword";
    public static final String cnstTitle = "Title";
    public static final String cnstWebAddress = "WebAddress";

    public static enum COLUMNHEADERS { cnstNr, cnstStar, cnstDate, cnstKeyword, cnstTitle, cnstWebAddress };
    private COLUMNHEADERS columnName;
    private String strSearch;
    private static String java_user_home = System.getProperty("user.home");
    public static final String cnstUserDirOfApp = ".javawebaddressfx";
    //  private  KeyCombination pasteKeyCombination = new KeyCodeCombination(KeyCode.V,KeyCombination.CONTROL_DOWN);

    private EventHandler<KeyEvent> keyEventHanler = null;

    private ChangeListener<Boolean> focuslistener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
        {
            if (newValue.booleanValue()) {
                focusGainedOfControl(observable);
            } else {
                focusLostOfControl(observable);
            }
        }
    };

    private void focusGainedOfControl(ObservableValue stackPane)
    {
        /*
        System.out.println("focus gained ObservableValue " +stackPane.getClass().getName());
        System.out.println("focus gained ObservableValue " +stackPane.getValue());
         */
        if (textFieldWebAddress.isFocused())
            textFieldShow.setText(textFieldWebAddress.getText());
        else
        // System.out.println("textFieldKeyWord=" +textFieldKeyWord.isFocused());
        if (textFieldKeyWord.isFocused())
            textFieldShow.setText(textFieldKeyWord.getText());
        else
        //System.out.println("textFieldTitle=" +textFieldTitle.isFocused());
        if (textFieldTitle.isFocused())
            textFieldShow.setText(textFieldTitle.getText());
    }

    private void focusLostOfControl(ObservableValue stackPane)
    {
        /*
        System.out.println("focus lost ObservableValue " +stackPane.getClass().getName());
        System.out.println("focus lost ObservableValue " +stackPane.getValue());
         */
    }

    public void handleKeyEvent(KeyEvent event) {
        keyEventHanler.handle(event);
    }

    public void pressedButtonDelete() {
       // System.out.println("pressedButtonDelete");
        WebAddresItem webAddresItem = tableViewWebPages.getSelectionModel().getSelectedItem();
        if (webAddresItem != null)
            webAddressRows.remove(webAddresItem);
        saveWebAddressItems();
    }

    @FXML
    protected void pressedButtonGlobalSearch()
    {
        if (textFieldSearch.getText().trim().length()==0)
        {
            tableViewWebPages.setItems(webAddressRows);
            buttonListAll.setSelected(true);
            return;
        }

        buttonListAll.setSelected(false);
        // System.out.println("pressedButtonGlobalSearch");
        // to filter
        final String strSearch = textFieldSearch.getText();
        FilteredList<WebAddresItem> listFiltered = webAddressRows.filtered(
                new Predicate<WebAddresItem>(){
                    public boolean test(WebAddresItem t){
                        return t.toString().contains(strSearch);
                    }
                });
        tableViewWebPages.setItems(listFiltered);
    }

    @FXML
    protected void pressedButtonSearch()
    {
    //    System.out.println("pressedButtonSearch");
        /*
        if (textFieldSearch.getText().trim().length()==0)
        {
            tableViewWebPages.setItems(webAddressRows);
            buttonListAll.setSelected(true);
            return;
        }
         */

        FilteredList<WebAddresItem> listFiltered = null;
        // System.out.println("pressedButtonGlobalSearch");
        // to filter

        /*
        break;
        case "cnstDate":
        ret = item.dateProperty().toString().contains(search);
        break;
        case "cnstWebAddress":
        ret = item.webaddressProperty().toString().contains(search);
        break;
        case "cnstKeyword":
        ret = item.keywordProperty().toString().contains(search);
        break;
        case "cnstStar":
        ret = item.starProperty().toString().contains(search);
        break;
        case "cnstTitle":
        ret = item.titleProperty().toString().contains(search);
        break;
        */

        buttonListAll.setSelected(false);

        final String strSearch = textFieldSearch.getText();
        columnName = COLUMNHEADERS.cnstWebAddress;
        if (radioButtonNr.isSelected()) {
            columnName = COLUMNHEADERS.cnstWebAddress;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                           return new Integer(t.getOrder()).toString().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonTitle.isSelected()) {
            columnName = COLUMNHEADERS.cnstTitle;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return t.getTitle().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonStar.isSelected()) {
            columnName = COLUMNHEADERS.cnstStar;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            if (strSearch == null || strSearch.trim().length()==0)
                                return t.getStar() == null || t.getStar().isBlank() || t.getStar().isEmpty();
                            return t.getStar() != null && t.getStar().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonWebAddress.isSelected()) {
            columnName = COLUMNHEADERS.cnstNr;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return t.getWebaddress() != null && t.getWebaddress().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonDate.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return t.getDate() != null && t.getDate().contains(strSearch);
                        }
                    });
        }
       else
        if (radioButtonKeyWord.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return t.getKeyword() != null && t.getKeyword().contains(strSearch);
                        }
                    });
        }
        else
        if (radioButtonBookMark.isSelected()) {
            columnName = COLUMNHEADERS.cnstDate;
            listFiltered = webAddressRows.filtered(
                    new Predicate<WebAddresItem>(){
                        public boolean test(WebAddresItem t){
                            return new Integer(t.getBookmark()).toString().contains(strSearch);
                        }
                    });
        }

        /*
        FilteredList<WebAddresItem> listFiltered = webAddressRows.filtered(
                new Predicate<WebAddresItem>(){
                    public boolean test(WebAddresItem t){
                        return columnContains(columnName, strSearch, t);
                    }
                });
         */
        tableViewWebPages.setItems(listFiltered);
    }


    private boolean columnContains(WebPagesController.COLUMNHEADERS columnName,
                                   String search, WebAddresItem item)
    {
        boolean ret = false;
        if (columnName != null && columnName.toString().trim().length()>0
                && search != null && search.trim().length()>0)
        {
            System.out.println("'" +columnName +"'");
            switch (columnName.toString())
            {
                case "cnstNr":
                    ret = item.orderProperty().toString().contains(search);
                    break;
                case "cnstDate":
                    ret = item.dateProperty().toString().contains(search);
                    break;
                case "cnstWebAddress":
                    ret = item.webaddressProperty().toString().contains(search);
                    break;
                case "cnstKeyword":
                    ret = item.keywordProperty().toString().contains(search);
                    break;
                case "cnstStar":
                    ret = item.starProperty().toString().contains(search);
                    break;
                case "cnstTitle":
                    ret = item.titleProperty().toString().contains(search);
                    break;
            }
        }
        return ret;
    }

    @FXML
    public void initialize() {

        buttonListAll.setSelected(true);

        textFieldWebAddress.focusedProperty().addListener(focuslistener);
        textFieldKeyWord.focusedProperty().addListener(focuslistener);
        textFieldTitle.focusedProperty().addListener(focuslistener);

        radioButtonChangeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (radioButtonGlobal.isSelected()) {
                    buttonGlobalSearch.setDisable(false);
                    buttonSearch.setDisable(true);
                }
                else
                if (newValue) {
                    buttonGlobalSearch.setDisable(true);
                    buttonSearch.setDisable(false);
                }
            }
        };

        radioButtonGlobal.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonDate.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonNr.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonKeyWord.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonStar.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonTitle.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonWebAddress.selectedProperty().addListener(radioButtonChangeListener);
        radioButtonBookMark.selectedProperty().addListener(radioButtonChangeListener);

        buttonGlobalSearch.setDisable(false);
        buttonSearch.setDisable(true);

        /*
        labelSearchDate.setFocusTraversable(true);
        labelSearchStar.setFocusTraversable(true);
        labelSearchKeyWord.setFocusTraversable(true);
        labelSearchTitle.setFocusTraversable(true);
         */

        textFieldShow.setStyle("-fx-font-weight: bold");
        tableViewWebPages.setStyle("-fx-font-weight: bold");
        textFieldTitle.setStyle("-fx-font-weight: bold");
        textFieldKeyWord.setStyle("-fx-font-weight: bold");
        textFieldWebAddress.setStyle("-fx-font-weight: bold");
        labelOrder.setStyle("-fx-font-weight: bold");
        labelDate.setStyle("-fx-font-weight: bold");
        textFieldSearch.setStyle("-fx-font-weight: bold");

        tableColumnNr.setCellValueFactory(new PropertyValueFactory("order"));
        tableColumnNr.setStyle( "-fx-alignment: CENTER-RIGHT;");
        tableColumnDate.setCellValueFactory(new PropertyValueFactory("date"));
        tableColumnDate.setStyle( "-fx-alignment: CENTER-RIGHT;");

        tableColumnStar.setCellValueFactory(new PropertyValueFactory("star"));
        tableColumnStar.setStyle( "-fx-alignment: CENTER-RIGHT;");
        tableColumnWebAddress.setCellValueFactory(new PropertyValueFactory("webaddress"));
        tableColumnKeyWord.setCellValueFactory(new PropertyValueFactory("keyword"));
        tableColumnTitle.setCellValueFactory(new PropertyValueFactory("title"));
        tableColumnBookMk.setCellValueFactory(new PropertyValueFactory("bookmark"));
        tableColumnBookMk.setStyle( "-fx-alignment: CENTER;");
        /*
        tableViewWebPages.setStyle(".table-row-cell:selected .tissue-cell {\n" +
                "    -fx-background-color: #F5AD11;\n" +
                "    -fx-text-fill: white;\n" +
                "}");
        */
        /*lor: steelblue;\n" +
                "            -fx-text-background-color: red;\n" +
                "        }\n");
         */

        tableViewWebPages.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
               editSelectedWebAddress(newSelection);
            }
        });

        tableViewWebPages.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double clicked");
                        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
                        if (item != null)
                            loadWebPageIntoWebView(item);
                    }
                }
            }
        });

       //  tableViewWebPages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
     // tableViewWebPages.getSelectionModel().setCellSelectionEnabled(true);

        tableViewWebPages.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.getTableColumn() != null){
              //  tableViewWebPages.getSelectionModel().selectRange(0, newVal.getTableColumn(), tableViewWebPages.getItems().size(), newVal.getTableColumn());
                /*
                System.out.println("Selected TableColumn: "+ newVal.getTableColumn().getText());
                System.out.println("Selected column index: "+ newVal.getColumn());
                 */
            }
        });

        // tableColumnWebAddress.setStyle( "-fx-alignment: CENTER-RIGHT;");

        comboStar.getItems().addAll(
                "*",
                "**",
                "***",
                "****",
                "*****"
        );

        tableViewWebPages.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                Clipboard cb = Clipboard.getSystemClipboard();
                boolean success = false;
                if (cb.hasString()) {
                    System.out.println("Dropped: " +cb.getString());
                    System.out.println("target: " + event.getTarget().toString());
                    handlePossibleUrl(cb.getString());
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

       //
        // gridPaneWebPages.onKeyPressedProperty(pasteKeyCombination);
        keyEventHanler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.V && event.isControlDown()) {
                    Clipboard clipboard = Dragboard.getSystemClipboard();
                    boolean success = false;
                    if (clipboard.hasString()) {
                        System.out.println("clibboard: " + clipboard.getString());
                        System.out.println("target: " + event.getTarget());
                        Control control = (Control)event.getTarget();
                        System.out.println("id: " + control.getId());
                        handlePossibleUrl(clipboard.getString());
                        success = true;
                    }
                    event.consume();
                }
            }
        };

        // scene.setOnKeyPressed(keyEventHanler);

        /*
        paneWebPages.setOnKeyPressed(keyEventHanler);
        paneWebPages.setFocusTraversable(true);
        gridPaneWebPages.setOnKeyPressed(keyEventHanler);
        gridPaneWebPages.setFocusTraversable(true);
        titledPaneWebPage.setOnKeyPressed(keyEventHanler);
        splitPanelWebPages.setOnKeyPressed(keyEventHanler);
        titledPaneWebPage.setFocusTraversable(true);
         */
        tableViewWebPages.setItems(webAddressRows);
        initGridPane();
        readWebAddressListFromFile();
    }

    private void loadWebPageIntoWebView(WebAddresItem item)
    {
        if (item == null)
            return;
        String webAddress = item.getWebaddress();
        WebEngine engine = webView.getEngine();
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    engine.load(webAddress);
                }catch (Exception e){
                    labelMsg.setText("Error: " +e.getMessage());
                    engine.load(null);
                }
            }
        });
    }

    private void editSelectedWebAddress(WebAddresItem newSelection)
    {
        textFieldShow.setText("");
        labelDate.setText(""+newSelection.getDate());
        labelOrder.setText(""+newSelection.getOrder());
        textFieldKeyWord.setText(""+newSelection.getKeyword());
        String start = newSelection.getStar();
        boolean bNullValue = false;
        if (start == null || start.trim().length()==0)
            bNullValue = true;
        if (!bNullValue) {
            int iStar = start.trim().length();
            if (iStar > 0)
                comboStar.getSelectionModel().select(iStar - 1);
            else
                comboStar.setValue(null);
        }
        else
            comboStar.setValue(null);
        textFieldTitle.setText(newSelection.getTitle());
        textFieldWebAddress.setText(""+newSelection.getWebaddress());
        buttonBookMark.setSelected(newSelection.getBookmark()!=0);
    }

    private void initGridPane()
    {
        /*
        int iOrder = 1;
        int iStars = 5;
        String keyWords = "keyworda1, keyworda2";
        String webAddress = "http://webaddress.com";
        String tile = "tile1, tile2";

      //  WebAddressRow row1 = new WebAddressRow(iOrder, iStars, keyWords, webAddress);
        // int iNr, int iSTar, String strDAte, String webAddress
        WebAddresItem item = new WebAddresItem(iOrder, iStars, getTodayString(), keyWords, webAddress, tile);
        int max = 6;
        String itemKWord, title;
        for(int i = 1; i < max; i++)
        {
            iOrder = i;
            iStars = i;
            itemKWord = "keyworda1_" +i +", " +"keyworda2_" +i;
            webAddress = "http://webaddress.com_" +i +", " +"http://webaddress.com_" +i;
            title = "title1_" +i +", " +"title2_" +i;
            item = new WebAddresItem(iOrder, iStars, getTodayString(), itemKWord, webAddress, title);
            webAddressRows.add(item);
        }
        // addGridPaneRow(row1);
         */
    }

    private String getTodayString()
    {
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    private void addGridPaneRow(WebAddressRow row)
    {
        // webAddressRows.add(row);
        /*
        gridPaneWebPages.add(row.getOrderNumber(), 0, iStartRow); // column=1 row=0
        gridPaneWebPages.add(row.getDate(), 1, iStartRow);
        gridPaneWebPages.add(row.getStars(), 2, iStartRow);
        gridPaneWebPages.add(row.getKeyrords(), 3, iStartRow);
        gridPaneWebPages.add(row.getWebadress(), 4, iStartRow); // column=1 row=0
         */
    }

    @FXML
    private void pressedButtonHelp()
    {
        System.out.println("pressedButtonHelp");
    }

    private void handlePossibleUrl(String fromClibBoard)
    {
        if (fromClibBoard == null || fromClibBoard.trim().length()==0)
            return;
        String tryUrl = fromClibBoard;
        try {
            try {
                new URL(tryUrl);
            }catch (MalformedURLException mfe){
                tryUrl = "https://" +fromClibBoard;
                try {
                    new URL(tryUrl);
                }catch (MalformedURLException mfe2){
                    tryUrl = "http://" +fromClibBoard;
                    try {
                        new URL(tryUrl);
                    }catch (MalformedURLException mfe3){
                        if (File.separatorChar == '\\')
                            tryUrl = "file:///" +fromClibBoard;
                        else
                            tryUrl = "file://" +fromClibBoard;
                        try {
                            new URL(tryUrl);
                        }catch (MalformedURLException mfe4) {
                            throw mfe4;
                        }
                    }
                }
            }
            addURLIntoGridWebPages(tryUrl);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            String msg = "Text paste is not web address: " +malformedURLException.getMessage();
            System.out.println(msg);
            Platform.runLater(new Runnable() {
                public void run() {
                    labelMsg.setText(msg);
                }
            });
            return;
        }
    }

    private boolean existingURLINwebAddressRows(String weburl)
    {
        boolean ret = false;
        if (weburl == null || weburl.trim().length()==0)
            ret = true;
        else
        {
            String webAddress = null;
            for (WebAddresItem item : webAddressRows)
            {
                if (item == null)
                    continue;
                webAddress = item.getWebaddress();
                if (webAddress != null && webAddress.equals(weburl)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    private int getMaxOrderValueFromRows()
    {
        int ret = 0;
        for(WebAddresItem item : webAddressRows)
        {
            if (item == null)
                continue;
            if (item.getOrder() > ret)
                ret = item.getOrder();
        }
        return ret +1;
    }
    private void addURLIntoGridWebPages(String fromClibBoard)
    {
        if (existingURLINwebAddressRows(fromClibBoard))
        {
            labelMsg.setText("The same ulr is existing allready! " +fromClibBoard);
            return;
        }

        int iOrder = webAddressRows.size() +1;
        if (iOrder > 1)
            iOrder = getMaxOrderValueFromRows();
        int iStars = 0;
        String itemKWord = "";
        String strWebPage = null;
        String title = "";
        try {
            WebEngine webEngine = webView.getEngine();
            webEngine.setJavaScriptEnabled(true);
            webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Trident/7.0; rv:11.0) like Gecko");
            // webEngine.load(fromClibBoard);
            /*
            File f = new File("C:\\Java\\project\\javafx\\javafxplayer\\src\\main\\resources\\com\\metait\\javafxplayer\\help\\help.html");
            if (f.exists())
             */
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        System.out.println("webengine->");
                        // webEngine.load(f.toURI().toString());
                        webEngine.load(fromClibBoard);
                    }catch (Exception e2){
                        System.out.println("webengine-> e2");
                        e2.printStackTrace();
                        labelMsg.setText(e2.getMessage());
                    }
                }
            });
            Url2String url2String = new Url2String();
            // webEngine.loadContent(strWebPage);
            URL url = null;
            try {
                url = new URL(fromClibBoard);
                strWebPage = url2String.getStringFromUrl(url);
                String unknowCharacterSetName = url2String.getUnknowCharacterSetName();
                if (unknowCharacterSetName != null)
                    System.err.println("Possible error in url2String: " +unknowCharacterSetName);
                int iConfidence = url2String.getiConfidence();
                if (iConfidence != -1)
                    System.err.println("iConfidence in url2String: " +iConfidence);
            }catch (Exception e){
                // url = new URL(null, fromClibBoard, new sun.net.www.protocol.https.Handler());
                try {
                url = new URL(fromClibBoard);
                strWebPage = url2String.getStringFromUrl(url.toURI().toURL());
                }catch (Exception e3){
                    if (fromClibBoard.startsWith("file:")) {
                        String tryUrl = fromClibBoard.replace("file:///","").replace("file://","");
                        File f2 = new File(tryUrl);
                        if (f2.exists()) {
                            url = f2.toURI().toURL();
                            strWebPage = url2String.inputStreamToString(new FileInputStream(f2));
                        }
                        else
                            return;
                    }
                }
            }
            itemKWord = getKeyWordsFromWebPage(strWebPage);
            title = getTitleFromWebPage(strWebPage);
        }catch (Exception e){
            e.printStackTrace();
            labelMsg.setText("Error: " +e.getMessage());
        }
        WebAddresItem item = new WebAddresItem(iOrder, iStars, getTodayString(), itemKWord, fromClibBoard, title, 0);
        webAddressRows.add(item);
        saveWebAddressItems();
    }

    private String getTitleFromWebPage(String strWebPage)
    {
        String ret = "";
        boolean bTitleFounded = false;
        if (strWebPage != null && strWebPage.trim().length()>0)
        {
            ret = getTextXmlBlockOf("title", strWebPage);
            if (ret == null || ret.trim().length()==0)
                ret = getTextXmlBlockOf("TITLE", strWebPage);
            if (ret == null || ret.trim().length()==0)
            {
                ret = getTextXmlBlockOf("h1", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("H1", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("h2", strWebPage);
                if (ret == null || ret.trim().length()==0)
                    ret = getTextXmlBlockOf("H2", strWebPage);
            }
            if (ret != null && ret.contains("<") && ret.contains(">"))
                ret = ret.replaceAll("\\<.*?\\>", "")
                        .replaceAll("\\</.*?\\>", "");
            if (ret != null)
                ret = ret.replaceAll("\n"," ").replaceAll("\\s+"," ");
        }
        return ret;
    }

    private String getTextXmlBlockOf(String h1, String strWebPage)
    {
        String ret = ";";
        boolean bTitleFounded = false;
        Pattern pattern = Pattern.compile("<" +h1 +"\\s*.*?>");
        Matcher matcher = pattern.matcher(strWebPage);
        if (matcher.find())
        {
            int iStart = matcher.start();
            int iEnd = matcher.end();
            String strRest = strWebPage.substring(iEnd);
            int ind = strRest.indexOf("</" +h1 +">");
            if (ind > -1) {
                String value = strRest.substring(0, ind);
                if (value != null && value.contains("<") && value.contains(">"))
                    value = value.replaceAll("\\<.*?\\>", "")
                            .replaceAll("\\</.*?\\>", "");
                if (value != null)
                    value = value.replaceAll("\n"," ").replaceAll("\\s+"," ");
                ret = value;
            }
        }
        return ret;
    }

    private String getKeywordsFromContent(String strContent)
    {
        if (strContent == null || strContent.trim().length()==0)
            return "";
        Pattern pattern2 = Pattern.compile("content\\s*=\\s*\"(.*)\"");
        Matcher matcher2 = pattern2.matcher(strContent);
        if (matcher2.find())
        {
            String value1 = matcher2.group(1);
            return value1;
        }
        return "";
    }

    private String getKeyWordsFromWebPage(String strWebPage)
    {
        String ret = "";
        if (strWebPage != null && strWebPage.trim().length()>0)
        {
            Pattern pattern = Pattern.compile("<meta\\s+(.*?)name\\s*=\\s*\"keywords\"(.*?)/*>");
            Matcher matcher = pattern.matcher(strWebPage);
            if (matcher.find())
            {
                String value1 = matcher.group(1);
                String value2 = matcher.group(2);
                int ind1 = -1, ind2 = -1;
                if (value1 != null)
                {
                    ind1 = value1.indexOf("content");
                    if (ind1 > -1)
                    {
                        ret = getKeywordsFromContent(value1);
                    }
                }
                if (ind1 == -1 && value2 != null)
                {
                    ind2 = value2.indexOf("content");
                    if (ind2 > -1)
                    {
                        ret = getKeywordsFromContent(value2);
                    }
                }

                if (ret != null && ret.contains("<") && ret.contains(">"))
                    ret = ret.replaceAll("\\<.*?\\>", "")
                            .replaceAll("\\</.*?\\>", "");
                if (ret != null)
                    ret = ret.replaceAll("\n"," ").replaceAll("\\s+"," ");
            }
        }
        return ret;
    }

    private File getJsonRowFile()
    {
        File rowFile = null;
        String userHome = java_user_home;
        File userDir = new File(userHome);
        if (!userDir.exists())
            userDir = new File(".");
        File appDir = new File(userDir +File.separator +cnstUserDirOfApp);
        if (!appDir.exists())
            if (!appDir.mkdir())
            {
                labelMsg.setText("Cannot create dir: " +appDir.getAbsolutePath());
                return null;
            }
        rowFile = new File(appDir +File.separator +"webaddressrows.json");
        return rowFile;
    }

    private void readWebAddressListFromFile()
    {
        File rowFile = getJsonRowFile();
        if (rowFile == null)
            return;
        if (!rowFile.exists())
            return;
        Reader reader = null;
        try {
            Path path = Paths.get(rowFile.getAbsolutePath());
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            JSONWebAddress[] jsonWebAddresItems = gson.fromJson(reader, JSONWebAddress[].class);
            for(JSONWebAddress jsonitem: jsonWebAddresItems)
            {
                webAddressRows.add(new WebAddresItem(jsonitem));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            labelMsg.setText("Read file " + rowFile.getAbsolutePath() + " error: " + ioe.getMessage());
        } catch (Exception norm_e) {
            norm_e.printStackTrace();
            labelMsg.setText("Read file " + rowFile.getAbsolutePath() + " error: " + norm_e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                    labelMsg.setText("Closing file " + rowFile.getAbsolutePath() + " error: " + ioe2.getMessage());
                }
            }
        }
    }

    private void saveWebAddressItems() {
     //   StringBuffer sb = new StringBuffer();
        Gson gson = null;
        String json = null;
        gson = new Gson();
        /*
        JSONHolderFowWebAddressList holder = new JSONHolderFowWebAddressList();
        for (WebAddresItem item : webAddressRows) {
            // json = gson.toJson(item);
            holder.webAddresItems.add(item);
            // sb.append(json);
        }
         */

        File rowFile = getJsonRowFile();
        if (rowFile == null)
            return;
        Writer writer = null;
        try {
            /*
                outputStream = new FileOutputStream(rowFile);
                byte[] bytes = sb.toString().getBytes();
                outputStream.write(bytes);
                // writer.close();
             */
                Path path = Paths.get(rowFile.getAbsolutePath());
                StandardOpenOption writeOption = StandardOpenOption.TRUNCATE_EXISTING;
                if (!rowFile.exists())
                    writeOption = StandardOpenOption.CREATE;
                writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, writeOption);
                gson = new Gson();
                List<JSONWebAddress> jsonList = new ArrayList<>();
                for(WebAddresItem item : webAddressRows)
                {
                    jsonList.add(new JSONWebAddress(item));
                }
                gson.toJson(jsonList, writer);
        } catch (IOException ioe) {
                ioe.printStackTrace();
                labelMsg.setText("Write file " + rowFile.getAbsolutePath() + " error: " + ioe.getMessage());
        } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ioe2) {
                        ioe2.printStackTrace();
                        labelMsg.setText("Closing file " + rowFile.getAbsolutePath() + " error: " + ioe2.getMessage());
                    }
                }
        }
    }

    @FXML
    protected void pressedButtonSave()
    {
        // System.out.println("pressedButtonSave");
        int iSelectedInd = tableViewWebPages.getSelectionModel().getFocusedIndex();
        if (iSelectedInd > -1)
        {
            WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
            if (item != null)
            {
                item.setStar(comboStar.getValue());
                item.setKeyword(textFieldKeyWord.getText());
                item.setTitle(textFieldTitle.getText());
                try {
                    new URL(textFieldWebAddress.getText());
                    item.setWebaddress(textFieldWebAddress.getText());
                    WebAddresItem searchItem = null;
                    int i = 0;
                    for (WebAddresItem item2 : webAddressRows)
                    {
                        if (item2 == null)
                        {
                            i++;
                            continue;
                        }
                        if (item2.getOrder() == item.getOrder()) {
                            searchItem = item2;
                            break;
                        }
                        i++;
                    }
                    if (searchItem != null)
                        webAddressRows.set(i, item);
                    saveWebAddressItems();
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                    String msg = "A wrong web adress value: " +malformedURLException.getMessage();
                    System.out.println(msg);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            labelMsg.setText(msg);
                        }
                    });
                    return;
                }
            }
        }
    }

    @FXML
    protected void pressedButtonOpenBrowser()
    {
     //   System.out.println("buttonOpenBrowser");
        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
        if (item != null)
        {
            String strUrl = item.getWebaddress();
            try {
                Desktop.getDesktop().browse(new URL(strUrl).toURI());
            } catch (IOException e) {
                e.printStackTrace();
                labelMsg.setText("Error: " +e.getMessage());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                labelMsg.setText("Error: " +e.getMessage());
            }
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        // welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void pressedButtonListAll()
    {
        tableViewWebPages.setItems(webAddressRows);
        buttonListAll.setSelected(true);
    }

    @FXML
    protected void pressedButtonBookMark()
    {
     //   System.out.println("pressedButtonBookMark");
        WebAddresItem item = tableViewWebPages.getSelectionModel().getSelectedItem();
        if (item != null)
        {
            boolean bModified = false;
            if (buttonBookMark.isSelected() && item.getBookmark() == 0) {
                item.setBookmark(1);
                bModified = true;
            }
            else
            if (!buttonBookMark.isSelected() && item.getBookmark() != 0) {
                item.setBookmark(0);
                bModified = true;
            }
            if (bModified)
            {
                WebAddresItem searchItem = null;
                int i = 0;
                for (WebAddresItem item2 : webAddressRows)
                {
                    if (item2 == null)
                    {
                        i++;
                        continue;
                    }
                    if (item2.getOrder() == item.getOrder()) {
                        searchItem = item2;
                        break;
                    }
                    i++;
                }
                if (searchItem != null) {
                    webAddressRows.set(i, item);
                    saveWebAddressItems();
                }
            }
        }
    }
}