package org.litespring.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConstructorArgument {
    private final List<ValueHolder> argumentValues = new LinkedList<ValueHolder>();

    public ConstructorArgument() {

    }

    public void addArgumentValue(ValueHolder valueHolder) {
        argumentValues.add(valueHolder);
    }

    public List<ValueHolder> getArgumentValues() {
        return Collections.unmodifiableList(this.argumentValues);
    }

    public int getArgumentCount() {
        return argumentValues.size();
    }

    public boolean isEmpty() {
        return argumentValues.isEmpty();
    }

    public static class ValueHolder {
        private Object value;
        private String type;
        private String name;


        public ValueHolder(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
