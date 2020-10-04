package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.ShippingDTO;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    Context context;
    List<OrderDTO> orderDTOList;

    public OrderHistoryAdapter(Context context, List<OrderDTO> orderDTOList) {
        this.context = context;
        this.orderDTOList = orderDTOList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.order_history_card_view,parent,false);

        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        ProductDTO productDTO=orderDTOList.get(position).getProductDTO();
        ShippingDTO shippingDTO=orderDTOList.get(position).getShippingDTO();

        holder.prod_name.setText(productDTO.getName());
        holder.order_status.setText(orderDTOList.get(position).getOrderStatus());
        holder.order_date.setText(orderDTOList.get(position).getOrderDateTime());
        holder.order_id.setText(orderDTOList.get(position).getOrderId());
        holder.delivery_date.setText(orderDTOList.get(position).getExpectedLastDateOfDelivery());
        holder.prod_image.setImageResource(R.drawable.ic_home_fish);


    }

    @Override
    public int getItemCount() {
        return orderDTOList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder{
        TextView prod_name,order_status,order_date,order_id,delivery_date;
        ImageView prod_image;


        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_name=itemView.findViewById(R.id.order_st_prod_name);
            order_status=itemView.findViewById(R.id.order_st_status);
            order_date=itemView.findViewById(R.id.order_st_date_time);
            order_id=itemView.findViewById(R.id.order_st_order_id);
            delivery_date=itemView.findViewById(R.id.order_st_delivery_date);
            prod_image=itemView.findViewById(R.id.order_st_product_image);

        }
    }

}
