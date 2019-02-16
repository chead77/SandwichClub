package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private Sandwich mSandwich = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        mSandwich = JsonUtils.parseSandwichJson(json);
        if (mSandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(mSandwich.getImage())
                .into(ingredientsIv);

        setTitle(mSandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        TextView originTV = findViewById(R.id.origin_tv);
        TextView descriptionTV = findViewById(R.id.description_tv);
        TextView ingredientsTV = findViewById(R.id.ingredients_tv);
        TextView alsoKnownTV = findViewById(R.id.also_known_tv);
        if (originTV != null && descriptionTV != null && ingredientsTV != null && alsoKnownTV != null) {
            if (mSandwich.getPlaceOfOrigin().isEmpty())
                ((LinearLayout) originTV.getParent()).setVisibility(View.GONE);
            else {
                ((LinearLayout) originTV.getParent()).setVisibility(View.VISIBLE);
                originTV.setText(mSandwich.getPlaceOfOrigin());
            }
            List<String> alsoKnownAs = mSandwich.getAlsoKnownAs();
            if (alsoKnownAs.size() == 0) {
                ((LinearLayout) alsoKnownTV.getParent()).setVisibility(View.GONE);
            } else {
                ((LinearLayout) alsoKnownTV.getParent()).setVisibility(View.VISIBLE);
                for (int i = 0; i < alsoKnownAs.size(); i++) {
                    alsoKnownTV.append(alsoKnownAs.get(i));
                    if (i < alsoKnownAs.size() - 1) {
                        alsoKnownTV.append("\n");
                    }
                }
            }
            descriptionTV.setText(mSandwich.getDescription());
            List<String> ingredients = mSandwich.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                ingredientsTV.append(ingredients.get(i));
                if (i < ingredients.size() - 1) {
                    ingredientsTV.append("\n");
                }
            }
        }
    }
}
