package pl.automatedplayground.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardFactory;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardDetailsFragment extends Fragment {

    @InjectView(R.id.card_title)
    TextView cardTitle;
    @InjectView(R.id.card_title_edit)
    EditText cardTitleEditable;
    @InjectView(R.id.card_content)
    TextView cardContent;
    @InjectView(R.id.card_content_edit)
    EditText cardContentEditable;
    @InjectView(R.id.mode_edit)
    View modeEdit;
    @InjectView(R.id.mode_view)
    View modeView;
    @InjectView(R.id.card_action_first)
    Button action1;
    @InjectView(R.id.card_action_second)
    Button action2;

    boolean editable = false;

    ICard<String> localCard = null;
    private CardType newCardType = CardType.TODO;

    public CardDetailsFragment() {
    }

    public static CardDetailsFragment createFragmentForDetails(ICard<String> card) {
        CardDetailsFragment fragment = new CardDetailsFragment();
        fragment.localCard = card;
        return fragment;
    }

    public static CardDetailsFragment createFragmentForCreateNew(CardType newCardType) {
        CardDetailsFragment fragment = new CardDetailsFragment();
        fragment.newCardType = newCardType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_details, container, false);
        ButterKnife.inject(this, view);
        if (localCard != null)
            bindData(localCard);
        else
            edit();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // we override menu
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (editable && localCard != null)
            inflater.inflate(R.menu.menu_card_edition, menu);
        else if (localCard != null && !editable)
            inflater.inflate(R.menu.menu_card_details, menu);
        else // new card
            inflater.inflate(R.menu.menu_card_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveEdit();
        } else if (item.getItemId() == R.id.action_edit)
            edit();
        else if (item.getItemId() == R.id.action_cancel)
            cancelEdit();
        else if (item.getItemId() == R.id.action_remove)
            remove();
        else
            return super.onOptionsItemSelected(item);
        return true;
    }


    private void bindData(ICard<String> card) {
        cardTitle.setText(card.getTitle());
        cardTitleEditable.setText(card.getTitle());
        cardContent.setText(card.getContent());
        cardContentEditable.setText(card.getContent());
        modeView.setVisibility(View.VISIBLE);
        modeEdit.setVisibility(View.GONE);

        // enable/disable possible actions
        if (card == null) {
            action1.setVisibility(View.GONE);
            action2.setVisibility(View.GONE);
        } else if (card.getType() == CardType.TODO) {
            action2.setText(R.string.card_todo_to_doing);
            action1.setVisibility(View.INVISIBLE);
            action2.setVisibility(View.VISIBLE);
        } else if (card.getType() == CardType.DOING) {
            action2.setText(R.string.card_doing_to_done);
            action1.setText(R.string.card_doing_to_todo);
            action1.setVisibility(View.VISIBLE);
            action2.setVisibility(View.VISIBLE);
        } else if (card.getType() == CardType.TODO) {
            action1.setText(R.string.card_done_to_doing);
            action1.setVisibility(View.VISIBLE);
            action2.setVisibility(View.INVISIBLE);
        }

        getActivity().invalidateOptionsMenu();
    }

    private void edit() {
        modeView.setVisibility(View.GONE);
        modeEdit.setVisibility(View.VISIBLE);
        editable = true;
        getActivity().invalidateOptionsMenu();
    }

    private void cancelEdit() {
        hideKeyboard();
        cardTitleEditable.setText(cardTitle.getText().toString());
        cardContentEditable.setText(cardContent.getText().toString());
        modeView.setVisibility(View.VISIBLE);
        modeEdit.setVisibility(View.GONE);
        editable = false;
        getActivity().invalidateOptionsMenu();
    }

    private void saveEdit() {
        hideKeyboard();
        cardTitle.setText(cardTitleEditable.getText().toString());
        cardContent.setText(cardContentEditable.getText().toString());
        if (localCard != null) {
            // change card data
            localCard.setData(localCard.getLocalID(), cardTitleEditable.getText().toString(), cardContentEditable.getText().toString(), localCard.getID());
            CardFactory.getInstance().changeCardData(localCard, null);
        } else {
            // create new card
            CardFactory.getInstance().createNewCard(cardTitleEditable.getText().toString(), cardContentEditable.getText().toString(), newCardType, null);
            getActivity().finish();
        }


        modeView.setVisibility(View.VISIBLE);
        modeEdit.setVisibility(View.GONE);
        editable = false;

        getActivity().invalidateOptionsMenu();
    }


    /**
     * Remove current card from local database
     */
    private void remove() {
        hideKeyboard();
        CardFactory.getInstance().removeCard(localCard, new SimpleCallback<Boolean>() {
            @Override
            public void onCallback(Boolean obj) {
                getActivity().finish();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
