package com.rebiekong.tools.file.catalog;

import com.rebiekong.tools.file.catalog.model.TranslateJob;
import com.rebiekong.tools.file.catalog.model.TranslateRule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {

    @FXML
    private TableView<TranslateRule> ruleTable;
    @FXML
    private Button executeBtn;
    @FXML
    private Button addRuleBtn;
    @FXML
    private TableColumn<TranslateRule, String> srcCol;
    @FXML
    private TableColumn<TranslateRule, String> ruleCol;
    @FXML
    private TableColumn<TranslateRule, String> dstCol;
    @FXML
    private TableColumn<TranslateRule, Boolean> isMoveCol;
    @FXML
    private Label fileCntLabel;
    //    @FXML
//    private TableColumn<TranslateRule, String> saveBtnCol;
    @FXML
    private ListView<TranslateJob> previewList;

    private ObservableList<TranslateRule> personData = FXCollections.observableArrayList();
    private ObservableList<TranslateJob> previewListData = FXCollections.observableArrayList();

    private Stage fileStage = new Stage();


    private void updatePreviewListData() {
        List<TranslateJob> a = personData.stream().flatMap(translateRule -> {
            File srcDir = new File(translateRule.getSrc());
            File dstDir = new File(translateRule.getDst());
            if (!srcDir.exists() || !dstDir.exists()) {
                return Stream.empty();
            }
            File[] files = srcDir.listFiles((dir, name) -> name.matches(translateRule.getContainStr()));
            if (files == null) {
                return Stream.empty();
            }

            return Arrays.stream(files).map(file -> {
                TranslateJob translateJob = new TranslateJob();
                translateJob.setSrc(file.getAbsolutePath());
                translateJob.setMoved(translateRule.getIsMove());
                translateJob.setDst(dstDir.getPath());
                return translateJob;
            });
        }).collect(Collectors.toList());

        previewListData.clear();
        previewListData.addAll(a);
        fileCntLabel.setText(String.format("文件数：%d", previewListData.size()));
    }

    @FXML
    private void initialize() {
        srcCol.setCellValueFactory(cellData -> cellData.getValue().srcProperty());
        ruleCol.setCellValueFactory(cellData -> cellData.getValue().containStrProperty());
        ruleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ruleCol.setOnEditCommit(
                (TableColumn.CellEditEvent<TranslateRule, String> t) -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setContainStr(t.getNewValue());
                    updatePreviewListData();
                });

        dstCol.setCellValueFactory(cellData -> cellData.getValue().dstProperty());

        addRuleBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                personData.add(new TranslateRule("来源地址", "包含字符串", "目的地", true));
            }
        });


        executeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updatePreviewListData();
            }
        });


        ruleTable.setOnMouseClicked(event -> {
            if (event.getTarget() instanceof TableCell) {
                TableCell<TranslateRule, String> a = (TableCell<TranslateRule, String>) event.getTarget();
                String cid = a.getTableColumn().getId();
                if (("srcCol".equals(cid) || "dstCol".equals(cid)) && a.getIndex() < personData.size()) {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File file = directoryChooser.showDialog(fileStage);
                    if (file != null) {
                        String path = file.getPath();
                        switch (a.getTableColumn().getId()) {
                            case "srcCol":
                                personData.get(a.getIndex()).setSrc(path);
                                break;
                            case "dstCol":
                                personData.get(a.getIndex()).setDst(path);
                                break;
                            default:
                        }
                        updatePreviewListData();
                    }

                }
            }

        });


        isMoveCol.setCellFactory((col) -> new TableCell<TranslateRule, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    CheckBox checkBox = new CheckBox();
                    checkBox.fire();
                    this.setGraphic(checkBox);
                    checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                        personData.get(this.getIndex()).setIsMove(newVal);
                        updatePreviewListData();
                    });
                }
            }

        });

//        saveBtnCol.setCellFactory((col) -> new TableCell<TranslateRule, String>() {
//
//            @Override
//            public void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                this.setText(null);
//                this.setGraphic(null);
//
//                if (!empty) {
//                    Button delBtn = new Button("save");
//                    this.setGraphic(delBtn);
//                    delBtn.setOnMouseClicked((me) -> {
//                        TranslateRule clickedStu = this.getTableView().getItems().get(this.getIndex());
//                        System.out.println("删除 " + clickedStu.getSrc() + " 的记录");
//                    });
//                }
//            }
//
//        });

        //绑定数据到TableView
        Callback<ListView<TranslateJob>, ListCell<TranslateJob>> call = TextFieldListCell.forListView(new StringConverter<TranslateJob>() {
            @Override
            public String toString(TranslateJob object) {
                if (object.isMoved()) {
                    return String.format("%s --> %s", object.getSrc(), object.getDst());
                } else {
                    return String.format("%s ==> %s", object.getSrc(), object.getDst());
                }
            }

            @Override
            public TranslateJob fromString(String string) {
                if (string.contains("-->")) {
                    String[] s = string.split("-->", 2);
                    TranslateJob translateJob = new TranslateJob();
                    translateJob.setMoved(true);
                    translateJob.setSrc(s[0]);
                    translateJob.setDst(s[1]);
                } else if (string.contains("==>")) {
                    String[] s = string.split("==>", 2);
                    TranslateJob translateJob = new TranslateJob();
                    translateJob.setMoved(true);
                    translateJob.setSrc(s[0]);
                    translateJob.setDst(s[1]);
                } else {
                    return null;
                }
                return null;
            }
        });
        previewList.setCellFactory(call);

        ruleTable.setItems(personData);
        previewList.setItems(previewListData);
        ruleTable.setPlaceholder(new Label("EMPTY,CLICK ADD RULE"));
        previewList.setPlaceholder(new Label("NO FILE TO MOVE"));
        updatePreviewListData();

    }
}
