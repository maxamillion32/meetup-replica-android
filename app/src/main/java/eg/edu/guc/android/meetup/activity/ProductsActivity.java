package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Product;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductsActivity extends BasePrivateActivity {
	private ArrayAdapter<Product> adpProducts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);

		setUpViews();
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshViews();
	}

	private void setUpViews() {
		ListView lstProducts = (ListView) findViewById(R.id.lst_products);
		adpProducts = new ArrayAdapter<Product>(this, 0) {
			private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final Product product = getItem(position);

				View view;
				if (convertView == null) {
					view = mInflater.inflate(R.layout.view_product, parent, false);
				} else {
					view = convertView;
				}

				TextView txtName = (TextView) view.findViewById(R.id.txt_name);
				txtName.setText(product.getName());

				ImageView imgImage = (ImageView) view.findViewById(R.id.img_image);
				Picasso.with(ProductsActivity.this).load(product.getImageUrl()).into(imgImage);

				Button btnBuy = (Button) view.findViewById(R.id.btn_buy);
				btnBuy.setEnabled(product.getStock() > 0);
				btnBuy.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
                        startProgress();

                        ApiRouter.withToken(getCurrentUser().getToken()).patchProductBuy(product.getId(),
                                new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response rawResponse) {
                                        Toast.makeText(ProductsActivity.this, "Bought: " + product.getName(),
                                                Toast.LENGTH_LONG).show();

                                        stopProgress();

                                        product.setStock(product.getStock() - 1);
                                        adpProducts.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void failure(RetrofitError e) {
                                        displayError(e);
                                    }
                                });
					}
				});

				return view;
			}
		};
		lstProducts.setAdapter(adpProducts);
	}

	protected void refreshViews() {
	    super.refreshViews();

        adpProducts.clear();

        startProgress();

        ApiRouter.withoutToken().getProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                adpProducts.addAll(products);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
	}
}
