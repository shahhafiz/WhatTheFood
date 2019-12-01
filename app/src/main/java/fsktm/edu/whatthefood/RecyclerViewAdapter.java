package fsktm.edu.whatthefood;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";


    private List<Item> itemList;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext,List<Item> itemList) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_item,parent, false);
        ViewHolder holder = new ViewHolder(view);
        
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Log.d(TAG,"onBindViewHolder : called.") ;

     Item item = itemList.get(position);

     Glide.with(mContext)
             .asBitmap()
             .load(item.getImageURL())
             .into(holder.itemImage);

    holder.itemName.setText(item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1));
    holder.itemCal.setText(item.getCalories());

     holder.parentLayout.setOnClickListener(new View.OnClickListener(){
         public void onClick(View view){
             Intent i = new Intent(mContext, NutritionDetails.class);
             i.putExtra("Name",item.getName());
             mContext.startActivity(i);


         }
     });
     
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName, itemCal;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemCal =  itemView.findViewById(R.id.itemCal);
            parentLayout = itemView.findViewById(R.id.parentlayout);
        }
    }
}
