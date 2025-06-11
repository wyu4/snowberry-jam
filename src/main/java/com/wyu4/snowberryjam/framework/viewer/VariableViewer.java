package com.wyu4.snowberryjam.framework.viewer;

import com.wyu4.snowberryjam.compiler.LocalStorage;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class VariableViewer extends TableView<VariableListener> implements Viewer {
    private final TableColumn<VariableListener, String> nameColumn = new TableColumn<>("Name");
    private final TableColumn<VariableListener, String> typeColumn = new TableColumn<>("Type");
    private final TableColumn<VariableListener, String> valueColumn = new TableColumn<>("Value");
    private final ObservableList<VariableListener> variables = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    public VariableViewer() {
        super();

        refresh();
        nameColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().getTypeProperty());
        valueColumn.setCellValueFactory(data -> {
            ObjectProperty<Object> value = data.getValue().getValueProperty();
            return Bindings.createStringBinding(() -> {
                Object raw = value.get();
                if (raw instanceof Object[]) {
                    return Arrays.toString((Object[]) raw);
                }
                return String.valueOf(raw);
            }, value);
        });

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        setEditable(false);

        nameColumn.setResizable(false);
        typeColumn.setResizable(false);
        valueColumn.setResizable(false);

        nameColumn.prefWidthProperty().bind(widthProperty().multiply(0.2));
        typeColumn.prefWidthProperty().bind(widthProperty().multiply(0.2));
        valueColumn.prefWidthProperty().bind(widthProperty().multiply(0.6));

        getColumns().addAll(nameColumn, typeColumn, valueColumn);
        setItems(variables);
    }

    @Override
    public void refresh() {
        Platform.runLater(() -> {
            variables.clear();
            LocalStorage.getVariableNames().forEach(name -> variables.add(new VariableListener(name)));
        });
    }
}

class VariableListener {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final ObjectProperty<Object> value = new SimpleObjectProperty<>();

    public VariableListener(String name) {
        this.name.set(name);
        value.addListener((evt, old, current) -> {
            if (current instanceof Double) {
                type.set("Number");
            } else if (current instanceof String) {
                type.set("String");
            } else if (current instanceof Boolean) {
                type.set("Boolean");
            } else if (current instanceof Object[]) {
                type.set("Array");
            } else {
                type.set("Unknown");
            }
        });
        try {
            Platform.runLater(() -> value.set(LocalStorage.getRaw(name)));
        } catch (NullPointerException ignore) {
        }
        LocalStorage.addVariableListener(name, raw -> Platform.runLater(() -> value.set(raw)));
    }

    public ObjectProperty<Object> getValueProperty() {
        return value;
    }

    public StringProperty getTypeProperty() {
        return type;
    }

    public StringProperty getNameProperty() {
        return name;
    }
}