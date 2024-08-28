package com.messenger.views.checkbox;

public class OneCheckPro {

    private static OneCheck checkChecks;

    static void change(OneCheck checkBox){
        if(checkChecks!=null)checkChecks.setChecked(false);
        checkChecks = checkBox;
    }

    public static void clear(){
        checkChecks = null;
    }

    public static String getTextCheck(){
        if(checkChecks!=null)
        return checkChecks.getText().toString();
        else return "";
    }

}
