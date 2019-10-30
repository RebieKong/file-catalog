package com.rebiekong.tools.file.catalog;

import com.rebiekong.tools.file.catalog.model.TranslateRule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;



public class Controller {

    @FXML
    private TableView<TranslateRule> ruleTable;
    @FXML
    private TableColumn<TranslateRule, String> srcCol;
    @FXML
    private TableColumn<TranslateRule, String> ruleCol;
    @FXML
    private TableColumn<TranslateRule, String> dstCol;
    @FXML
    private TableColumn<TranslateRule, Boolean> isMoveCol;
    @FXML
    private TableColumn<TranslateRule, String> saveBtnCol;

    private ObservableList<TranslateRule> personData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        srcCol.setCellValueFactory(cellData -> cellData.getValue().srcProperty());
        ruleCol.setCellValueFactory(cellData -> cellData.getValue().containStrProperty());
        dstCol.setCellValueFactory(cellData -> cellData.getValue().dstProperty());

        isMoveCol.setCellFactory((col) -> new TableCell<TranslateRule, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    CheckBox checkBox = new CheckBox();
                    this.setGraphic(checkBox);
                    checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                        if (newVal) {
                            // 添加选中时执行的代码
                            System.out.println("第" + this.getIndex() + "行被选中！");
                            // 获取当前单元格的对象
                            // this.getItem();
                        }

                    });
                }
            }

        });

        saveBtnCol.setCellFactory((col) -> new TableCell<TranslateRule, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    Button delBtn = new Button("save");
                    this.setGraphic(delBtn);
                    delBtn.setOnMouseClicked((me) -> {
                        TranslateRule clickedStu = this.getTableView().getItems().get(this.getIndex());
                        System.out.println("删除 " + clickedStu.getSrc() + " 的记录");
                    });
                }
            }

        });

        //绑定数据到TableView
        ruleTable.setItems(personData);

        //添加数据到personData，TableView会自动更新
        personData.add(new TranslateRule("007", "爱谁谁", "???", true));
    }
}
