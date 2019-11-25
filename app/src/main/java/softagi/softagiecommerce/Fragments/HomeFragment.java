package softagi.softagiecommerce.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.softagiecommerce.Models.BrandModel;
import softagi.softagiecommerce.Models.ProductModel;
import softagi.softagiecommerce.R;

public class HomeFragment extends Fragment
{
    private View view;
    private brandAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        initSlider();
        initSearch();
    }

    private void initSearch()
    {
        SearchView searchView = view.findViewById(R.id.searchview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initSlider()
    {
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        List<Integer> integers = new ArrayList<>();

        integers.add(R.drawable.a);
        integers.add(R.drawable.b);
        integers.add(R.drawable.c);

        sliderView.setSliderAdapter(new SliderAdapterExample(integers));
    }

    private void initViews()
    {
        final RecyclerView brand_rv = view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        brand_rv.setLayoutManager(layoutManager);
        brand_rv.addItemDecoration(dividerItemDecoration);

        final List<BrandModel> brandModels = new ArrayList<>();
        /*List<ProductModel> productModels = new ArrayList<>();

        productModels.add(new ProductModel(R.drawable.shoes, "Classic Shoes 1"));
        productModels.add(new ProductModel(R.drawable.shoes, "Classic Shoes 2"));
        productModels.add(new ProductModel(R.drawable.shoes, "Classic Shoes 3"));
        productModels.add(new ProductModel(R.drawable.shoes, "Classic Shoes 4"));
        productModels.add(new ProductModel(R.drawable.shoes, "Classic Shoes 5"));*/

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Brands").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                brandModels.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    BrandModel brandModel = dataSnapshot1.getValue(BrandModel.class);
                    brandModels.add(brandModel);
                }

                adapter = new brandAdapter(brandModels);
                brand_rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public class brandAdapter extends RecyclerView.Adapter<brandAdapter.brandVH> implements Filterable
    {
        List<BrandModel> bb;
        List<BrandModel> filterddoctorModels;

        brandAdapter(List<BrandModel> bb)
        {
            this.bb = bb;
            this.filterddoctorModels = new ArrayList<>(bb);
        }

        @NonNull
        @Override
        public brandVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.brand_item, parent, false);
            return new brandVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull brandVH holder, int position)
        {
            BrandModel brandModel = bb.get(position);

            String name = brandModel.getTitle();
            String categ = brandModel.getCateg();
            String img = brandModel.getImg();
            String id = brandModel.getId();

            Picasso.get()
                    .load(img)
                    .into(holder.brand_img);

            holder.brand_name.setText(name);
            holder.brand_categ.setText(categ);
            holder.setProducts(id);
        }

        @Override
        public int getItemCount()
        {
            return bb.size();
        }

        @Override
        public Filter getFilter()
        {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                List<BrandModel> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0)
                {
                    filteredList.addAll(filterddoctorModels);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (BrandModel item : filterddoctorModels)
                    {
                        if (item.getTitle().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                bb.clear();
                bb.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        class brandVH extends RecyclerView.ViewHolder
        {
            CircleImageView brand_img;
            TextView brand_name,brand_categ;
            RecyclerView product_rv;
            List<ProductModel> productModels;
            DatabaseReference databaseReference;
            productAdapter adapter;

            brandVH(@NonNull View itemView)
            {
                super(itemView);

                brand_img = itemView.findViewById(R.id.brand_img);
                brand_name = itemView.findViewById(R.id.brand_title);
                brand_categ = itemView.findViewById(R.id.brand_categ);
                product_rv = itemView.findViewById(R.id.recyclerview);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                product_rv.setLayoutManager(layoutManager);

                productModels = new ArrayList<>();
                databaseReference = FirebaseDatabase.getInstance().getReference();
            }

            void setProducts(String id)
            {
                databaseReference.child("Products").child(id).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        productModels.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                            productModels.add(productModel);
                        }
                        adapter = new productAdapter(productModels);
                        product_rv.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        }
    }

    public class productAdapter extends RecyclerView.Adapter<productAdapter.productVH>
    {
        List<ProductModel> pp;

        productAdapter(List<ProductModel> pp)
        {
            this.pp = pp;
        }

        @NonNull
        @Override
        public productVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.product_item, parent, false);
            return new productVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull productVH holder, int position)
        {
            ProductModel productModel = pp.get(position);

            String name = productModel.getTitle();
            String img = productModel.getImg();

            holder.product_name.setText(name);
            Picasso.get()
                    .load(img)
                    .into(holder.product_img);
        }

        @Override
        public int getItemCount()
        {
            return pp.size();
        }

        class productVH extends RecyclerView.ViewHolder
        {
            ImageView product_img;
            TextView product_name;

            productVH(@NonNull View itemView)
            {
                super(itemView);

                product_img = itemView.findViewById(R.id.product_img);
                product_name = itemView.findViewById(R.id.product_name);
            }
        }
    }

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>
    {
        List<Integer> img;

        SliderAdapterExample(List<Integer> img)
        {
            this.img = img;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent)
        {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, int position)
        {
            int url = img.get(position);
            /*if (url.charAt(0) == '|') {
                url = url.substring(1, url.lastIndexOf(url));
            }*/
            /*Picasso.get()
                    .load("http://www.saqah.com/images/" + url)
                    .into(viewHolder.imageViewBackground);*/
            viewHolder.imageViewBackground.setImageResource(url);
        }

        @Override
        public int getCount()
        {
            //slider view count could be dynamic size
            return img.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder
        {
            ImageView imageViewBackground;

            SliderAdapterVH(View itemView)
            {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.image_slide);
            }
        }
    }
}