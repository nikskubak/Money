package com.fivestar.models.columns;

/**
 * Created by skuba on 15.05.2016.
 */
public interface RecommendationColumns {

    String ID = "id";
    String DESCRIPTION = "description";
    String TYPE = "type";
    String DATE = "date";
    String VIEWED = "viewed";

    String[] COLUMNS = new String[]{ID, DESCRIPTION, TYPE, DATE, VIEWED};
}
