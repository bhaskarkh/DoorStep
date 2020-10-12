package com.bhaskar.doorstep.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhaskar.doorstep.ChangeAddress;
import com.bhaskar.doorstep.R;
import com.bhaskar.doorstep.model.AddressDTO;
import com.bhaskar.doorstep.model.ProductDTO;
import com.bhaskar.doorstep.util.AddressServices;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddresListViewHolder>{
Context context;
List<AddressDTO> addressDTOList;
Intent intentforSource;
    private static final String TAG = "AddressListAdapter";

    public AddressListAdapter(Context context, List<AddressDTO> addressDTOList, Intent intentforSource) {
        this.context = context;
        this.addressDTOList = addressDTOList;
        this.intentforSource = intentforSource;
    }

    @NonNull
    @Override
    public AddresListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.address_list_card_view,parent,false);
        return new AddresListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddresListViewHolder holder, int position) {

        AddressServices addressServices=new AddressServices(context);
        AddressDTO addressDTO=addressDTOList.get(position);
        holder.address_list_name.setText(addressDTO.getName());
        holder.address_list_house_road_landmark.setText(addressServices.firstLineAddress(addressDTO));
        holder.addresslist_city_state_pin.setText(addressServices.secondLineAddress(addressDTO));
        holder.addresslist_rmn.setText(addressDTO.getDeliveryMobileNo());
        if (addressDTO.isPrimaryAddress()) {
            holder.default_address_text.setVisibility(View.VISIBLE);
        } else {
            holder.default_address_text.setVisibility(View.GONE);
        }
        ;

        holder.edit_address_btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: name= "+ addressDTO.getName());
                Intent intent=new Intent(context, ChangeAddress.class);
                String source=intentforSource.getStringExtra("source");

                intent.putExtra("source",source);
                intent.putExtra("selected_product",(ProductDTO)intentforSource.getParcelableExtra("selected_product"));
                intent.putExtra("method","edit");
                intent.putExtra("addressVal",addressDTO);
                if(source.equals("ProductDetails")) {
                    intent.putExtra("cat_name", intentforSource.getStringExtra("cat_name"));
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressDTOList.size();
    }

    public static class AddresListViewHolder extends RecyclerView.ViewHolder {
        TextView address_list_name,address_list_house_road_landmark,addresslist_city_state_pin,addresslist_rmn;
        TextView edit_address_btn_text,default_address_text;



        public AddresListViewHolder(@NonNull View itemView) {
            super(itemView);
            address_list_name=itemView.findViewById(R.id.address_list_name);
            address_list_house_road_landmark=itemView.findViewById(R.id.address_list_house_road_landmark);
            addresslist_city_state_pin=itemView.findViewById(R.id.addresslist_city_state_pin);
            addresslist_rmn=itemView.findViewById(R.id.addresslist_rmn);
            edit_address_btn_text=itemView.findViewById(R.id.edit_address_btn_text);
            default_address_text=itemView.findViewById(R.id.default_address_text);


        }
    }
}
