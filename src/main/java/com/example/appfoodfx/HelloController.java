package com.example.appfoodfx;

import com.example.appfoodfx1.GUI.FoodFormController;
import com.example.appfoodfx1.models.Chocolate;
import com.example.appfoodfx1.models.Cookie;
import com.example.appfoodfx1.models.Food;
import com.example.appfoodfx1.models.Fruit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public TableView mainTable;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    // создали список
    ObservableList<Food> foodList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // заполнили список данными
        foodList.add(new Fruit(100,"Яблоко",true));
        foodList.add(new Chocolate(200,"шоколад Аленка",Chocolate.Type.milk));
        foodList.add(new Cookie(300, "сладкая булочка с Маком", true, true, false));

        // подключили к таблице
        mainTable.setItems(foodList);

        // создаем столбец, указываем что столбец преобразует Food в String,
        // указываем заголовок колонки как "Название"
        TableColumn<Food, String> titleColumn = new TableColumn<>("Название");
        // указываем какое поле брать у модели Food
        // в данном случае title, кстати именно в этих целях необходимо было создать getter и setter для поля title
        //   titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(cellData.getValue().getTitle());
        });

        // тоже самое для калорийности
        TableColumn<Food, String> kkalColumn = new TableColumn<>("Калорийность");
        // kkalColumn.setCellValueFactory(new PropertyValueFactory<>("kkal"));
//v-1
        kkalColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(String.valueOf(cellData.getValue().getKkal()));
        });

        // добавляем столбец с описанием
        TableColumn<Food, String> descriptionColumn = new TableColumn<>("Описание");
        // если хотим что-то более хитрое выводить, то используем лямбда выражение
        descriptionColumn.setCellValueFactory(cellData -> {
            // плюс надо обернуть возвращаемое значение в обертку свойство
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        // добавляем сюда descriptionColumn
        mainTable.getColumns().addAll(titleColumn, kkalColumn, descriptionColumn);
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException, java.io.IOException {
        // эти три строчки создюат форму из fxml файлика
        // в принципе можно было бы обойтись
        // Parent root = FXMLLoader.load(getClass().getResource("FoodForm.fxml"));
        // но дальше вот это разбиение на три строки упростит нам жизнь
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/appfoodfx/FoodForm.fxml"));
        Parent root = loader.load();

        // ну а тут создаем новое окно
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        // указываем что оно модальное
        stage.initModality(Modality.WINDOW_MODAL);
        // указываем что оно должно блокировать главное окно
        // ну если точнее, то окно, на котором мы нажали на кнопку
        stage.initOwner(this.mainTable.getScene().getWindow());

        // открываем окно и ждем пока его закроют
        stage.showAndWait();

        // вытаскиваем контроллер который привязан к форме
        FoodFormController controller = loader.getController();
        // проверяем что наали кнопку save
        if (controller.getModalResult()) {
            // собираем еду с формы
            Food newFood = controller.getFood();
            // добавляем в список
            this.foodList.add(newFood);
        }

    }

    public void onEditClick(ActionEvent actionEvent) throws IOException, java.io.IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/appfoodfx/FoodForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.mainTable.getScene().getWindow());

        stage.showAndWait();

        // передаем выбранную еду
        FoodFormController controller = loader.getController();
        controller.setFood((Food) this.mainTable.getSelectionModel().getSelectedItem());

        stage.showAndWait();

        // если нажали кнопку сохранить
        if (controller.getModalResult()) {
            // узнаем индекс выбранной в таблице строки
            int index = this.mainTable.getSelectionModel().getSelectedIndex();
            // подменяем строку в таблице данными на форме
            this.mainTable.getItems().set(index, controller.getFood());
        }
    }

    public void onDeleteClick(ActionEvent actionEvent) {
        // берем выбранную на форме еду
        Food food = (Food) this.mainTable.getSelectionModel().getSelectedItem();

        // выдаем подтверждающее сообщение
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(String.format("Точно удалить %s?", food.getTitle()));

        // если пользователь нажал OK
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            // удаляем строку из таблицы
            this.mainTable.getItems().remove(food);
        }
    }
}