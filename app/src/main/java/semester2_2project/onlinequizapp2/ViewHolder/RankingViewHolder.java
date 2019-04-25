package semester2_2project.onlinequizapp2.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import semester2_2project.onlinequizapp2.Interface.ItemClickListener;
import semester2_2project.onlinequizapp2.R;


/**
 * Created by HP on 29/1/2018.
 */

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name, txt_score;

    private ItemClickListener itemClickListener;



    public RankingViewHolder(View itemView) {
        super(itemView);
        txt_name =(TextView) itemView.findViewById(R.id.txt_name);
        txt_score =(TextView) itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}


