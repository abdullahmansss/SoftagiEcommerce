package softagi.softagiecommerce.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.softagiecommerce.Models.BrandModel;
import softagi.softagiecommerce.R;

public class AddNewProductFragment extends Fragment
{
    View view;
    brandAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.add_new_product_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
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
        public brandAdapter.brandVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.brand_item2, parent, false);
            return new brandAdapter.brandVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull brandVH holder, int position)
        {
            BrandModel brandModel = bb.get(position);

            String name = brandModel.getTitle();
            final String id = brandModel.getId();
            String img = brandModel.getImg();

            Picasso.get()
                    .load(img)
                    .into(holder.brand_img);

            holder.brand_name.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NewFragment newFragment = new NewFragment(id);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newFragment, "new").addToBackStack(null).commit();
                }
            });
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
            TextView brand_name;

            brandVH(@NonNull View itemView)
            {
                super(itemView);

                brand_img = itemView.findViewById(R.id.brand_img);
                brand_name = itemView.findViewById(R.id.brand_title);
            }
        }
    }
}
