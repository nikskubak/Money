package com.fivestar.models.columns;

/**
 * Created by skuba on 06.03.2016.
 */
public interface TransactionColumns {

    String ID = "id";
    String DESCRIPTION = "description";
    String CATEGORY = "category";
    String MONEY = "money";
    String DATE = "date";
    String TYPE = "type";

    String[] COLUMNS = new String[]{ID, DESCRIPTION, CATEGORY, MONEY, DATE, TYPE};

}
