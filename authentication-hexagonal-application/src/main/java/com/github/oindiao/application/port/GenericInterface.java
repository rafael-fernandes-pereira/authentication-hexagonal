package com.github.oindiao.application.port;

import java.util.ArrayList;
import java.util.List;

public interface GenericInterface<Input, Output> {

    Output execute(Input input);

    class Notification {

        private List<String> errors;

        private Notification(){
            this.errors = new ArrayList<>();
        }

        public static Notification create(){
            return new Notification();
        }

        public void addError(String error){
            this.errors.add(error);
        }

        public Boolean hasError(){
            return ! this.errors.isEmpty();
        }

        public List<String> getErrors(){
            return this.errors;
        }

    }
}
