package ucucite.edu.epass_admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable {

    List<Userstravellers> persons, personsFilter;
    private Context context;
    private RecyclerViewClickListener mListener;
    UsersCustomFilter filter;

    public UsersAdapter(List<Userstravellers> persons, Context context, RecyclerViewClickListener listener) {
        this.persons = persons;
        this.personsFilter = persons;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mName.setText(persons.get(position).getName());
        holder.mType.setText("Password: " +persons.get(position).getBreed());
        holder.mDate.setText("Username : " +persons.get(position).getSpecies());
        //holder.mgapo.setText(pets.get(position).getGapo());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(context)
                .load(persons.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);

      //  final Boolean love = pets.get(position).getLove();

       // if (love){
        //    holder.mLove.setImageResource(R.drawable.likeon);
       // } else {
       //     holder.mLove.setImageResource(R.drawable.likeof);
      //  }

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null) {
            filter=new UsersCustomFilter((ArrayList<Userstravellers>) personsFilter,this);

        }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private CircleImageView mPicture;
      //  private ImageView mLove;
        private TextView mName, mType, mDate;
        private RelativeLayout mRowContainer;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.picture);
            mName = itemView.findViewById(R.id.name);
            mType = itemView.findViewById(R.id.type);
          //  mLove = itemView.findViewById(R.id.love);
            mDate = itemView.findViewById(R.id.date);
           // mgapo = itemView.findViewById(R.id.gapo);

            mRowContainer = itemView.findViewById(R.id.row_container);

            mListener = listener;
            mRowContainer.setOnClickListener(this);
          //  mLove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    mListener.onRowClick(mRowContainer, getAdapterPosition());
                    break;
               // case R.id.love:
               //     mListener.onLoveClick(mLove, getAdapterPosition());
               //     break;
                default:
                    break;
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
      //  void onLoveClick(View view, int position);
    }

}




