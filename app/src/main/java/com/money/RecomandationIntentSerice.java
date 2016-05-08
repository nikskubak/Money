package com.money;

import android.app.IntentService;
import android.content.Intent;

import com.money.recomandation.CorrelationRecomandationGenerator;

/**
 * Created by skuba on 08.05.2016.
 */
public class RecomandationIntentSerice extends IntentService {

    public RecomandationIntentSerice() {
        super(RecomandationIntentSerice.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getIntExtra(Constants.RECOMANDATION_SERVICE_TAG,-1)){
            case Constants.RECOMANDATION_SERVICE_CORRELATION_VALUE:
                CorrelationRecomandationGenerator generator = new CorrelationRecomandationGenerator(
                        getApplicationContext(),
                        intent.getLongExtra(Constants.START_DATE_TAG, -1),
                        intent.getLongExtra(Constants.FINISH_DATE_TAG, -1));
                generator.getCorrelationRecomandation();
        }
    }
}
