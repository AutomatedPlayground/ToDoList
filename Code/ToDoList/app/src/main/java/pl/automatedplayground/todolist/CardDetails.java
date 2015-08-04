package pl.automatedplayground.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;

public class CardDetails extends AppCompatActivity {

    public static final String INTENT_CARD = "CARD_FOR_USE";
    public static final String INTENT_CARDTYPE = "CARDTYPE_FOR_USE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        if (getIntent().getSerializableExtra(INTENT_CARD) != null) {
            ICard<String> card = (ICard<String>) getIntent().getSerializableExtra(INTENT_CARD);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentarea, CardDetailsFragment.createFragmentForDetails(card)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentarea, CardDetailsFragment.createFragmentForCreateNew(CardType.fromId(getIntent().getIntExtra(INTENT_CARDTYPE, CardType.TODO.toInt())))).commit();
        }
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_remove) {
//            Toast.makeText(this,"REMOVING",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
