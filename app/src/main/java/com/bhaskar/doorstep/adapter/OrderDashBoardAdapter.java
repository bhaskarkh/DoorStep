package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.OrderStatusChange;
import com.bhaskar.doorstep.OrderStatusDetail;
import com.bhaskar.doorstep.ProductDetails;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.OrderDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.model.ShippingDTO;
import com.bhaskar.doorstep.services.Home;

import java.util.List;

public class OrderDashBoardAdapter extends RecyclerView.Adapter<OrderDashBoardAdapter.OrderDashBoardViewHolder>{

    Context context;
    List<OrderDTO> orderDTOList;

    public OrderDashBoardAdapter(Context context, List<OrderDTO> orderDTOList) {
        this.context = context;
        this.orderDTOList = orderDTOList;
    }

    @NonNull
    @Override
    public OrderDashBoardAdapter.OrderDashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_dashboard_card_view, parent, false);
        return new OrderDashBoardAdapter.OrderDashBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDashBoardAdapter.OrderDashBoardViewHolder holder, int position) {
        Home hm=new Home(context);
        OrderDTO orderDTO=orderDTOList.get(position);
        ProductDTO productDTO=orderDTOList.get(position).getProductDTO();
        ShippingDTO shippingDTO=orderDTOList.get(position).getShippingDTO();
        String firstCapsProdName=hm.getFirstUpperCase(productDTO.getName());
        holder.prod_name.setText(firstCapsProdName);
        if(firstCapsProdName.length()>16)
        {
            hm.limitCharacterInView(holder.prod_name,16,firstCapsProdName);
        }

        holder.order_status.setText(orderDTOList.get(position).getOrderStatus());
        hm.changeColorOfTextView(holder.order_status,orderDTOList.get(position).getOrderStatus(),"order_status");
        holder.price.setText(hm.appendRuppeSymbol(String.valueOf(productDTO.getMyPrice())));
        holder.delivery_date.setText(orderDTOList.get(position).getExpectedLastDateOfDelivery());
        hm.loadImageInGlide(holder.prod_image,productDTO.getImage());
        // holder.prod_image.setImageResource(R.drawable.ic_home_fish);
        holder.order_st_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "orderStatus Clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, OrderStatusChange.class);
                intent.putExtra("OrderInfo",orderDTO);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDTOList.size();
    }

    public static class OrderDashBoardViewHolder extends  RecyclerView.ViewHolder{

        TextView prod_name,order_status,price,delivery_date;
        ImageView prod_image;
        ConstraintLayout order_st_layout;


        public OrderDashBoardViewHolder(@NonNull View itemView) {
            super(itemView);

            prod_name=itemView.findViewById(R.id.order_dashboard_st_prod_name);
            order_status=itemView.findViewById(R.id.order_dashboard_st_status);
            // order_date=itemView.findViewById(R.id.order_st_date_time);
            price=itemView.findViewById(R.id.order_dashboard_st_price);
            delivery_date=itemView.findViewById(R.id.order_dashboard_st_delivery_date);
            prod_image=itemView.findViewById(R.id.order_dashboard_st_product_image);
            order_st_layout=itemView.findViewById(R.id.order_dashboard_st_layout);
        }
    }
}
