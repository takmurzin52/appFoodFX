package com.example.appfoodfx1.GUI;

import com.example.appfoodfx1.models.Chocolate;
import com.example.appfoodfx1.models.Food;
import com.example.appfoodfx1.models.Fruit;
import com.example.appfoodfx1.models.Cookie;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class FoodFormController  implements Initializable {
    // создаем
    public ChoiceBox cmbFoodType;
    public TextField txtFoodTitle;
    public TextField txtFoodKkal;

    public VBox fruitPane;
    public CheckBox chkIsFresh;

    public HBox chocolatePane;
    public ChoiceBox cmbChocolateType;

    public VBox cookiePane;
    public CheckBox chkWithSugar;
    public CheckBox chkWithPoppy;
    public CheckBox chkWithSesame;

    final String FOOD_FRUIT = "Фрукт";
    final String FOOD_CHOCOLATE = "Шоколадка";
    final String FOOD_COOKIE = "Булочка";

    // добавляем новое поле
    private Boolean modalResult = false;

    // добавил новую функцию
    public void updatePanes(String value) {
        cmbFoodType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.cookiePane.setVisible(newValue.equals(FOOD_COOKIE));
            this.cookiePane.setManaged(newValue.equals(FOOD_COOKIE));
            this.fruitPane.setVisible(newValue.equals(FOOD_FRUIT));
            this.fruitPane.setManaged(newValue.equals(FOOD_FRUIT));
            this.chocolatePane.setVisible(newValue.equals(FOOD_CHOCOLATE));
            this.chocolatePane.setManaged(newValue.equals(FOOD_CHOCOLATE));
        });

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbFoodType.setItems(FXCollections.observableArrayList(
                FOOD_FRUIT,
                FOOD_CHOCOLATE,
                FOOD_COOKIE
        ));

        cmbFoodType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // вызываю новую функцию
            updatePanes((String) newValue);
        });

        // добавляем все три типа шоколада в комобобокс
        cmbChocolateType.getItems().setAll(
                Chocolate.Type.white,
                Chocolate.Type.black,
                Chocolate.Type.milk
        );

        // и используем метод setConverter,
        // чтобы типы объекты рендерились как строки
        cmbChocolateType.setConverter(new StringConverter<Chocolate.Type>() {
            @Override
            public String toString(Chocolate.Type object) {
                if (object == null) {
                    return "";
                }
                switch (object) {
                    case white:
                        return "Белый";
                    case black:
                        return "Черный";
                    case milk:
                        return "Молочный";
                }
                return "";
            }

            @Override
            public Chocolate.Type fromString(String string) {
                // этот метод не трогаем так как наш комбобкос имеет фиксированный набор элементов
                return null;
            }
        });

        // вызываю новую функцию при инициализации формы
        updatePanes("");
    }

    // обработчик нажатия на кнопку Сохранить
    public void onSaveClick(ActionEvent actionEvent) {
        this.modalResult = true; // ставим результат модального окна на true
        // закрываем окно к которому привязана кнопка
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void onCancelClick(ActionEvent actionEvent) {
        this.modalResult = false; // ставим результат модального окна на false
        // закрываем окно к которому привязана кнопка
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }

    // геттер для результата модального окна
    public Boolean getModalResult() {
        return modalResult;
    }

    public void setFood(Food food) {
        // делаем так что если объект редактируется, то нельзя переключать тип
        this.cmbFoodType.setDisable(food != null);
        if (food != null) {
            // ну а тут стандартное заполнение полей в соответствии с переданной едой
            this.txtFoodKkal.setText(String.valueOf(food.getKkal()));
            this.txtFoodTitle.setText(food.getTitle());

            if (food instanceof Fruit) { // если фрукт
                this.cmbFoodType.setValue(FOOD_FRUIT);
                this.chkIsFresh.setSelected(((Fruit) food).isFresh);
            } else if (food instanceof Cookie) { // если булочка
                this.cmbFoodType.setValue(FOOD_COOKIE);
                this.chkWithSugar.setSelected(((Cookie) food).withSugar);
                this.chkWithPoppy.setSelected(((Cookie) food).withPoppy);
                this.chkWithSesame.setSelected(((Cookie) food).withSesame);
            } else if (food instanceof Chocolate) { // если шоколад
                this.cmbFoodType.setValue(FOOD_CHOCOLATE);
                this.cmbChocolateType.setValue(((Chocolate) food).type);
            }
        }
    }

    public Food getFood() {
        Food result = null;
        int kkal = Integer.parseInt(this.txtFoodKkal.getText());
        String title = this.txtFoodTitle.getText();

        switch ((String)this.cmbFoodType.getValue()) {
            case FOOD_CHOCOLATE:
                result = new Chocolate(kkal, title, (Chocolate.Type)this.cmbChocolateType.getValue());
                break;
            case FOOD_COOKIE:
                result = new Cookie(
                        kkal,
                        title,
                        this.chkWithSugar.isSelected(),
                        this.chkWithPoppy.isSelected(),
                        this.chkWithSesame.isSelected()
                );
                break;
            case FOOD_FRUIT:
                result = new Fruit(kkal, title, this.chkIsFresh.isSelected());
                break;
        }
        return result;
    }
}
